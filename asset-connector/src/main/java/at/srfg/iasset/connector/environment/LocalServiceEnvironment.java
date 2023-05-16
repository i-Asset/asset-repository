package at.srfg.iasset.connector.environment;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.DataElement;
import org.eclipse.aas4j.v3.model.Key;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.config.MarshallingFeature;
import at.srfg.iasset.connector.component.endpoint.HttpComponent;
import at.srfg.iasset.connector.component.endpoint.RepositoryConnection;
import at.srfg.iasset.connector.component.endpoint.controller.AssetAdministrationRepositoryController;
import at.srfg.iasset.connector.component.endpoint.controller.AssetAdministrationShellController;
import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.repository.component.ModelChangeProvider;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.Persistence;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASJacksonMapperProvider;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.InMemoryStorage;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.custom.InstanceProperty;
import at.srfg.iasset.repository.model.helper.SubmodelHelper;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.visitor.EventElementCollector;
import at.srfg.iasset.repository.model.helper.visitor.SubmodelElementCollector;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.repository.utils.SubmodelUtils;

public class LocalServiceEnvironment implements ServiceEnvironment, LocalEnvironment {
	private final Logger logger = LoggerFactory.getLogger(LocalServiceEnvironment.class);
//	private InstanceEnvironment environment;
	private Persistence storage;
	
//	private final Collection<ModelListener> listeners = new HashSet<ModelListener>();
	private final ObjectMapper objectMapper;
	private final Set<String> registrations = new HashSet<String>();
	private final URI repositoryURI;
	private final RepositoryConnection repository;
	private ConnectorEndpoint httpEndpoint;
	private ConnectorMessaging eventProcessor;
	
	private ModelChangeProvider changeProvider = ModelChangeProvider.getProvider();

	
	public LocalServiceEnvironment(URI repositoryURI) {
		this.repositoryURI = repositoryURI;
		this.repository = RepositoryConnection.getConnector(repositoryURI);
		// in the local service environment we may use this objectmapper
		this.objectMapper = ClientFactory.getObjectMapper();
		// obtain the messaging component
		eventProcessor =ConnectorMessaging.create(objectMapper, this);
		storage = new InMemoryStorage();
//		environment = new InstanceEnvironment(changeProvider);
		// The model update listener is currently not working!
		changeProvider.addListener(new ModelListener() {
			
			@Override
			public void propertyRemoved(String submodelIdentifier, String path, Property property) {
				logger.debug("Property Element removed at path {}", path);
			}
			
			@Override
			public void propertyCreated(String submodelIdentifier, String path, Property property) {
				logger.debug("Property Element created at path {}", path);
				
			}
			
			@Override
			public void operationRemoved(String submodelIdentifier, String path, Operation operation) {
				logger.debug("Operation Element removed at path {}", path);
				
			}
			
			@Override
			public void operationCreated(String submodelIdentifier, String path, Operation operation) {
				logger.debug("Operation Element created at path {}", path);
				
			}
			
			@Override
			public void eventElementRemoved(String submodelIdentifier, String path, BasicEventElement eventElement) {
				logger.debug("Event Element removed at path {}", path);
				
			}
			
			@Override
			public void eventElementCreated(String submodelIdentifier, String path, BasicEventElement eventElement) {
				// tell the processor that a new event element has been added
				logger.debug("Event Element created at path {}", path);
				
			}

			@Override
			public void submodelElementCreated(String submodelIdentifier, String path, SubmodelElement element) {
				logger.debug("Submodel Element created at path {}", path);
				
			}
			@Override
			public void submodelElementRemoved(String submodelIdentifier, String path, SubmodelElement element) {
				logger.debug("Submodel Element Removed at path {}", path);
				System.out.println("Element created: " + path + " - " + element.getIdShort());
				
			}

			@Override
			public void dataElementChanged(String submodelIdentifier, String path, DataElement property) {
				System.out.println("Property changed: " + path + " - " + property.getIdShort());
				
			}

		});
	}
	
	@Override
	public void addAdministrationShell(AssetAdministrationShell shell) {
		setAssetAdministrationShell(shell.getId(), shell);
		
	}

	@Override
	public void addSubmodel(String aasIdentifer, Submodel submodel) {
		setSubmodel(aasIdentifer, submodel.getId(), submodel);
		
	}

	public void addModelListener(ModelListener listener) {
		changeProvider.addListener(listener);
	}
	public void removeModelListener(ModelListener listener) {
		changeProvider.removeListener(listener);
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> shell = storage.findAssetAdministrationShellById(aasIdentifier);
		if ( shell.isPresent()) {
			if ( ReferenceUtils.extractReferenceFromList(shell.get().getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL).isPresent() ) {
				return storage.findSubmodelById(submodelIdentifier);
			}		
		}
		return Optional.empty();
	}
	public Optional<Submodel> getSubmodel(String identifier) {
		return storage.findSubmodelById(identifier).or(new Supplier<Optional<? extends Submodel>>() {

			@Override
			public Optional<? extends Submodel> get() {
				// TODO Auto-generated method stub
				Optional<Submodel> fromRemote = repository.getSubmodel(identifier);
				if (fromRemote.isPresent()) {
					return Optional.of(internalSubmodel(identifier, fromRemote.get()));
//					return Optional.of(storage.persist(fromRemote.get()));
				}
				return Optional.empty();
			}
		});
		
	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier) {
		return storage.findAssetAdministrationShellById(identifier);
	}

	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier, AssetAdministrationShell theShell) {
		theShell.setId(aasIdentifier);
		return storage.persist(theShell);
	}
	
	@Override
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		return storage.findConceptDescriptionById(identifier)
			.or(new Supplier<Optional<ConceptDescription>>() {
				/**
				 * Obtain the requested {@link ConceptDescription} from the repository!
				 */
				@Override
				public Optional<ConceptDescription> get() {
					Optional<ConceptDescription> fromRepo = repository.getConceptDescription(identifier);
					if ( fromRepo.isPresent()) {
						// 
						return Optional.of(storage.persist(fromRepo.get()));
						
					}
					return Optional.empty();
				}});
	}

	@Override
	public boolean deleteAssetAdministrationShellById(String identifier) {
		storage.deleteAssetAdministrationShellById(identifier);
		return true;
	}

	public boolean deleteSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<Submodel> toDelete = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( toDelete.isPresent() ) {
			storage.deleteSubmodelById(submodelIdentifier);
			notifyDeletion(toDelete.get(), "", toDelete.get());
			return true;
		}
		return false;
	}
	@Override
	public boolean deleteSubmodelReference(String aasIdentifier, Reference ref) {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public <T extends Referable> Optional<T> resolve(Reference reference, Class<T> type) {
		
		Optional<Referable> ref = resolve(reference);
		if ( ref.isPresent() && type.isInstance(ref.get()) ) {
			return Optional.of(type.cast(ref.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
		return storage.getAssetAdministrationShells();
	}

	@Override
	public boolean deleteSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Optional<SubmodelElement> deleted = helper.removeSubmodelElementAt(path);
			deleted.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					notifyDeletion(submodel.get(), path, t);
				}
			});
			return deleted.isPresent();
		}
		return false;
	}

	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, String idShortPath,
			SubmodelElement body) {
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			// 
			
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Optional<SubmodelElement> oldElement = helper.removeSubmodelElementAt(idShortPath);
			oldElement.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					notifyDeletion(submodel.get(), idShortPath, t);
					
				}
			});

			Optional<SubmodelElement> added = helper.setSubmodelElementAt(idShortPath, body);
			added.ifPresent(new Consumer<SubmodelElement>() {
				

				@Override
				public void accept(SubmodelElement t) {
					notifyCreation(submodel.get(), idShortPath, t);
					
				}
			});
			if ( added.isPresent()) {
				return added.get();
			}
			return null;
		}
		return null;
	}

	@Override
	public Optional<SubmodelElement> getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			return new SubmodelHelper(submodel.get()).getSubmodelElementAt(path);
		}
		return Optional.empty();
	}
	private Submodel internalSubmodel(String submodelIdentifier, Submodel submodel) {
		Optional<Submodel> existing = storage.findSubmodelById(submodelIdentifier);

		existing.ifPresent(new Consumer<Submodel>() {
			
			@Override
			public void accept(Submodel t) {
				notifyDeletion(submodel, "", t);
			}
		});
		submodel.setId(submodelIdentifier);
		Submodel stored = storage.persist(submodel);
		notifyCreation(submodel, "", stored);
		return stored;
	}
	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel) {
		Optional<AssetAdministrationShell> shell = getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> ref = ReferenceUtils.extractReferenceFromList(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isEmpty()) {
				Reference newRef = ReferenceUtils.toReference(submodel);
				theShell.getSubmodels().add(newRef);
			}
			Optional<Submodel> existing = storage.findSubmodelById(submodelIdentifier);

			existing.ifPresent(new Consumer<Submodel>() {
				
				@Override
				public void accept(Submodel t) {
					notifyDeletion(submodel, "", t);
				}
			});
			
			submodel.setId(submodelIdentifier);
			Submodel stored = storage.persist(submodel);
			notifyCreation(submodel, "", stored);
			return stored;
		}

		return null;
	}

	@Override
	public Optional<Referable> getSubmodelElement(AssetAdministrationShell aasIdentifier, Reference element) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Object getElementValue(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent() ) {
			return new SubmodelHelper(sub.get()).getValueAt(path);
		}

		return new HashMap<String, Object>();
	}

	@Override
	public void setElementValue(String aasIdentifier, String submodelIdentifier, String path, Object value) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent() ) {
			// make a json node out of it
			JsonNode node = ClientFactory.getObjectMapper().valueToTree(value);
			Optional<SubmodelElement> element = new SubmodelHelper(sub.get()).setValueAt(path, node);
			if ( element.isPresent()) {
				notifyChange(sub.get(), path, element.get());
			}
		}
		
	}

	@Override
	public ConceptDescription setConceptDescription(String cdIdentifier, ConceptDescription conceptDescription) {
		conceptDescription.setId(cdIdentifier);
		return storage.persist(conceptDescription);
	}

	@Override
	public List<Reference> getSubmodelReferences(String aasIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> setSubmodelReferences(String aasIdentifier, List<Reference> submodels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> deleteSubmodelReference(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> shell = getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> ref = ReferenceUtils.extractReferenceFromList(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isPresent()) {
				theShell.getSubmodels().remove(ref.get());
				return theShell.getSubmodels();
			}
		}
		return Collections.emptyList();
	}

	@Override
	public SubmodelElement setSubmodelElement(String id, String submodelIdentifier, SubmodelElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeOperation(String aasIdentifier, String submodelIdentifier, String path, Object parameterMap) {
		// TODO Auto-generated method stub
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			Optional<Operation> operation = new SubmodelHelper(sub.get()).getSubmodelElementAt(path,Operation.class);
			if ( operation.isPresent() ) {
				if (InstanceOperation.class.isInstance(operation.get())) {
					InstanceOperation theOperation = InstanceOperation.class.cast(operation.get());
					return theOperation.invoke(parameterMap);
					
				}
			}
		}
		return null;
	}

	@Override
	public Object invokeOperation(Reference operation, Object parameter) {
		Optional<Operation> theOperation = resolve(operation, Operation.class);
		if ( theOperation.isPresent()) {
			if (InstanceOperation.class.isInstance(theOperation.get())) {
				InstanceOperation instanceOperation = InstanceOperation.class.cast(theOperation.get());
				return instanceOperation.invoke(parameter);
			}
		}
		return null;
	}

	@Override
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path, Consumer<String> consumer) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(sub.get());
			Optional<Property> property = helper.getSubmodelElementAt(path, Property.class);
			if ( property.isPresent()) {
				Property theProp = property.get();
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceProperty(theProp);
					helper.setSubmodelElementAt(path, theProp);
				}
				if ( InstanceProperty.class.isInstance(theProp)) {
					InstanceProperty iProp = InstanceProperty.class.cast(theProp);
					iProp.consumer(consumer);
				}
				
			}
		}
	}

	@Override
	public void setValueSupplier(String aasIdentifier, String submodelIdentifier, String path, Supplier<String> supplier) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(sub.get());
			Optional<Property> property = helper.getSubmodelElementAt(path, Property.class);
			if ( property.isPresent()) {
				Property theProp = property.get();
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceProperty(theProp);
					helper.setSubmodelElementAt(path, theProp);
				}
				if ( InstanceProperty.class.isInstance(theProp)) {
					InstanceProperty iProp = InstanceProperty.class.cast(theProp);
					iProp.supplier(supplier);
				}
				
			}
		}
		// 
		
	}

	@Override
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path,
			Function<Object, Object> operation) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(sub.get());
			Optional<Operation> property = helper.getSubmodelElementAt(path, Operation.class);
			if ( property.isPresent()) {
				Operation theProp = property.get();
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceOperation(theProp);
					helper.setSubmodelElementAt(path, theProp);
				}
				if ( InstanceOperation.class.isInstance(theProp)) {
					InstanceOperation iProp = InstanceOperation.class.cast(theProp);
					iProp.function(operation);
				}
				
			}
		}
		
	}
	public String getHostAddress() {
		try {
			InetAddress adr = InetAddress.getLocalHost();
			return adr.getHostAddress();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}
	public boolean register(String aasIdentifier) {

		Optional<AssetAdministrationShell> shellToRegister = getAssetAdministrationShell(aasIdentifier);
		if ( shellToRegister.isPresent()) {
			String idEncoded = Base64Utils.encodeToString(aasIdentifier.getBytes());
			String pathToShell = repositoryURI.getPath() + String.format("shells/%s", idEncoded);
			URI shellUri = URI.create(String.format("%s://%s:%s%s", repositoryURI.getScheme(), getHostAddress(), httpEndpoint.getPort(), pathToShell));
			if (repository.register(shellToRegister.get(), shellUri)) {
				// keep in the list of active registrations
				registrations.add(aasIdentifier);
				return true;
			}
		}
		return false;
	}
	/**
	 * Unregister all registered {@link AssetAdministrationShell}s of the current I4.0 Component from the repository. 
	 */
	public void unregister() {
		registrations.forEach(new Consumer<String>() {

			@Override
			public void accept(String aasIdentifier) {
				repository.unregister(aasIdentifier);
			}
		});
		registrations.clear();
	}
	/**
	 * Unregister the {@link AssetAdministrationShell} from the repository.
	 * @param aasIdentifier
	 */
	public void unregister(String aasIdentifier) {
		if ( registrations.contains(aasIdentifier)) {
			if (repository.unregister(aasIdentifier)) {
				registrations.remove(aasIdentifier);
			}
		}
	}

	@Override
	public ConnectorEndpoint startEndpoint(int port) {
		httpEndpoint = new HttpComponent(port);
		
		httpEndpoint.start("/", getRootConfig());
		return httpEndpoint;
	}
	public void startEndpoint(int port, String contextPath) {
		httpEndpoint = new HttpComponent(port);
		
		httpEndpoint.start(contextPath, getRootConfig());
	}
	
	private ResourceConfig getRootConfig() {
		ResourceConfig rootConfig = new ResourceConfig();
		ServiceEnvironment injectEnvironment = this;
		rootConfig.register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(injectEnvironment).to(ServiceEnvironment.class);
				
			}
		});
		rootConfig.register(AASJacksonMapperProvider.class);
		rootConfig.register(MarshallingFeature.class);
		rootConfig.register(AssetAdministrationRepositoryController.class);
		return rootConfig;
	}
	
	private void notifyChange(Submodel submodel, String path, SubmodelElement submodelElement) {
		Map<String, SubmodelElement> elements = new SubmodelElementCollector().collectMap(path, submodelElement);
		
		elements.forEach(new BiConsumer<String, SubmodelElement>() {

			@Override
			public void accept(String t, SubmodelElement u) {
				if ( DataElement.class.isInstance(u)) {
					changeProvider.valueChanged(submodel.getId(), path, DataElement.class.cast(u));
				}
			
			}
		});		
	}

	private <T extends Referable> void notifyDeletion(Submodel submodel, String pathToElement, T deletedElement) {
		// check for the messaging component
		Map<String, SubmodelElement> elements = new SubmodelElementCollector().collectMap(pathToElement, deletedElement);
		
		elements.forEach(new BiConsumer<String, SubmodelElement>() {

			@Override
			public void accept(String t, SubmodelElement u) {
				if ( BasicEventElement.class.isInstance(u)) {
					changeProvider.eventElementRemoved(t, pathToElement, null);
					// handle removal from messaging
					Reference elementRef = new SubmodelHelper(submodel).getReference(pathToElement);
					eventProcessor.removeEventElement(elementRef);
					
				}
				else if ( Operation.class.isInstance(u)) {
					changeProvider.operationRemoved(submodel.getId(), pathToElement, Operation.class.cast(u));
				}
				else if ( Property.class.isInstance(u)) {
					changeProvider.propertyRemoved(submodel.getId(), pathToElement, Property.class.cast(u));
				} 
				else {
					changeProvider.elementRemoved(submodel.getId(), pathToElement, u);
				}
				
			}
		});
	}
	private <T extends Referable> void notifyCreation(final Submodel submodel, String pathToElement, T createdElement) {
		new SubmodelElementCollector().collectMap(pathToElement, createdElement).forEach(new BiConsumer<String, SubmodelElement>() {

			@Override
			public void accept(String t, SubmodelElement u) {
				if ( BasicEventElement.class.isInstance(u)) {
					changeProvider.eventElementCreated(submodel.getId(), t, BasicEventElement.class.cast(u));
					Reference elementRef = new SubmodelHelper(submodel).getReference(t);
					eventProcessor.registerEventElement(elementRef);
					
				}
				else if ( Operation.class.isInstance(u)) {
					changeProvider.operationCreated(submodel.getId(), t, Operation.class.cast(u));
				}
				else if ( Property.class.isInstance(u)) {
					changeProvider.propertyCreated(submodel.getId(), t, Property.class.cast(u));
				}
				else {
					changeProvider.elementCreated(submodel.getId(), t, u);
				}
					
				
			}
		});


	}
	/**
	 * Create a {@link ResourceConfig} used for dedicated {@link HttpHandler}.
	 * 
	 * 
	 */
	private ResourceConfig getShellConfig(AssetAdministrationShell forShell) {
		ResourceConfig shellConfig = new ResourceConfig();
		final ServiceEnvironment injectedEnvironment = this;
		shellConfig.register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(injectedEnvironment).to(ServiceEnvironment.class);
				
			}
		});
		shellConfig.register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(forShell).to(AssetAdministrationShell.class);
				
			}
		});
		shellConfig.register(AASJacksonMapperProvider.class);
		shellConfig.register(MarshallingFeature.class);
		shellConfig.register(AssetAdministrationShellController.class);
		return shellConfig;
	}
	@Override
	public void shutdownEndpoint() {
		if ( httpEndpoint.isStarted() ) {
			// unregister this environment
			unregister();
			httpEndpoint.stop();
		}
	}

	@Override
	public void addHandler(String aasIdentifier) {
		Optional<AssetAdministrationShell> theShell = getAssetAdministrationShell(aasIdentifier);
		if ( theShell.isPresent()) {
			httpEndpoint.addHttpHandler(theShell.get().getIdShort(), getShellConfig(theShell.get()));
		}
		
	}

	@Override
	public void addHandler(String aasIdentifier, String alias) {
		Optional<AssetAdministrationShell> theShell = getAssetAdministrationShell(aasIdentifier);
		if ( theShell.isPresent()) {
			httpEndpoint.addHttpHandler(alias, getShellConfig(theShell.get()));
		}
		
	}
	@Override
	public void removeHandler(String alias) {
		Optional<AssetAdministrationShell> theShell = getAssetAdministrationShell(alias);
		if ( theShell.isPresent()) {
			httpEndpoint.removeHttpHandler(theShell.get().getIdShort());
		}
		// 
		httpEndpoint.removeHttpHandler(alias);
		
	}

	@Override
	public <T> void addMesssageListener(Reference reference, EventHandler<T> listener) {
		getEventProcessor().registerHandler(reference, listener);
		
	}

	@Override
	public <T> EventProducer<T> getMessageProducer(Reference reference, Class<T> clazz) {
		return getEventProcessor().getProducer(reference, clazz);
	}

	@Override
	public <T> EventProducer<T> getMessageProducer(String submodelIdentifier, Reference reference, Class<T> clazz) {
		Optional<Submodel> sub = getSubmodel(submodelIdentifier);
		if ( sub.isPresent()) {
			return getEventProcessor().getProducer(reference, clazz);
		}
		// fallback
		return null;
	}

	@Override
	public <T> void registerEventHandler(String submodelIdentifier, Reference reference, EventHandler<T> handler) {
		Optional<Submodel> sub = getSubmodel(submodelIdentifier);
		if ( sub.isPresent()) {
			getEventProcessor().registerHandler(reference, handler);
		}
		
	}

	@Override
	public Object executeOperation(String aasIdentifier, String submodelIdentifier, String path, Object parameter) {
		
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			Optional<Operation> operation = new SubmodelHelper(sub.get()).getSubmodelElementAt(path, Operation.class);
			if ( operation.isPresent() && InstanceOperation.class.isInstance(operation.get())) {
				InstanceOperation instance = (InstanceOperation)operation.get();
				return instance.invoke(parameter);
			}
		}
		// when element not found locally, try to invoke the operation remote! 
		return repository.invokeOperation(aasIdentifier, submodelIdentifier, path, parameter);
	}

	@Override
	public <T extends SubmodelElement> List<T> getSubmodelElements(String aasIdentifier, String submodelIdentifier, Reference semanticId, Class<T> clazz) {
		Optional<Submodel> sub = getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			return new EventElementCollector().collect(sub.get()).stream()
				.filter(new Predicate<SubmodelElement>() {

					@Override
					public boolean test(SubmodelElement t) {
						return t.getSemanticId().equals(semanticId);
					}
				})
				.map(new Function<SubmodelElement, T>() {

					@Override
					public T apply(SubmodelElement t) {
						return clazz.cast(t);
					}})
				.collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public ConnectorMessaging getEventProcessor() {
		return eventProcessor;
		
	}

	@Override
	public Optional<Referable> resolve(Reference reference) {
		if ( reference != null) {
			Iterator<Key> keyIterator = reference.getKeys().iterator();
			if ( keyIterator.hasNext()) {
				Key rootKey = keyIterator.next();
				KeyTypes keyType = rootKey.getType();
				switch(keyType) {
				case SUBMODEL:
					Optional<Submodel> keySub = getSubmodel(rootKey.getValue());
					if ( keySub.isPresent()) {
						if ( keyIterator.hasNext()) {
							return SubmodelUtils.resolveKeyPath(keySub.get(), keyIterator);
//							return new SubmodelHelper(keySub.get()).resolveKeyPath(keyIterator);
						}
						return Optional.of(keySub.get());
					}
					break;
				case CONCEPT_DESCRIPTION:
					Optional<ConceptDescription> cDesc = getConceptDescription(rootKey.getValue());
					if ( cDesc.isPresent()) {
						return Optional.of(cDesc.get());
					}
					break;
				case ASSET_ADMINISTRATION_SHELL:
					Optional<AssetAdministrationShell> aas = storage.findAssetAdministrationShellById(rootKey.getValue());
					if ( aas.isPresent()) {
						if (keyIterator.hasNext()) {
							Key submodelKey = keyIterator.next();
							Optional<Submodel> submodel = getSubmodel(rootKey.getValue(), submodelKey.getValue() );
							if ( submodel.isPresent()) {
								if ( keyIterator.hasNext()) {
									return SubmodelUtils.resolveKeyPath(submodel.get(), keyIterator);
//									return new SubmodelHelper(submodel.get()).resolveKeyPath(keyIterator);
								}
								return Optional.of(submodel.get());
							}
						}
						return Optional.of(aas.get());
					}
					break;
				case GLOBAL_REFERENCE:
					return Optional.empty();
				default:
					throw new IllegalArgumentException("Provided reference points to a non-identifiable element!");
				}
			}
		}
		
		return Optional.empty();
	}
	public <T> Optional<T> resolveValue(Reference reference, Class<T> type) {
		// use empty path! 
		return resolveValue(reference, "", type);
	}
	public <T> Optional<T> resolveValue(Reference reference, String path, Class<T> type) {
		Optional<Referable> element = resolve(reference);
		if (element.isPresent()) {
			Referable referable = element.get();
			if (SubmodelElement.class.isInstance(referable)) {
				Object elementValue = SubmodelUtils.getSubmodelElementValue(SubmodelElement.class.cast(referable), path );
				return Optional.of(objectMapper.convertValue(elementValue, type));
			}
		}
		return Optional.empty();
	}
	@Override
	public Optional<SubmodelElement> getSubmodelElement(String submodelIdentifier, String path) {
		Optional<Submodel> submodel = getSubmodel(submodelIdentifier);
		if ( submodel.isPresent()) {
			return new SubmodelHelper(submodel.get()).getSubmodelElementAt(path);
		}
		return Optional.empty();
	}

	@Override
	public Object getElementValue(String submodelIdentifier, String path) {
		Optional<Submodel> submodel = getSubmodel(submodelIdentifier);
		if ( submodel.isPresent()) {
			return new SubmodelHelper(submodel.get()).getValueAt(path);
		}
		return null;
	}
	@Override
	public <T>  T getElementValue(String submodelIdentifier, String path, Class<T> clazz) {
		Object value = getElementValue(submodelIdentifier, path);
		return objectMapper.convertValue(value, clazz);
	}

	@Override
	public Object getElementValue(Reference reference) {
		Optional<SubmodelElement> referenced = resolve(reference, SubmodelElement.class);
		if ( referenced.isPresent() ) {
			return ValueHelper.toValue(referenced.get());
		}
		return null;
	}
	public <T> T getElementValue(Reference reference, Class<T> clazz) {
		Object value = getElementValue(reference);
		return objectMapper.convertValue(value, clazz);
	}

	@Override
	public Optional<Referable> getSubmodelElement(Reference reference) {
		return resolve(reference);
	}

}

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
import java.util.Optional;
import java.util.Set;
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
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.config.MarshallingFeature;
import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.connector.component.impl.HttpComponent;
import at.srfg.iasset.connector.component.impl.RepositoryConnection;
import at.srfg.iasset.connector.component.impl.event.EventProcessorImpl;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationRepositoryController;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationShellController;
import at.srfg.iasset.repository.component.ModelChangeProvider;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.Persistence;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASJacksonMapperProvider;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.event.EventHandler;
import at.srfg.iasset.repository.event.EventProcessor;
import at.srfg.iasset.repository.event.EventProducer;
import at.srfg.iasset.repository.model.InMemoryStorage;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.custom.InstanceProperty;
import at.srfg.iasset.repository.model.helper.SubmodelHelper;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.visitor.EventElementCollector;
import at.srfg.iasset.repository.model.helper.visitor.ReferenceCollector;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class LocalServiceEnvironment implements ServiceEnvironment, LocalEnvironment {
//	private InstanceEnvironment environment;
	private Persistence storage;
	
//	private final Collection<ModelListener> listeners = new HashSet<ModelListener>();
	private final ObjectMapper objectMapper;
	private final Set<String> registrations = new HashSet<String>();
	private final URI repositoryURI;
	private final RepositoryConnection repository;
	private ConnectorEndpoint httpEndpoint;
	private EventProcessorImpl eventProcessor;
	
	private ModelChangeProvider changeProvider = ModelChangeProvider.getProvider();

	
	public LocalServiceEnvironment(URI repositoryURI) {
		this.repositoryURI = repositoryURI;
		this.repository = RepositoryConnection.getConnector(repositoryURI);
		// in the local service environment we may use this objectmapper
		this.objectMapper = ClientFactory.getObjectMapper();
		eventProcessor = new EventProcessorImpl(this.objectMapper, this);
		storage = new InMemoryStorage();
//		environment = new InstanceEnvironment(changeProvider);
		// The model update listener is currently not working!
		changeProvider.addListener(new ModelListener() {
			

			
			@Override
			public void propertyRemoved(String path, Property property) {
				System.out.println("Element removed: "+ path);
				
			}
			
			@Override
			public void propertyCreated(String path, Property property) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void operationRemoved(String path, Operation operation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void operationCreated(String path, Operation operation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void eventElementRemoved(String path, BasicEventElement eventElement) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void eventElementCreated(String path, BasicEventElement eventElement) {
				// tell the processor that a new event element has been added
				eventProcessor.registerEventElement(eventElement);
				
			}

			@Override
			public void submodelElementCreated(String path, SubmodelElement element) {
				System.out.println("Element created: " + path + " - " + element.getIdShort());
				
			}

			@Override
			public void dataElementChanged(String path, DataElement property) {
				System.out.println("Property changed: " + path + " - " + property.getIdShort());
				
			}
		});
		// TODO: REMOVE test data!
		setAssetAdministrationShell(AASFull.AAS_1.getId(), AASFull.AAS_1);
		setAssetAdministrationShell(AASFull.AAS_2.getId(), AASFull.AAS_2);
		setAssetAdministrationShell(AASFull.AAS_3.getId(), AASFull.AAS_3);
		setAssetAdministrationShell(AASFull.AAS_4.getId(), AASFull.AAS_4);
		setAssetAdministrationShell(AASFull.AAS_4.getId(), AASFull.AAS_4);
		setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_1.getId(), AASFull.SUBMODEL_1);
		setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_2.getId(), AASFull.SUBMODEL_2);
		setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_3.getId(), AASFull.SUBMODEL_3);
		setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_4.getId(), AASFull.SUBMODEL_4);
		setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_5.getId(), AASFull.SUBMODEL_5);
		setSubmodel(AASFull.AAS_1.getId(), AASFull.SUBMODEL_6.getId(), AASFull.SUBMODEL_6);
//		environment.setSubmodel(AASFull.SUBMODEL_7.getId(), AASFull.SUBMODEL_7);
		setConceptDescription(AASFull.CONCEPT_DESCRIPTION_1.getId(), AASFull.CONCEPT_DESCRIPTION_1);
		setConceptDescription(AASFull.CONCEPT_DESCRIPTION_2.getId(), AASFull.CONCEPT_DESCRIPTION_2);
		setConceptDescription(AASFull.CONCEPT_DESCRIPTION_3.getId(), AASFull.CONCEPT_DESCRIPTION_3);
		setConceptDescription(AASFull.CONCEPT_DESCRIPTION_4.getId(), AASFull.CONCEPT_DESCRIPTION_4);
		// 
		setAssetAdministrationShell(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.AAS_BELT_TEMPLATE);
		setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_TEMPLATE);
		setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_EVENT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_EVENT_TEMPLATE);
		setSubmodel(AASFull.AAS_BELT_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_TEMPLATE);
		// belt instance data
		setAssetAdministrationShell(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.AAS_BELT_INSTANCE);
		setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE);
		setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE);
		setSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE);
		
		Set<Reference> references = new ReferenceCollector(this, KeyTypes.BASIC_EVENT_ELEMENT).collect(AASFull.AAS_BELT_INSTANCE);
		references.size();
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
		if ( ReferenceUtils.extractReferenceFromList(shell.get().getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL).isPresent() ) {
			return storage.findSubmodelById(submodelIdentifier);
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
					return Optional.of(storage.persist(fromRemote.get()));
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
			notifyDeletion(toDelete.get());
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
					new EventElementCollector().collect(t).forEach(new Consumer<BasicEventElement>() {

						@Override
						public void accept(BasicEventElement t) {
							changeProvider.eventElementRemoved(t);
						}});
					
					
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
			
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Optional<SubmodelElement> oldElement = helper.removeSubmodelElementAt(idShortPath);
			oldElement.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					changeProvider.elementRemoved(t);
					
				}
			});

			Optional<SubmodelElement> added = helper.setSubmodelElementAt(idShortPath, body);
			added.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					changeProvider.elementCreated(t);
					
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
					notifyDeletion(t);
				}
			});
			submodel.setId(submodelIdentifier);
			Submodel stored = storage.persist(submodel);
			notifyCreation(stored);
		}

		return null;
	}

	@Override
	public Optional<Referable> getSubmodelElement(String aasIdentifier, Reference element) {
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
				changeProvider.elementChanged(element.get());
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
	
	public void unregister() {
		registrations.forEach(new Consumer<String>() {

			@Override
			public void accept(String aasIdentifier) {
				repository.unregister(aasIdentifier);
			}
		});
		registrations.clear();
	}
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
	private <T extends Referable> void notifyDeletion(T deletedElement) {
		new EventElementCollector().collect(deletedElement).forEach(new Consumer<BasicEventElement>() {

			@Override
			public void accept(BasicEventElement t) {
				changeProvider.eventElementRemoved(t);
			}
		});


	}
	private <T extends Referable> void notifyCreation(T createdElement) {
		new EventElementCollector().collect(createdElement).forEach(new Consumer<BasicEventElement>() {

			@Override
			public void accept(BasicEventElement t) {
				changeProvider.eventElementAdded(t);
			}
		});


	}

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
	public Object executeOperaton(String aasIdentifier, String submodelIdentifier, String path, Object parameter) {
		
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

	public EventProcessor getEventProcessor() {
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
							return new SubmodelHelper(keySub.get()).resolveKeyPath(keyIterator);
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
									return new SubmodelHelper(submodel.get()).resolveKeyPath(keyIterator);
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

}

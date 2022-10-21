package at.srfg.iasset.connector.environment;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.RelationshipElement;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.connector.MessageListener;
import at.srfg.iasset.connector.MessageProducer;
import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.config.MarshallingFeature;
import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.connector.component.impl.HttpComponent;
import at.srfg.iasset.connector.component.impl.RepositoryConnection;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationRepositoryController;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationShellController;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASJacksonMapperProvider;
import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.custom.InstanceEnvironment;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.custom.InstanceProperty;
import at.srfg.iasset.repository.model.helper.SubmodelHelper;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class LocalServiceEnvironment implements ServiceEnvironment, LocalEnvironment {
	private InstanceEnvironment environment;
	
	private final Collection<ModelListener> listeners = new HashSet<ModelListener>();
	
	private final Set<String> registrations = new HashSet<String>();
	private final URI repositoryURI;
	private final RepositoryConnection repository;
	private ConnectorEndpoint httpEndpoint;

	
	public LocalServiceEnvironment(URI repositoryURI) {
		this.repositoryURI = repositoryURI;
		this.repository = RepositoryConnection.getConnector(repositoryURI);
		
		environment = new InstanceEnvironment();
		// TODO: REMOVE test data!
		environment.addAssetAdministrationShell(AASFull.AAS_1.getId(), AASFull.AAS_1);
		environment.addAssetAdministrationShell(AASFull.AAS_2.getId(), AASFull.AAS_2);
		environment.addAssetAdministrationShell(AASFull.AAS_3.getId(), AASFull.AAS_3);
		environment.addAssetAdministrationShell(AASFull.AAS_4.getId(), AASFull.AAS_4);
		environment.addSubmodel(AASFull.SUBMODEL_1.getId(), AASFull.SUBMODEL_1);
		environment.addSubmodel(AASFull.SUBMODEL_2.getId(), AASFull.SUBMODEL_2);
		environment.addSubmodel(AASFull.SUBMODEL_3.getId(), AASFull.SUBMODEL_3);
		environment.addSubmodel(AASFull.SUBMODEL_4.getId(), AASFull.SUBMODEL_4);
		environment.addSubmodel(AASFull.SUBMODEL_5.getId(), AASFull.SUBMODEL_5);
		environment.addSubmodel(AASFull.SUBMODEL_6.getId(), AASFull.SUBMODEL_6);
		environment.addSubmodel(AASFull.SUBMODEL_7.getId(), AASFull.SUBMODEL_7);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_1);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_2);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_3);
		environment.addConceptDescription(AASFull.CONCEPT_DESCRIPTION_4);
		listeners.add(new ModelListener() {
			
			@Override
			public void propertyValueChanged(String path, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void propertyRemoved(String path) {
				System.out.println("Element removed: "+ path);
				
			}
			
			@Override
			public void propertyCreated(String path, Property property) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void operationRemoved(String path) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void operationCreated(String path, Operation operation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void eventElementRemoved(String path) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void eventElementCreated(String path, EventElement eventElement) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void submodelElementCreated(String path, SubmodelElement element) {
				System.out.println("Element created: " + path + " - " + element.getIdShort());
				
			}
		});
	}
	
	public void addModelListener(ModelListener listener) {
		listeners.add(listener);
	}
	public void removeModelListener(ModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		return environment.getSubmodel(aasIdentifier, submodelIdentifier);
	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier) {
		return environment.getAssetAdministrationShell(identifier);
	}

	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier, AssetAdministrationShell theShell) {
		environment.addAssetAdministrationShell(aasIdentifier, theShell);
		return theShell;
	}

	@Override
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		Optional<ConceptDescription>  cDesc = environment.getConceptDescription(identifier);
		return cDesc.or(new Supplier<Optional<ConceptDescription>>() {

			@Override
			public Optional<ConceptDescription> get() {
				
				return Optional.empty();
			}});
	}

	@Override
	public boolean deleteAssetAdministrationShellById(String identifier) {
		return environment.deleteAssetAdministrationShell(identifier);
	}

	@Override
	public boolean deleteSubmodelReference(String aasIdentifier, Reference ref) {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public <T extends Referable> Optional<T> resolve(Reference reference, Class<T> type) {
		throw new UnsupportedOperationException("Not yet implemented!");
	}

	@Override
	public List<AssetAdministrationShell> getAllAssetAdministrationShells() {
		return environment.getAssetAdministrationShells();
	}

	@Override
	public boolean deleteSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Optional<SubmodelElement> deleted = helper.removeSubmodelElementAt(path);
			deleted.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					submodelElementRemoved(aasIdentifier, submodelIdentifier, path, t);
					
				}
			});
			return deleted.isPresent();
		}
		return false;
	}

	@Override
	public SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, String idShortPath,
			SubmodelElement body) {
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			
			SubmodelHelper helper = new SubmodelHelper(submodel.get());
			Optional<SubmodelElement> oldElement = helper.removeSubmodelElementAt(idShortPath);
			oldElement.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					submodelElementRemoved(aasIdentifier, submodelIdentifier, idShortPath, t);
					
				}
			});

			Optional<SubmodelElement> added = helper.setSubmodelElementAt(idShortPath, body);
			added.ifPresent(new Consumer<SubmodelElement>() {

				@Override
				public void accept(SubmodelElement t) {
					submodelElementAdded(aasIdentifier, submodelIdentifier, idShortPath, t);
					
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
		Optional<Submodel> submodel = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( submodel.isPresent()) {
			return new SubmodelHelper(submodel.get()).getSubmodelElementAt(path);
		}
		return Optional.empty();
	}

	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel) {
		Optional<AssetAdministrationShell> shell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> ref = ReferenceUtils.getReference(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if (ref.isEmpty()) {
				Reference newRef = AasUtils.toReference(submodel);
				theShell.getSubmodels().add(newRef);
			}
			environment.addSubmodel(submodelIdentifier, submodel);
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
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent() ) {
			return new SubmodelHelper(sub.get()).getValueAt(path);
		}

		return new HashMap<String, Object>();
	}

	@Override
	public void setElementValue(String aasIdentifier, String submodelIdentifier, String path, Object value) {
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent() ) {
			// make a json node out of it
			JsonNode node = ClientFactory.getObjectMapper().valueToTree(value);
			new SubmodelHelper(sub.get()).setValueAt(path, node);
		}
		
	}

	@Override
	public ConceptDescription setConceptDescription(String cdIdentifier, ConceptDescription conceptDescription) {
		// TODO Auto-generated method stub
		return null;
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
		Optional<AssetAdministrationShell> shell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> ref = ReferenceUtils.getReference(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
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
	public Map<String, Object> invokeOperation(String id, String base64Decode, String path,
			Map<String, Object> parameterMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void submodelElementRemoved(String aasIdentifier, String submodelIdentifier, String path, SubmodelElement element) {
		if ( Property.class.isInstance(element) || RelationshipElement.class.isInstance(element)) {
			listeners.forEach(new Consumer<ModelListener>() {

				@Override
				public void accept(ModelListener t) {
					t.propertyRemoved(path);
					
				}
			});
		}
		
	}
	private void submodelElementAdded(String aasIdentifier, String submodelIdentifier, String path, SubmodelElement element) {
		if ( Property.class.isInstance(element) || RelationshipElement.class.isInstance(element)) {
			listeners.forEach(new Consumer<ModelListener>() {

				@Override
				public void accept(ModelListener t) {
					t.submodelElementCreated(path, element);
					
				}
			});
		}
	}

	@Override
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path, Consumer<String> consumer) {
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
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
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
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
			Function<Map<String,Object>, Object> operation) {
		Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
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

		Optional<AssetAdministrationShell> shellToRegister = environment.getAssetAdministrationShell(aasIdentifier);
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
		Optional<AssetAdministrationShell> theShell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( theShell.isPresent()) {
			httpEndpoint.addHttpHandler(theShell.get().getIdShort(), getShellConfig(theShell.get()));
		}
		
	}

	@Override
	public void addHandler(String aasIdentifier, String alias) {
		Optional<AssetAdministrationShell> theShell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( theShell.isPresent()) {
			httpEndpoint.addHttpHandler(alias, getShellConfig(theShell.get()));
		}
		
	}
	@Override
	public void removeHandler(String alias) {
		Optional<AssetAdministrationShell> theShell = environment.getAssetAdministrationShell(alias);
		if ( theShell.isPresent()) {
			httpEndpoint.removeHttpHandler(theShell.get().getIdShort());
		}
		// 
		httpEndpoint.removeHttpHandler(alias);
		
	}

	@Override
	public <T> void addMesssageListener(Reference reference, MessageListener<T> listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> MessageProducer<T> getMessageProducer(Reference reference, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}


}

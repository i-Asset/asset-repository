package at.srfg.iasset.connector.environment;

import at.srfg.iasset.connector.api.ValueConsumer;
import at.srfg.iasset.connector.api.ValueSupplier;
import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.exception.ShellNotFoundException;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.custom.InstanceProperty;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.helper.value.type.Value;
import at.srfg.iasset.repository.model.helper.visitor.SemanticIdCollector;
import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.digitaltwin.aas4j.v3.model.*;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationScoped
public class LocalEnvironmentCDI implements LocalEnvironment {


	private static final Logger log = LoggerFactory.getLogger(LocalEnvironmentCDI.class);
	@Inject
	private ConnectorEndpoint endpoint;
	
	@Inject 
	private ConnectorMessaging messaging;

	@Inject
	private ServiceEnvironment serviceEnvironment;

	@Inject
	private RDFEnvironment rdfModel;

	private final Set<String> registeredAssetIdentifier = new HashSet<>();

	@Inject @Any
	private Instance<AASEnvironment> aasData;

	@Override
	public ServiceEnvironment getServiceEnvironment() {
		return serviceEnvironment;
	}

	@PostConstruct
	protected void postConstruct() {
		// 
		Environment coll = new DefaultEnvironment.Builder().build();
		for (AASEnvironment data : aasData) {
			log.info("creating environment for data {}", data.getClass().getName());
			Environment env = data.getAASData();
			if ( data != null) {
				log.info("adding Shells");
				coll.getAssetAdministrationShells().addAll(env.getAssetAdministrationShells());
				log.info("adding Submodels");
				coll.getSubmodels().addAll(env.getSubmodels());
				log.info("adding Concept Descriptions");
				coll.getConceptDescriptions().addAll(env.getConceptDescriptions());
			}
			log.info("adding model to RDF Model");
			rdfModel.addModel(data.getRDFData());
		}
		// store
		log.info("storing environment");
		serviceEnvironment.setEnvironment(coll);
		
	}
	
	@PreDestroy
	protected void destroyLocalEnvironment() {
		List<String> work = new ArrayList<>(registeredAssetIdentifier);
		work.forEach(new Consumer<String>() {

			@Override
			public void accept(String t) {
				unregisterAssetAdministrationShell(t);
				
			}
		});
		endpoint.stop();
		messaging.stopEventProcessing();
	}
	@Override
	public ConnectorEndpoint startEndpoint(int port) {
		endpoint.start(port);
		// do not expose the endpoint object
		return null;
	}

	@Override
	public void startEndpoint() {
		// start endpoint with default settings
		endpoint.start();

	}
	public ConnectorEndpoint getEndpoint() {
		return endpoint; 
	}

	@Override
	public void addAdministrationShell(AssetAdministrationShell shell) {
		serviceEnvironment.setAssetAdministrationShell(shell.getId(), shell);

	}
	

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier) {
		return serviceEnvironment.getAssetAdministrationShell(identifier);
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		return serviceEnvironment.getSubmodel(aasIdentifier, submodelIdentifier);
	}

	@Override
	public Optional<Submodel> getSubmodel(String submodelIdentifier) {
		return serviceEnvironment.getSubmodel(submodelIdentifier);
	}

	@Override
	public void addSubmodel(String aasIdentifer, Submodel submodel) throws ShellNotFoundException {
		serviceEnvironment.setSubmodel(aasIdentifer, submodel.getId(), submodel);

	}

	@Override
	public Optional<Referable> resolveReference(Reference patternReference) {
		return serviceEnvironment.resolve(patternReference);
	}
	@Override
	public <T extends SubmodelElement> Optional<T> resolveElementReference(Reference patternReference, Class<T> clazz) {
		return serviceEnvironment.resolve(patternReference, clazz);
	}
	@Override
	public void shutdownEndpoint() {
		if ( endpoint.isStarted()) {
			endpoint.stop();
		}
	}


	@Override
	public void addHandler(String aasIdentifier) {
		serviceEnvironment.getAssetAdministrationShell(aasIdentifier).ifPresent(new Consumer<AssetAdministrationShell>() {

			@Override
			public void accept(AssetAdministrationShell t) {
				endpoint.startAlias(ApiUtils.base64Encode(aasIdentifier), t);
			}
		});

	}

	@Override
	public void addHandler(String aasIdentifier, String alias) {
		serviceEnvironment.getAssetAdministrationShell(aasIdentifier).ifPresent(new Consumer<AssetAdministrationShell>() {

			@Override
			public void accept(AssetAdministrationShell t) {
				endpoint.startAlias(alias, t);
				
			}
		});

	}

	@Override
	public void removeHandler(String alias) {
		// remove the alias name
		endpoint.removeHttpHandler(alias);

	}


	@Override
	public <T> EventProducer<T> getEventProducer(String semanticId, Class<T> clazz) {
		return messaging.getProducer(semanticId, clazz);
	}

	@Override
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path,
			Function<Object, Object> function) {
		// TODO Auto-generated method stub

	}



//	@Override
//	public Object executeOperation(String aasIdentifier, String submodelIdentifier, String path, Object parameter) {
//		
//		Optional<Submodel> sub = serviceEnvironment.getSubmodel(aasIdentifier, submodelIdentifier);
//		if ( sub.isPresent()) {
//			Optional<Operation> operation = SubmodelUtils.getSubmodelElementAt(sub.get(), path, Operation.class);
//			if ( operation.isPresent() && InstanceOperation.class.isInstance(operation.get())) {
//				InstanceOperation instance = (InstanceOperation)operation.get();
//				return instance.callback().execute(parameter);
//			}
//		}
//		return null;
//		// when element not found locally, try to invoke the operation remote!
//		// TODO: check where to inject the repo-connector
////		return repository.invokeOperation(aasIdentifier, submodelIdentifier, path, parameter);
//	}
//
//	@Override
//	public ConnectorMessaging getEventProcessor() {
//		return messaging;
//	}

	@Override
	public <T> void registerEventHandler(EventHandler<T> clazz, String semanticId, String topic, String... references) throws MessagingException {
		if ( references != null && references.length > 0) {
			List<Reference> additionalRefs = 
			Arrays.asList(references).stream().map(new Function<String, Reference>() {

				@Override
				public Reference apply(String t) {
					return ReferenceUtils.asGlobalReference(t);
				}
			})
			.collect(Collectors.toList());
			// c
			Reference[] addRefArray = additionalRefs.toArray(new Reference[0]);
			messaging.registerHandler(clazz, ReferenceUtils.asGlobalReference(semanticId), topic, addRefArray);
		}
		else {
			messaging.registerHandler(clazz, ReferenceUtils.asGlobalReference(semanticId), topic, (Reference)null);
		}
		
	}

	@Override
	public <T> void registerEventHandler(EventHandler<T> clazz, String semanticId, String... references) throws MessagingException {
		messaging.registerHandler(clazz, semanticId, references);
	}
	@Override
	public <T> void registerValueCallback(String aasIdentifier, String submodelIdentifier, String path,
			ValueConsumer<T> consumer) {
		Optional<Submodel> sub = serviceEnvironment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			Optional<Property> property = SubmodelUtils.getSubmodelElementAt(sub.get(), path, Property.class);
			if ( property.isPresent()) {
				Property theProp = property.get();
				
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceProperty(theProp);
					SubmodelUtils.setSubmodelElementAt(sub.get(),path, theProp);
				}
				if ( InstanceProperty.class.isInstance(theProp)) {
					InstanceProperty iProp = InstanceProperty.class.cast(theProp);
					//
					ParameterizedType t = (ParameterizedType) consumer.getClass().getGenericInterfaces()[0];
					final Type p = t.getActualTypeArguments()[0];
					

					iProp.consumer(new Consumer<String>() {

						@Override
						public void accept(String t) {
							// perform type safe conversion
							try {
								consumer.accept(Value.toValue(p, t));
							} catch (ValueMappingException e) {
								// silently exit
								// new value is not stored!
							}
						}
					});
				}
				
			}
		}
	}
	@Override
	public <T> void registerValueCallback(String aasIdentifier, String submodelIdentifier, String path,
			ValueSupplier<T> supplier) {
		Optional<Submodel> sub = serviceEnvironment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			Optional<Property> property = SubmodelUtils.getSubmodelElementAt(sub.get(), path, Property.class);
			if ( property.isPresent()) {
				Property theProp = property.get();
				
				if (! InstanceProperty.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceProperty(theProp);
					SubmodelUtils.setSubmodelElementAt(sub.get(),path, theProp);
				}
				if ( InstanceProperty.class.isInstance(theProp)) {
					InstanceProperty iProp = InstanceProperty.class.cast(theProp);
					//
					ParameterizedType t = (ParameterizedType) supplier.getClass().getGenericInterfaces()[0];
					final Type p = t.getActualTypeArguments()[0];
					

					iProp.supplier(new Supplier<String>() {

						@Override
						public String get() {
							try {
								return Value.fromValue(supplier.get());
							} catch (ValueMappingException e) {
								return null;
							}
						}
					});
				}
				
			}
		}
	}

	@Override
	public void registerOperation(String aasIdentifier, String submodelIdentifier, String path,
			OperationCallback callback) {
		Optional<Submodel> sub = serviceEnvironment.getSubmodel(aasIdentifier, submodelIdentifier);
		if ( sub.isPresent()) {
			Optional<Operation> property = SubmodelUtils.getSubmodelElementAt(sub.get(), path, Operation.class);
			if ( property.isPresent()) {
				Operation theProp = property.get();
				
				if (! InstanceOperation.class.isInstance(theProp)) {
					// need to exchange the default property
					theProp = new InstanceOperation(theProp);
					SubmodelUtils.setSubmodelElementAt(sub.get(),path, theProp);
				}
				if ( InstanceOperation.class.isInstance(theProp)) {
					 InstanceOperation iProp = InstanceOperation.class.cast(theProp);
					
					iProp.callback(callback);				
				}
			}
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public <Input, Result> Result executeMethod(String aasIdentifier, String submodelIdentifier, String path,
			Input parameter) {
		// TODO Auto-generated method stub
		try {
			
			ParameterizedType t = (ParameterizedType) parameter.getClass().getGenericSuperclass();
			
			Method method = getClass().getMethod("executeMethod", String.class, String.class, String.class, Object.class);
			for (Type type : method.getGenericParameterTypes() ) {
				if ( type instanceof ParameterizedType) {
					Type[] theType = ((ParameterizedType) type).getActualTypeArguments();
					int i = theType.length;
				}
				if (type instanceof TypeVariable) {
					Type[] theType = ((TypeVariable) type).getBounds();
					int i = theType.length;
				}
			};
			Type returnType = method.getGenericReturnType();
			if ( returnType instanceof TypeVariable) {
				TypeVariable<?> pReturnType = (TypeVariable)returnType;
				GenericDeclaration g = pReturnType.getGenericDeclaration();
				g.getTypeParameters();
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Add a {@link ModelListener} to the local environment
	 * @param listener
	 */
	public void addModelListener(ModelListener listener) {
		serviceEnvironment.addModelListener(listener);
	}
	/**
	 * Remove a {@link ModelListener} from the local environment
	 * @param listener
	 */
	public void removeModelListener(ModelListener listener) {
		serviceEnvironment.removeModelListener(listener);
	}
	@Override
	public OperationInvocation getOperationInvocation(String semanticId, String ... additional) throws OperationInvocationException {
		// search for the asset implementing a requested semantic id
		Optional<OperationInvocation> implementation = serviceEnvironment.getImplementation(semanticId, additional);
		if ( implementation.isPresent()) {
			return implementation.get();
		}
		// operation must not return null!
		
		throw new OperationInvocationException(String.format("Operation for [%s] serving %s not found!", semanticId, Arrays.asList(additional)) );
	}
	
	@Override
	public boolean loadIntegrationPattern(String patternIdentifier) {
		Reference pattern = new DefaultReference.Builder()
				.type(ReferenceTypes.MODEL_REFERENCE)
				.keys(new DefaultKey.Builder()
						.type(KeyTypes.SUBMODEL)
						.value(patternIdentifier)
						.build())
				.build();
		return loadIntegrationPattern(pattern);
	}
	@Override
	public boolean loadIntegrationPattern(Reference patternReference) {
		Optional<Submodel> pattern = serviceEnvironment.resolve(patternReference, Submodel.class);
		if ( pattern.isPresent()) {
			return true;
		}
		return false;
	}
	@Override
	public boolean loadIntegrationPattern(Submodel submodel) {
		
		serviceEnvironment.setSubmodel(submodel.getId(), submodel);
		return true;
	}
	@Override
	public <T> void setElementValue(String aasIdentifier, String submodelIdentifier, String path, T value) {
		serviceEnvironment.setElementValue(aasIdentifier, submodelIdentifier, path, value);
		
	}
	@Override
	public <T> T getElementValue(String aasIdentifier, String submodelIdentifier, String path, Class<T> value) {
		return serviceEnvironment.getElementValue(aasIdentifier, submodelIdentifier, path, value);
		
	}
	
	@Override
	public <T extends SubmodelElement> T getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path,
			Class<T> clazz) {
		Optional<SubmodelElement> element = serviceEnvironment.getSubmodelElement(aasIdentifier, submodelIdentifier, path);
		if ( element.isPresent() && clazz.isInstance(element.get())) {
			return clazz.cast(element.get()); 
		}
		return null;
	}

	@Override
	public <T extends SubmodelElement> void setSubmodelElement(String aasIdentifier, String submodelIdentifier,
			String path, T element) {
		// when no path provided, the element belongs to the submodel!
		if ( path == null || path.length() == 0 ) {
			serviceEnvironment.setSubmodelElement(aasIdentifier, submodelIdentifier, element);
		}
		else {
			serviceEnvironment.setSubmodelElement(aasIdentifier, submodelIdentifier, path, element);
		}
		
	}

	@Override
	public void registerAssetAdministrationShell(String aasIdentifier) {
		Optional<AssetAdministrationShell> optShell = serviceEnvironment.getAssetAdministrationShell(aasIdentifier);
		if ( optShell.isPresent()) {
			log.debug("creating descriptor for AAS id '{}'", aasIdentifier);
			createDescriptor(optShell.get());
			registeredAssetIdentifier.add(aasIdentifier);
		} else {
			log.warn("AssetAdministrationShell with identifier '{}' not found in service environment", aasIdentifier);
		}
		
	}
	public void unregisterAssetAdministrationShell(String aasIdentifier) {
		serviceEnvironment.unregisterAssetAdministrationShell(aasIdentifier);
		registeredAssetIdentifier.remove(aasIdentifier);
		
	}
	private void createDescriptor(AssetAdministrationShell theShell) {
		AssetAdministrationShellDescriptor descriptor = new DefaultAssetAdministrationShellDescriptor.Builder()
				.id(theShell.getId())
				.idShort(theShell.getIdShort())
				.displayName(theShell.getDisplayName())
				.description(theShell.getDescription())
				//
				.globalAssetId(theShell.getAssetInformation().getGlobalAssetId())
				.specificAssetIds(theShell.getAssetInformation().getSpecificAssetIds())
				.submodelDescriptors(createSubmodelDescriptor(theShell))
				.build();

		try {
			Endpoint endpoint1 = endpoint.getEndpoint();
			log.debug("setting endpoint1 '{}' to AAS '{}'", endpoint1.getProtocolInformation().getHref(), theShell.getId());
			Endpoint endpoint2 = endpoint.getEndpoint(theShell.getId());
			log.debug("setting endpoint2 '{}' to AAS '{}'", endpoint2.getProtocolInformation().getHref(), theShell.getId());
			descriptor.setEndpoints(List.of(
					endpoint1,
					// @TODO: decide for the endpoint ... could be available only with alias
					endpoint2
			));
		} catch (NullPointerException | IllegalStateException e) {
			log.warn("no endpoint for shell with id {}", theShell.getId());
		}

		serviceEnvironment.registerAssetAdministrationShell(descriptor);
	}
	private List<SubmodelDescriptor> createSubmodelDescriptor(AssetAdministrationShell theShell) {
		List<SubmodelDescriptor> descriptor = new ArrayList<>();
	
		for ( Reference subRef : serviceEnvironment.getSubmodelReferences(theShell.getId())) {
			Optional<Submodel> sub = serviceEnvironment.resolve(subRef, Submodel.class);
			if (sub.isPresent()) {
				SubmodelDescriptor subDescriptor = new DefaultSubmodelDescriptor.Builder()
						.id(sub.get().getId())
						.idShort(sub.get().getIdShort())
						.description(sub.get().getDescription())
						.displayName(sub.get().getDisplayName())
						.semanticId(sub.get().getSemanticId())
						.supplementalSemanticIds(supplementalSemantics(sub.get()))
						.build();

				try {
					subDescriptor.setEndpoints(List.of(
							endpoint.getEndpoint(theShell.getId(), sub.get().getId())
					));
				} catch (NullPointerException | IllegalStateException e) {
					log.warn("no endpoint for submodel with id {}", sub.get().getId());
				}
				
				descriptor.add(subDescriptor);
			}
		}
		return descriptor;
		
	}
	private List<Reference> supplementalSemantics(Submodel submodel) {
		return new SemanticIdCollector(submodel).findSemanticIdentifier(EventElement.class, Operation.class);
	}

}

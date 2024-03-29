package at.srfg.iasset.connector.environment;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;
import org.jboss.weld.exceptions.IllegalStateException;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.api.OperationInvocationHandler;
import at.srfg.iasset.connector.api.ValueConsumer;
import at.srfg.iasset.connector.api.ValueSupplier;
import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.custom.InstanceProperty;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;
import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LocalEnvironmentCDI implements LocalEnvironment {

	
	@Inject
	private ConnectorEndpoint endpoint;
	
	@Inject 
	private ConnectorMessaging messaging;
	
	@Inject 
	private ServiceEnvironment serviceEnvironment;
	
	@Inject
	private ObjectMapper objectMapper;

	
	@PreDestroy
	protected void destroyLocalEnvironment() {
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

	@Override
	public void addAdministrationShell(AssetAdministrationShell shell) {
		serviceEnvironment.setAssetAdministrationShell(shell.getId(), shell);

	}

	@Override
	public void addSubmodel(String aasIdentifer, Submodel submodel) {
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
		// TODO Auto-generated method stub

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
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path,
			Consumer<String> consumer) {

	}

	@Override
	public void setValueSupplier(String aasIdentifier, String submodelIdentifier, String path,
			Supplier<String> consumer) {
		// TODO Auto-generated method stub

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
		}
		messaging.registerHandler(clazz, ReferenceUtils.asGlobalReference(semanticId), topic, (Reference)null);
		
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
							consumer.accept(ValueType.toValue(p,  t));
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
							// perform type safe conversion
							return ValueType.fromValue(supplier.get()).toString();
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
	public OperationInvocationHandler getOperation(String semanticId) {
		Reference reference = ReferenceUtils.asGlobalReference(semanticId);
		//
		Optional<Operation> operation = serviceEnvironment.getSubmodelElement(reference, Operation.class);
		if ( operation.isPresent()) {
			// TODO: obtain the reference to the resolved operation ...
			// 
			return new OperationInvocationHandler(operation.get(), serviceEnvironment, objectMapper);
		}
		// operation must not return null!
		throw new IllegalStateException(String.format("Operation with semantic id %s not found!", semanticId) );
	}
	@Override
	public boolean loadIntegrationPattern(String patternIdentifier) {
		Reference pattern = new DefaultReference.Builder()
				.type(ReferenceTypes.MODEL_REFERENCE)
				.key(new DefaultKey.Builder()
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

}

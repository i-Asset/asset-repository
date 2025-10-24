package at.srfg.iasset.connector.environment;

import java.util.Optional;
import java.util.function.Function;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.EventElement;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.connector.api.ValueConsumer;
import at.srfg.iasset.connector.api.ValueSupplier;
import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.exception.ShellNotFoundException;
import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;

public interface LocalEnvironment {
	/**
	 * Create the HTTP(s) enpoint. The component starts an HTTP endpoint serving the 
	 * local {@link AssetAdministrationShell}s. 
	 * @param port The port where the service endpoint is to be provided
	 * @return
	 */
	@Deprecated
	public ConnectorEndpoint startEndpoint(int port);
	/**
	 * Start the HTTP REST endpoint
	 */
	public void startEndpoint();
	/**
	 * Convenience Method to access the HTTP endpoint
	 * @return
	 */
	public ConnectorEndpoint getEndpoint();
	/**
	 * Add a new {@link AssetAdministrationShell} to the local environment
	 * @param shell
	 */
	public void addAdministrationShell(AssetAdministrationShell shell);
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier);
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier);
	public Optional<Submodel> getSubmodel(String submodelIdentifier);
	/**
	 * Add a new {@link Submodel} to the local environment. The model is registered
	 * with the identified {@link AssetAdministrationShell} 
	 * @param aasIdentifer
	 * @param submodel
	 * @throws ShellNotFoundException 
	 */
	public void addSubmodel(String aasIdentifer, Submodel submodel) throws ShellNotFoundException;
	/**
	 * Resolve a pattern by it's identifier. The pattern is searched in 
	 * <ul>
	 * <li>The local environment
	 * <li>The remote repository
	 * </ul>
	 * 
	 */
	public boolean loadIntegrationPattern(String patternIdentifier);
	/**
	 * Register the referenced {@link Submodel} with the local environment. The submodel
	 * must be of {@link ModelingKind#TEMPLATE}!
	 * <p>
	 * The {@link Submodel} is not connected to any {@link AssetAdministrationShell} but
	 * may contain {@link EventElement} or {@link Operation} defining
	 * communication settings which together represent a <b>Semantic Integration Pattern</b>
	 * </p>
	 * @param submodel The Submodel
	 * @return
	 */
	public boolean loadIntegrationPattern(Reference patternReference);
	/**
	 * Register the provided {@link Submodel} with the local environment.
	 * The {@link Submodel} is not connected to any {@link AssetAdministrationShell} but
	 * may contain {@link EventElement} or {@link Operation} defining
	 * communication settings which together represent a <b>Semantic Integration Pattern</b>
	 * @param submodel The Submodel
	 * @return
	 */
	public boolean loadIntegrationPattern(Submodel submodel);
	/**
	 * Resolve a reference!
	 * @param patternReference
	 */
	public Optional<Referable> resolveReference(Reference patternReference);
	/**
	 * Resolve a reference when the reference points to the expected submodel element type
	 * @param patternReference
	 */
	public <T extends SubmodelElement> Optional<T> resolveElementReference(Reference patternReference, Class<T> clazz);
	/**
	 * Stop the HTTP(s) endpoint
	 */
	public void shutdownEndpoint();
	/**
	 * Add a {@link ModelListener} to the local environment
	 * @param listener
	 */
	public void addModelListener(ModelListener listener);
	/**
	 * Remove a {@link ModelListener} from the local environment
	 * @param listener
	 */
	public void removeModelListener(ModelListener listener);
	/**
	 * Add a dedicated service handler for the identified {@link AssetAdministrationShell}.
	 * The service hanaddHandlerdler is created with the {@link AssetAdministrationShell#getIdShort()}  
	 * @param aasIdentifier
	 */
	public void addHandler(String aasIdentifier);
	/**
	 * Add a dedicated service handler for the identified {@link AssetAdministrationShell}.
	 * The service handler is created with the provided alias name  
	 * @param aasIdentifier
	 * @param alias
	 */
	public void addHandler(String aasIdentifier, String alias);
	/**
	 * Remove the decicated service handler
	 * @param alias
	 */
	public void removeHandler(String alias);
//	/**
//	 * Obtain a message producer which is bound to the semantic reference provided. 
//	 * The type Parameter ensures, the payload can be mapped to the provided type.
//	 * @param <T>
//	 * @param reference The model reference to the {@link EventElement}
//	 * @param clazz The type of the payload
//	 * @return
//	 */
//	public <T> EventProducer<T> getMessageProducer(Reference referenceToEventElement, Class<T> clazz);
	/**
	 * Obtain a {@link EventProducer} which is bound to the semantic reference provided. 
	 * The type Parameter ensures, the payload can be mapped to the provided type.
	 * @param <T>
	 * @param reference The semantic reference of the {@link EventElement}
	 * @param clazz The type of the payload
	 * @return
	 */
	public <T> EventProducer<T> getEventProducer(String semanticId, Class<T> clazz);
	/**
	 * Inject a {@link Function} to the {@link Operation} in the local environment. 
	 * The {@link Function#apply(Object)} method is called whenever the {@link Operation} is invoked.
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param function
	 */
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path, Function<Object,Object> function);
//	/**
//	 * Execute the operation
//	 * @param aasIdentifier
//	 * @param submodelIdentifier
//	 * @param path
//	 * @param parameter
//	 * @return
//	 */
//	public Object executeOperation(String aasIdentifier, String submodelIdentifier, String path, Object parameter);
//	/**
//	 * Obtain the event processor.
//	 * @return
//	 */
//	ConnectorMessaging getEventProcessor();
	/**
	 * Register an eventHandler for messages matching all provided references
	 * @param <T>
	 * @param reference A list of references which must be provided by the message producer
	 * @param clazz
	 */
	<T> void registerEventHandler(EventHandler<T> clazz, String semanticId, String topic, String ...references ) throws MessagingException;
	/**
	 * Register an eventHandler for messages matching all provided references
	 * @param <T>
	 * @param reference A list of references which must be provided by the message producer
	 * @param clazz
	 */
	<T> void registerEventHandler(EventHandler<T> clazz, String semanticId, String ...references) throws MessagingException;
	/**
	 * Obtain the execution environment for the mehtod/functionality identified
	 * by the provided semantic id
	 * @param semanticId
	 * @return
	 */
	public OperationInvocation getOperationInvocation(String semanticId, String ... additional) throws OperationInvocationException;
	/**
	 * Register an {@link OperationCallback} method which is to be executed
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param function
	 */
	public void registerOperation(String aasIdentifier, String submodelIdentifier, String path,
			OperationCallback function);
	/**
	 * Register a type safe {@link ValueConsumer} function for {@link Property} element. The callback function is 
	 * invoked whenever a new value is provided for the property via REST services
	 * @param <T>
	 * @param aasIdentifier The id of the {@link AssetAdministrationShell}
	 * @param submodelIdentifier The id of the {@link Submodel}, must be part of the AAS!
	 * @param path The dot-separated path of the {@link Property} 
	 * @param consumer The {@link ValueConsumer} function implementing the {@link ValueConsumer#accept(Object)} method!
	 */
	<T> void registerValueCallback(String aasIdentifier, String submodelIdentifier, String path,
			ValueConsumer<T> consumer);
	/**
	 * Register a type safe {@link ValueSupplier} function for {@link Property} element. The callback function is 
	 * invoked whenever the REST endpoint is about to process the contents of the {@link Property}
	 * @param <T>
	 * @param aasIdentifier The id of the {@link AssetAdministrationShell}
	 * @param submodelIdentifier The id of the {@link Submodel}, must be part of the AAS!
	 * @param path The dot-separated path of the {@link Property} 
	 * @param consumer The {@link ValueSupplier} function implementing the {@link ValueSupplier#get()} method!
	 */
	<T> void registerValueCallback(String aasIdentifier, String submodelIdentifier, String path,
			ValueSupplier<T> supplier);
	public <Input, Result> Result executeMethod(String aasIdentifier, String submodelIdentifier, String path,
			Input parameter);
	public <T> void setElementValue(String aasIdentifier, String submodelIdentifier, String path, T value);
	/**
	 * Obtain the value of the requested element
	 * @param <T>
	 * @param aasIdentifier The {@link AssetAdministrationShell} 
	 * @param submodelIdentifier The submodel, the element is part of
	 * @param path Path to the element
	 * @param clazz The expected type of the value
	 * @return
	 */
	public <T> T getElementValue(String aasIdentifier, String submodelIdentifier, String path, Class<T> clazz);
	
	public <T extends SubmodelElement> T getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path, Class<T> clazz);
	
	/**
	 * Register the {@link AssetAdministrationShell} with the configured directory service
	 * @param aasIdentifier
	 */
	public void registerAssetAdministrationShell(String aasIdentifier);
	
	

}

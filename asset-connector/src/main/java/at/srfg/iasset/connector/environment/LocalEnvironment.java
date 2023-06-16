package at.srfg.iasset.connector.environment;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.component.ModelListener;

public interface LocalEnvironment {
	/**
	 * Create the HTTP(s) enpoint. The component starts an HTTP endpoint serving the 
	 * local {@link AssetAdministrationShell}s. 
	 * @param port The port where the service endpoint is to be provided
	 * @return
	 */
	public ConnectorEndpoint startEndpoint(int port);
	/**
	 * Add a new {@link AssetAdministrationShell} to the local environment
	 * @param shell
	 */
	public void addAdministrationShell(AssetAdministrationShell shell);
	/**
	 * Add a new {@link Submodel} to the local environment. The model is registered
	 * with the identified {@link AssetAdministrationShell} 
	 * @param aasIdentifer
	 * @param submodel
	 */
	public void addSubmodel(String aasIdentifer, Submodel submodel);
	/**
	 * Resolve a reference!
	 * @param patternReference
	 */
	public Optional<Referable> resolveReference(Reference patternReference);
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
	/**
	 * Obtain a message producer which is bound to the semantic reference provided. 
	 * The type Parameter ensures, the payload can be mapped to the provided type.
	 * @param <T>
	 * @param reference The model reference to the {@link EventElement}
	 * @param clazz The type of the payload
	 * @return
	 */
	public <T> EventProducer<T> getMessageProducer(Reference referenceToEventElement, Class<T> clazz);
	/**
	 * Inject a {@link Consumer} function to the local environment. This function's {@link Consumer#accept(Object)} 
	 * method is called whenever a new value for the identified element is provided
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param consumer
	 */
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path, Consumer<String> consumer);
	/**
	 * Inject a {@link Supplier} function to the local environment. This function's {@link Supplier#get()} method is
	 * called whenever element is serialized.
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param consumer
	 */
	public void setValueSupplier(String aasIdentifier, String submodelIdentifier, String path, Supplier<String> consumer);
	/**
	 * Inject a {@link Function} to the {@link Operation} in the local environment. 
	 * The {@link Function#apply(Object)} method is called whenever the {@link Operation} is invoked.
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param function
	 */
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path, Function<Object,Object> function);
	/**
	 * Execute the operation
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param parameter
	 * @return
	 */
	public Object executeOperation(String aasIdentifier, String submodelIdentifier, String path, Object parameter);
	/**
	 * Obtain the event processor.
	 * @return
	 */
	ConnectorMessaging getEventProcessor();
	/**
	 * Register an eventHandler for messages matching all provided references
	 * @param <T>
	 * @param reference A list of references which must be provided by the message producer
	 * @param clazz
	 */
	<T> void registerEventHandler(EventHandler<T> clazz, Reference ...references) throws MessagingException;
	
	

}

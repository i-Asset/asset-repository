package at.srfg.iasset.connector.environment;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.event.EventHandler;
import at.srfg.iasset.repository.event.EventProcessor;
import at.srfg.iasset.repository.event.EventProducer;

public interface LocalEnvironment {
	/**
	 * Create the HTTP(s) enpoint. The component starts an HTTP endpoint serving the 
	 * local {@link AssetAdministrationShell}s. 
	 * @param port The port where the service endpoint is to be provided
	 * @return
	 */
	public ConnectorEndpoint startEndpoint(int port);
	public void addAdministrationShell(AssetAdministrationShell shell);
	public void addSubmodel(String aasIdentifer, Submodel submodel);
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
	 * The service handler is created with the {@link AssetAdministrationShell#getIdShort()}  
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
	 * Add a message listener for a particular semanticId
	 * @param <T>
	 * @param reference
	 * @param listener
	 */
	public <T> void addMesssageListener(Reference reference, EventHandler<T> listener);
	
	/**
	 * Obtain a message producer 
	 * @param <T>
	 * @param reference
	 * @param clazz
	 * @return
	 */
	public <T> EventProducer<T> getMessageProducer(Reference reference, Class<T> clazz);
	public <T> EventProducer<T> getMessageProducer(String submodelIdentifier, Reference reference, Class<T> clazz);
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
	public Object executeOperaton(String aasIdentifier, String submodelIdentifier, String path, Object parameter);
	/**
	 * Obtain the event processor.
	 * @return
	 */
	EventProcessor getEventProcessor();
	
	

}

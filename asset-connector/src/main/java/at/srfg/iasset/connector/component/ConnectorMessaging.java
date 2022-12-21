package at.srfg.iasset.connector.component;

import org.eclipse.aas4j.v3.model.Reference;

import at.srfg.iasset.connector.component.event.EventHandler;
import at.srfg.iasset.connector.component.event.EventProducer;

/**
 * The EventProcessor is the mediator between the Asset/Application Connector
 * and the outer messaging infrastructure 
 * @author dglachs
 *
 */
public interface ConnectorMessaging {

	/**
	 * Registers an {@link EventHandler} with the messaging infrastructure.
	 * The {@link EventHandler#onEventMessage(org.eclipse.aas4j.v3.model.EventPayload, Object)}
	 * is invoked on every incoming message which is (somehow) connected with the provided
	 * identifier.
	 * @param <T> The payload type, specific to the working environment
	 * @param semanticReference The global reference identifier
	 * @param handler The respective {@link EventHandler} implementing the {@link EventHandler#onEventMessage(org.eclipse.aas4j.v3.model.EventPayload, Object)}
	 *                method
	 */
	<T> void registerHandler(String semanticReference, EventHandler<T> handler);
	/**
	 * Registers an {@link EventHandler} with the messaging infrastructure.
	 * The {@link EventHandler#onEventMessage(org.eclipse.aas4j.v3.model.EventPayload, Object)}
	 * is invoked on every incoming message which is (somehow) connected with the provided
	 * identifier.
	 * @param <T> The payload type, specific to the working environment
	 * @param semanticReference The global reference
	 * @param handler The respective {@link EventHandler} implementing the {@link EventHandler#onEventMessage(org.eclipse.aas4j.v3.model.EventPayload, Object)}
	 *                method
	 */
	<T> void registerHandler(Reference semanticReference, EventHandler<T> handler);
	/**
	 * Creates an {@link EventProducer} used to send typed data objects to the 
	 * outer messaging infrastructure.
	 * @param <T> The payload type, specific to the working environment
	 * @param semanticReference The global reference identifier to send the message to
	 * @param clazz The class type for the requested event producer
	 */
	<T> EventProducer<T> getProducer(String semanticReference, Class<T> clazz);
	/**
	 * Creates an {@link EventProducer} used to send typed data objects to the 
	 * outer messaging infrastructure.
	 * @param <T> The payload type, specific to the working environment
	 * @param semanticReference The global reference to send the message to
	 * @param clazz The class type for the requested event producer
	 */
	<T> EventProducer<T> getProducer(Reference semanticReference, Class<T> clazz);
	
	/**
	 * Start the messaging infrastructure, e.g. connects with the outer messaging
	 * infrastructure and subscribes to the required topics. 
	 * <p>
	 * This will also start producing of outgoing messages.  
	 * </p>
	 */
	void startEventProcessing();
	/**
	 * Stop the messaging infrastructure.
	 */
	void stopEventProcessing();
	
	
}

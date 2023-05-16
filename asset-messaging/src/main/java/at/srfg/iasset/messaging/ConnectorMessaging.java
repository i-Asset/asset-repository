package at.srfg.iasset.messaging;

import org.eclipse.aas4j.v3.model.Reference;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.impl.MessagingComponent;
import at.srfg.iasset.repository.component.ServiceEnvironment;

/**
 * The EventProcessor is the mediator between the Asset/Application Connector
 * and the outer messaging infrastructure.
 * 
 * <p>
 * This interface {@link ConnectorMessaging} defines the API methods for a connector
 * when communicating with configured messaging environments such as 
 * <ul>
 * <li>KAFKA
 * <li>MQTT
 * <li>AMQP
 * <li>others
 * </ul>
 * 
 * </p>
 * @author dglachs
 *
 */
public interface ConnectorMessaging {
	static ConnectorMessaging create(ObjectMapper objectMapper, ServiceEnvironment environment) {
		return new MessagingComponent(objectMapper, environment);
	}

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
	void removeEventElement(Reference elementRef);
	void registerEventElement(Reference elementRef);
	
	
}

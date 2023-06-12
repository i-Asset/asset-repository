package at.srfg.iasset.messaging;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Reference;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.exception.MessagingException;
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
	 * @param handler The respective {@link EventHandler} implementing the {@link EventHandler#onEventMessage(EventPayload, Object)}
	 *                method
	 * @param semanticReference The global reference identifier
	 * @see #registerHandler(EventHandler, Reference...)
	 */
	<T> void registerHandler(EventHandler<T> handler, String ... globalReferences) throws MessagingException;
	/**
	 * Registers an {@link EventHandler} with the messaging infrastructure.
	 * The {@link EventHandler#onEventMessage(org.eclipse.aas4j.v3.model.EventPayload, Object)}
	 * is invoked on every incoming message which is (somehow) connected with the provided
	 * identifier.
	 * @param <T> The payload type, specific to the working environment
	 * @param handler The respective {@link EventHandler} implementing the {@link EventHandler#onEventMessage(EventPayload, Object)}
	 *                method
	 * @param references Global References for matching incoming messages. Only events with <b>ALL</b> requested references 
	 *                   will be propagated to {@link EventHandler#onEventMessage(EventPayload, Object)}
	 */
	<T> void registerHandler(EventHandler<T> handler, Reference ... references) throws MessagingException;
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
	 * @param eventReference The model reference for the event element
	 * @param clazz The class type for the requested event producer
	 */
	<T> EventProducer<T> getProducer(Reference eventReference, Class<T> clazz);
	<T> EventProducer<T> getProducer(BasicEventElement eventElement, Class<T> clazz);
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
	/**
	 * Stop event handling for the event element denoted by the Reference
	 * @param elementRef
	 */
	void removeEventElement(Reference elementRef);
	/**
	 * Register an event element
	 * @param elementRef
	 */
	void registerEventElement(Reference elementRef);
	
	
}

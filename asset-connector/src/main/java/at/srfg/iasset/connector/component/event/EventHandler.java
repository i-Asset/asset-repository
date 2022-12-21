package at.srfg.iasset.connector.component.event;

import org.eclipse.aas4j.v3.model.EventPayload;

import at.srfg.iasset.connector.component.ConnectorMessaging;

/**
 * Typed {@link EventHandler} used to process incoming
 * messages. 
 * <p>
 * An {@link EventHandler} can be attached to the messaging infrastructure
 * with {@link ConnectorMessaging#registerHandler(String, EventHandler)} where 
 * the provided Reference is used to interlink the handler with the messages
 * </p>
 * @author dglachs
 *
 * @param <T>
 */
public interface EventHandler<T> {
	/**
	 * Callback method for event processing
	 * @param eventPayload {@link EventPayload} object 
	 * @param payload The (typed) event payload
	 */
	void onEventMessage(EventPayload eventPayload, T payload);
	/**
	 * Indicate the type of the expected payload for proper
	 * transformation of the incoming data to the desired typed payload object! 
	 * @return
	 */
	Class<T> getPayloadType();
}

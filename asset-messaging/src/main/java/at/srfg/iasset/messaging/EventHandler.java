package at.srfg.iasset.messaging;

import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Reference;


/**
 * Typed {@link EventHandler} used to process incoming
 * messages. 
 * <p>
 * An {@link EventHandler} can be attached to the messaging infrastructure
 * with {@link ConnectorMessaging#registerHandler(String, EventHandler)} where 
 * the provided Reference is used to interlink the handler with the correct topic
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
	/**
	 * Define the semanticId, the Handler is responsible for!
	 * @return
	 */
	Reference getSemanticId();
}

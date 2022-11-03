package at.srfg.iasset.repository.event;

import org.eclipse.aas4j.v3.model.EventPayload;

public interface EventHandler<T> {
	/**
	 * Callback method for event processing
	 * @param eventPayload {@link EventPayload} object 
	 * @param payload The (typed) event payload
	 */
	void onEventMessage(EventPayload eventPayload, T payload);
	Class<T> getPayloadType();
}

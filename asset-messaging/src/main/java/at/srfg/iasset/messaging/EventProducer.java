package at.srfg.iasset.messaging;

import org.eclipse.aas4j.v3.model.Reference;


/**
 * Interface for sending events to the outer messaging interface.
 * <p>
 * Use {@link ConnectorMessaging#getProducer(String, Class)} to obtain a working {@link EventProducer}.
 * 
 * The returned {@link EventProducer} is linked to the outer messaging infrastructure based on the 
 * provided {@link Reference}!
 * 
 * </p>
 * @author dglachs
 *
 * @param <T>
 * @see ConnectorMessaging#getProducer(String, Class)
 * @see ConnectorMessaging#getProducer(org.eclipse.aas4j.v3.model.Reference, Class)
 */
public interface EventProducer<T> {
	/**
	 * Send the provided (typed) payload object
	 * @param payload
	 */
	void sendEvent(T payload);
	/**
	 * Send the provided (typed) payload object and register for completion notification
	 * @param payload
	 * @param callback
	 */
	void sendEvent(T payload, Callback<T> callback);
	/**
	 * Explicitly stop the underlying messaging infrastructure
	 */
	void close();
}

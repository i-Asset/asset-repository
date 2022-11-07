package at.srfg.iasset.repository.event;

import org.eclipse.aas4j.v3.model.Reference;

/**
 * Interface for sending events to the outer messaging interface.
 * <p>
 * Use {@link EventProcessor#getProducer(String, Class)} to obtain a working {@link EventProducer}.
 * 
 * The returned {@link EventProducer} is linked to the outer messaging infrastructure based on the 
 * provided {@link Reference}!
 * 
 * </p>
 * @author dglachs
 *
 * @param <T>
 * @see EventProcessor#getProducer(String, Class)
 * @see EventProcessor#getProducer(org.eclipse.aas4j.v3.model.Reference, Class)
 */
public interface EventProducer<T> {
	/**
	 * Send the provided (typed) payload object
	 * @param payload
	 */
	void sendEvent(T payload);
}

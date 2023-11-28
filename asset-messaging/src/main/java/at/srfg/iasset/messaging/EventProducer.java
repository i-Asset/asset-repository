package at.srfg.iasset.messaging;

import org.eclipse.digitaltwin.aas4j.v3.model.Reference;

import at.srfg.iasset.messaging.exception.MessagingException;


/**
 * Interface for sending events to the outer messaging interface.
 * <p>
 * Use {@link ConnectorMessaging#getProducer(String, Class)} to obtain a working {@link EventProducer}.
 * 
 * The returned {@link EventProducer} is linked to the outer messaging infrastructure based on the 
 * provided {@link Reference}!
 * 
 * </p>
 * <p>
 * Each EventProducer is linked with a {@link EventElementHandler} helper object.
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
	 * @throws MessagingException 
	 */
	void sendEvent(T payload) throws MessagingException;
	/**
	 * Send the provided (typed) payload object, and pass along the 
	 * @param payload
	 * @param subjectId
	 */
	void sendEvent(T payload, Reference subjectId) throws MessagingException;
	<U> void sendEvent(T payload, String subjectId) throws MessagingException;

	/**
	 * Explicitly stop the underlying messaging infrastructure
	 */
	void close();
}

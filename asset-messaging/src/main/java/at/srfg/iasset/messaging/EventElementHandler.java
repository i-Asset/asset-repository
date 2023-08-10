package at.srfg.iasset.messaging;

import java.util.List;

import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;

import at.srfg.iasset.messaging.exception.MessagingException;


/**
 * Implementing class act as an active event extension bound to 
 * a distinct {@link EventElement}. 
 * <p>
 * The {@link EventElementHandler} manages/compares all the {@link Reference}s 
 * provided in the {@link EventPayload} data such as
 * <ul> 
 * <li>source - ModelReference to the {@link EventElement}
 * <li>sourceSemanticId - Reference to the semantics of the {@link EventElement}
 * <li>observed - ModelReference to the observed {@link Referable} 
 * <li>observedSemanticId - Reference to the semantics of the observed {@link Referable}
 * <li><b>subjectId</b> - Reference pointing to SubjectId
 * </ul>
 * </>
 * The EventElementHandler 
 * @author dglachs
 *
 */
public interface EventElementHandler {
	/**
	 * Check whether the actual element handler has all the references
	 * assigned. 
	 * @param references
	 * @return
	 */
	boolean handlesReferences(Reference ... references);
	/**
	 * Check whether the actual element handler has all the references
	 * assigned. 
	 * @param references The list of references
	 * @return
	 */
	boolean handlesReferences(List<Reference> references);
	/**
	 * Is the event handling currently active or not
	 * @return
	 */
	boolean isActive();
	/** 
	 * Helper function: Is the underlying event element receiving messages
	 * @return <code>true</code> when {@link Direction#IN}
	 */
	boolean isConsuming();
	boolean isDirection(Direction direction);
	/**
	 * Obtain a {@link EventProducer} from the {@link EventElementHandler}
	 * accepts the typed payload object for sending.
	 * 
	 * <p>
	 * The {@link EventProducer} wraps the provided payload object
	 * as {@link EventPayload} and delegates the message 
	 * to the external messaging environment.
	 * </p>
	 * @param <T>
	 * @param type The type of the payload object
	 * @return The typed {@link EventProducer}
	 */
	<T> EventProducer<T> getProducer(Class<T> type);
//	/**
//	 * Obtain a producer from the {@link EventElementHandler}!
//	 * <p>
//	 * The producer is bound to the messaging environment (broker)
//	 * and the message topic. 
//	 * @param <T>
//	 * @param payloadType The type of the payload object
//	 * @param reference
//	 * @return
//	 */
//	<T> EventProducer<T> getProducer(Class<T> payloadType, Reference ...reference);
	
	
	/**
	 * Registers an event handler to be notified when the payload matches all the provided 
	 * references!
	 * @param <T>
	 * @param handler
	 * @param reference
	 * @throws MessagingException 
	 */
	<T> void registerHandler(EventHandler<T> handler, Reference ... references) throws MessagingException;
	<T> void registerHandler(EventHandler<T> handler, String topic, Reference... references) throws MessagingException;
	<T> void removeHandler(EventHandler<T> handler) throws MessagingException;
	
	void sendEvent(Object eventPayload) throws MessagingException;
	void sendEvent(Object eventPayload, String subjectId) throws MessagingException;
	void sendEvent(Object eventPayload, Reference subjectId) throws MessagingException;
	/**
	 * Shutdown the event handling infrastructure
	 */
	void shutdown();
	
	

}

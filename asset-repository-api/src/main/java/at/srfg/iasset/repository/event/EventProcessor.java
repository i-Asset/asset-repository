package at.srfg.iasset.repository.event;

import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Reference;

/**
 * The EventProcessor connectos to the outer environment based on 
 * @author dglachs
 *
 */
public interface EventProcessor {

	
	<T> void registerHandler(String semanticReference, EventHandler<T> handler);
	<T> void registerHandler(Reference semanticReference, EventHandler<T> handler);
	<T> EventProducer<T> getProducer(String semanticReference, Class<T> clazz);
	<T> EventProducer<T> getProducer(Reference semanticReference, Class<T> clazz);
	
//	void sendTestEvent(String string, String semantiId, Object payload);
//	void sendTestEvent(EventPayload payload);
	void processIncomingMessage(String topic, String key, String message);
	
	void startEventProcessing();
	void stopEventProcessing();
	
	
}

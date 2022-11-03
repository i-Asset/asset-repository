package at.srfg.iasset.repository.event;

import org.eclipse.aas4j.v3.model.Reference;

/**
 * The EventProcessor connectos to the outer environment based on 
 * @author dglachs
 *
 */
public interface EventProcessor {

	
	<T> void registerHandler(String aasIdentifier, String submodelIdentifier, Reference semanticId, EventHandler<T> handler);
	<T> void registerHandler(String aasIdentifier, String submodelIdentifier, String semanticReference, EventHandler<T> handler);
	void sendTestEvent(String string, String semantiId, Object payload);
	void processIncomingMessage(String topic, String key, String message);
	
	
	
}

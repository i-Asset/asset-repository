package at.srfg.iasset.messaging.impl;

import org.eclipse.aas4j.v3.model.EventPayload;

import at.srfg.iasset.messaging.impl.helper.MessageBroker;

public interface EventHelper {
//	public EventPayload toEventPayload(Object payload);
	public byte[] toByteArray(EventPayload payload); 
	public EventPayload fromByteArray(byte[] payload);
	public <T> T fromMessaging(byte[] incoming, Class<T> clazz);
	public String getTopic();
	public MessageBroker getBroker();
	
}

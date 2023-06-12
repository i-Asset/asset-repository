package at.srfg.iasset.messaging.impl;

import at.srfg.iasset.messaging.exception.MessagingException;

/**
 * Interface for incoming message listeners, each message consumer
 * 
 * @author dglachs
 *
 */
public interface MessageConsumer {
	public void close();
	public void subscribe(String topic, MessageHandler handler) throws MessagingException;

}

package at.srfg.iasset.messaging.impl;

import at.srfg.iasset.messaging.exception.MessagingException;

/**
 * Accept the incoming message from the 
 * outer messaging infrastructure
 * @author dglachs
 *
 */
public interface MessageHandler {
	void acceptMessage(String topic, byte[] message) throws MessagingException;
	void acceptMessage(String topic, String message) throws MessagingException;
	

}

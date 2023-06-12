package at.srfg.iasset.messaging.impl;

import at.srfg.iasset.messaging.exception.MessagingException;

public interface MessageProducer {
	/**
	 * Send the message to the message broker
	 * @param topic
	 * @param message The message payload as byte array
	 */
	public void send(String topic, byte[] message) throws MessagingException;
	/**
	 * Close the connection to the message broker
	 */
	public void close();
}

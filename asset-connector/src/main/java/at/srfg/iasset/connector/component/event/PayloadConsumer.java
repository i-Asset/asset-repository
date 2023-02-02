package at.srfg.iasset.connector.component.event;

public interface PayloadConsumer {
	/**
	 * Process the message retrieved from the outer messaging infrastructure
	 * @param topic The topic the message was received from
	 * @param key
	 * @param message The message payload as string
	 */
	void processIncomingMessage(String topic, String key, String message);
	/**
	 * Stop listening for new incoming messages
	 */
	void stop();
	

}
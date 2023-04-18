package at.srfg.iasset.connector.component.event;

/**
 * Interface for connecting/listening to the outer messaging infrastructure.
 * 
 * <p>Implementors will be informed whenever a new message is available.
 * </p> 
 * @author dglachs
 *
 */
public interface PayloadConsumer {
	/**
	 * Process the message retrieved from the outer messaging infrastructure
	 * @param topic The topic the message was received from
	 * @param key
	 * @param message The message payload as string
	 */
	void processIncomingMessage(String topic, String key, String message);
	/**
	 * Stop listening for new incoming messages, e.g. removes the 
	 * listener from the environment!
	 */
	void stop();
	

}

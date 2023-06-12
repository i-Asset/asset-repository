package at.srfg.iasset.messaging;

/**
 * Interface for event consumer. The {@link EventConsumer} listens 
 * for incoming messages, thus is {@link Runnable}!
 * @author dglachs
 *
 */
public interface EventConsumer {
	/**
	 * Subscribe to the topic, The payload 
	 * @param topic
	 * @param consumer
	 */
	void subscribe(String topic);
	/**
	 * Close the consumer, e.g. stop the message listeners
	 */
	void close();

}

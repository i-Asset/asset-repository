package at.srfg.iasset.connector.component.impl.event;

/**
 * Interface for event consumer. The {@link EventConsumer} listens 
 * for incoming messages, thus is {@link Runnable}!
 * @author dglachs
 *
 */
public interface EventConsumer extends Runnable {
	/**
	 * Close the consumer, e.g. stop the message listeners
	 */
	void close();

}

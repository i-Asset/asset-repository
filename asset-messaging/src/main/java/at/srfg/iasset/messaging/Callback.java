package at.srfg.iasset.messaging;

/**
 * Interface to notify a sender, that a message has been delivered!
 * 
 * @author dglachs
 *
 * @param <T>
 */
public interface Callback<T> {
	/**
	 * Notify a sender that the payload object has been sucessfully delivered
	 * to the messaging environment. 
	 * @param payload The payload object passed to the environment
	 */
	void deliveryComplete(T payload);

}

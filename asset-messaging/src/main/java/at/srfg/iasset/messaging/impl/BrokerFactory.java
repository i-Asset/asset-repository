package at.srfg.iasset.messaging.impl;

import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;

public interface BrokerFactory {
	/**
	 * Create a {@link MessageConsumer} connected with the
	 * external messaging environment specified with {@link MessageBroker}.
	 *  
	 * @param broker Specifies the external messaging environment
	 * @param clientId Uniquely identifies the client with the external messaging infrastructure.
	 * @return
	 * @throws MessagingException
	 */
	MessageConsumer createConsumer(MessageBroker broker, String clientId) throws MessagingException;
	/**
	 * Create a {@link MessageProducer} connected with the 
	 * external messaging environment specified with {@link MessageBroker}.
	 * 
	 * @param broker Specifies the external messaging environment
	 * @param clientId Uniquely identifies the client with the external messaging infrastructure.
	 * @return 
	 * @throws MessagingException
	 */
	MessageProducer createProducer(MessageBroker broker, String clientId) throws MessagingException;
	/**
	 * Names the type of the underlying message broker infrastructure 
	 * @return
	 */
	MessageBroker.BrokerType getBrokerType();


		
//	}
}

package at.srfg.iasset.messaging.impl.kafka;

import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.BrokerFactory;
import at.srfg.iasset.messaging.impl.MessageConsumer;
import at.srfg.iasset.messaging.impl.MessageProducer;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;
import at.srfg.iasset.messaging.impl.helper.MessageBroker.BrokerType;
/**
 * Factory creating {@link MessageProducer} and {@link MessageBroker}
 * for {@link BrokerType#MQTT}
 * @author dglachs
 *
 */
public class KafkaFactory implements BrokerFactory {
	@Override
	public BrokerType getBrokerType() {
		return BrokerType.KAFKA;
	}

	@Override
	public MessageConsumer createConsumer(MessageBroker broker, String clientId) throws MessagingException {
		try {
			return new KafkaListener(broker.getHosts(), clientId);
		} catch (Exception e) {
			throw new MessagingException(e.getLocalizedMessage());
		}
	}
	public MessageProducer createProducer(MessageBroker broker, String clientId) throws MessagingException {
		try {
			return new KafkaSender(broker.getHosts(), clientId);
		} catch (Exception e) {
			throw new MessagingException(e.getLocalizedMessage());
		}
		
	}
}

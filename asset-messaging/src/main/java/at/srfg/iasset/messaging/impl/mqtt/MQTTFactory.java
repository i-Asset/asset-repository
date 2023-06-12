package at.srfg.iasset.messaging.impl.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

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
public class MQTTFactory implements BrokerFactory {
	@Override
	public BrokerType getBrokerType() {
		return BrokerType.MQTT;
	}

	@Override
	public MessageConsumer createConsumer(MessageBroker broker, String clientId) throws MessagingException {
		try {
			return new MQTTListener(broker.getHosts(), clientId);
		} catch (MqttException e) {
			throw new MessagingException(e.getLocalizedMessage());
		}
	}
	public MessageProducer createProducer(MessageBroker broker, String clientId) throws MessagingException {
		try {
			return new MQTTSender(broker.getHosts(), clientId);
		} catch (MqttException e) {
			throw new MessagingException(e.getLocalizedMessage());
		}
		
	}
}

package at.srfg.iasset.messaging.impl.mqtt;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;

import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.impl.BrokerFactory;
import at.srfg.iasset.messaging.impl.EventHelper;
import at.srfg.iasset.messaging.impl.helper.MessageBroker.BrokerType;

public class MqttFactory implements BrokerFactory {
	@Override
	public EventConsumer createConsumer(EventHelper helper) {
		// TODO Auto-generated method stub
		try {
			return new MqttConsumer(helper, UUID.randomUUID().toString());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public <T> EventProducer<T> getProducer(EventHelper payloadHelper, Class<T> type) {
		// TODO Auto-generated method stub
		try {
			return new MqttProducer<>(payloadHelper, UUID.randomUUID().toString());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public BrokerType getBrokerType() {
		return BrokerType.MQTT;
	}

}

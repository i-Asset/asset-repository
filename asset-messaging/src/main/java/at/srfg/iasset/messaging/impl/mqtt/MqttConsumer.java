package at.srfg.iasset.messaging.impl.mqtt;

import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.impl.EventHelper;
import at.srfg.iasset.messaging.impl.PayloadConsumer;

public class MqttConsumer implements EventConsumer {
	private final EventHelper eventHelper;
	private final IMqttClient client;
	private String topic;
	
	public MqttConsumer(EventHelper helper, String clientId) throws MqttException {
		this.eventHelper = helper;
		client = new MqttClient(helper.getBroker().getHosts(), clientId, new MemoryPersistence());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		client.connect(options);
	}

	@Override
	public void subscribe(String channelTopic) {
		try {
			client.subscribe(channelTopic, (topic, message) -> {
				// check whether to use byte[] instead of string
				eventHelper.acceptMessage(topic, message.getPayload());
//				eventHelper.processIncomingMessage(topic, new String(message.getPayload()));
			});
			this.topic = channelTopic;
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void close() {
		try {
			if ( topic != null) {
				client.unsubscribe(topic);
				topic = null;
			}
			if ( client.isConnected()) {
				client.disconnect();
			}
			client.close();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

}

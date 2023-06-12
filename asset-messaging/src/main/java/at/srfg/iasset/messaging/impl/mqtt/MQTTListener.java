package at.srfg.iasset.messaging.impl.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.MessageConsumer;
import at.srfg.iasset.messaging.impl.MessageHandler;

public class MQTTListener implements MessageConsumer {
	private final IMqttClient client;
	private String topic;
	
	public MQTTListener(String hosts, String clientId) throws MqttException {
		client = new MqttClient(hosts, clientId, new MemoryPersistence());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		client.connect(options);
	}

	@Override
	public void subscribe(String channelTopic, MessageHandler eventHelper) throws MessagingException {
		try {
			client.subscribe(channelTopic, (topic, message) -> {
				// check whether to use byte[] instead of string
				eventHelper.acceptMessage(topic, message.getPayload());
			});
			this.topic = channelTopic;
		} catch (MqttException e) {
			throw new MessagingException(e.getLocalizedMessage());
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

package at.srfg.iasset.messaging.impl.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.MessageProducer;

public class MQTTSender implements MessageProducer {
	private final IMqttClient client;
	
	public MQTTSender(String hosts, String clientId) throws MqttException {
		
		client = new MqttClient(hosts, clientId, new MemoryPersistence());
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		client.connect(options);
		
	}



	@Override
	public void close() {
		try {
			if ( client.isConnected()) {
				client.disconnect();
			}
			client.close();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Send payload as byte array to message broker
	 * @param payload
	 */
	public void send(String topic, byte[] payload) throws MessagingException {
		if ( client.isConnected()) {
			try {
				MqttMessage msg = new MqttMessage();
				msg.setQos(0);
				msg.setPayload(payload);
				client.publish(topic, msg);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}

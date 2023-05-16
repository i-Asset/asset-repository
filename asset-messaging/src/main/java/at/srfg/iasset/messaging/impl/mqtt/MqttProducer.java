package at.srfg.iasset.messaging.impl.mqtt;

import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.srfg.iasset.messaging.Callback;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.impl.EventHelper;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;

public class MqttProducer<T> implements EventProducer<T> {
	private final EventHelper payloadHelper;
	private final IMqttClient client;
	private final String topic;
	
	public MqttProducer(EventHelper helper, String clientId) throws MqttException {
		this.payloadHelper = helper;
		this.topic = helper.getTopic();
		
		client = new MqttClient(helper.getBroker().getHosts(), clientId, new MemoryPersistence());
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

	@Override
	public void sendEvent(T payload) {
		// 
		EventPayload eventPayload = payloadHelper.toEventPayload(payload);
		if ( client.isConnected()) {
			try {
				MqttMessage msg = new MqttMessage();
				msg.setQos(0);
				msg.setPayload(payloadHelper.toByteArray(eventPayload));
				client.publish(topic, msg);
			} catch (MqttException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void sendEvent(T payload, Callback<T> callback) {
		sendEvent(payload);
		
	}

}

package at.srfg.iasset.connector.component.impl.event.mqtt;

import java.awt.event.FocusEvent.Cause;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * A sample application that demonstrates how to use the Paho MQTT v3.1 Client blocking API.
 */
public class MQTTConsumer implements MqttCallback {

    private final int qos = 1;
    private String topic = "test";
    private MqttClient client;

    public MQTTConsumer(String uri) throws MqttException, URISyntaxException {
        this(new URI(uri));
    }

    public MQTTConsumer(URI uri) throws MqttException {
    	
        String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
        String[] auth = this.getAuth(uri);
        String username = auth[0];
        String password = auth[1];
        String clientId = "MQTT-Java-Example";
        if (!uri.getPath().isEmpty()) {
            this.topic = uri.getPath().substring(1);
        }

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
//        conOpt.set
//        conOpt.setUserName(username);
//        conOpt.setPassword(password.toCharArray());

        this.client = new MqttClient(host, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);

        this.client.subscribe(this.topic, qos);
    }

    private String[] getAuth(URI uri) {
        String a = uri.getAuthority();
        String[] first = a.split("@");
        return first[0].split(":");
    }

    public void sendMessage(String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        this.client.publish(this.topic, message); // Blocking publish
    }

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost because: " + cause);
        System.exit(1);
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        System.out.println(String.format("[%s] %s", topic, new String(message.getPayload())));
    }

    public static void main(String[] args) throws MqttException, URISyntaxException {
    	String publisherId = UUID.randomUUID().toString();
    	MqttClient consumer = new MqttClient("tcp://localhost:1883", publisherId+".consumer", new MemoryPersistence());
    	MqttClient publisher = new MqttClient("tcp://localhost:1883", publisherId+".producer", new MemoryPersistence());
    	
    	publisher.setCallback(new MqttCallback() {
			
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
		        System.out.println(String.format("Producer: [%s] %s", topic, new String(message.getPayload())));
				
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
		        System.out.println(String.format("[%s] %s", token.getClient().getClientId(), token.getClient().getServerURI()));
				
			}
			
			@Override
			public void connectionLost(Throwable cause) {
		        System.out.println(String.format("[Connection Lost] %s", Cause.values()));

				
			}
		});
    	
    	MqttConnectOptions options = new MqttConnectOptions();
    	options.setAutomaticReconnect(true);
    	options.setCleanSession(true);
    	options.setConnectionTimeout(10);
    	publisher.connect(options);
    	consumer.connect(options);
    	consumer.setCallback(new MqttCallback() {
			
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
		        System.out.println(String.format("Consumer: [%s] %s", topic, new String(message.getPayload())));
				
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
		        System.out.println(String.format("[%s] %s", token.getClient().getClientId(), token.getClient().getServerURI()));
				
			}
			
			@Override
			public void connectionLost(Throwable cause) {
		        System.out.println(String.format("[Connection Lost] %s", Cause.values()));

				
			}
		});
    	consumer.subscribe("at/srfg/ws/station/thing/distance/+", 0);
//    	publisher.subscribe("testTopic", 0);
//    	publisher.subscribe(publisherId, (topic, msg) -> {
//    		byte[] payload = msg.getPayload();
//    		System.out.println(topic + " " + new String(payload));
//    	});
    	
    	MqttMessage msg = new MqttMessage("this is my message".getBytes());
    	msg.setQos(0);
    	msg.setRetained(false);
    	publisher.publish("at/srfg/ws/station/thing/distance/int", msg);
    	publisher.publish("at/srfg/ws/station/thing/distance/ext", msg);
    	publisher.unsubscribe("at/srfg/ws/station/thing/distance/int");
    	publisher.disconnect();
    	publisher.close();
    	
//    	consumer.publish("testTopic", msg);
    	consumer.unsubscribe("at/srfg/ws/station/thing/distance/+");
    	consumer.disconnect();
    	consumer.close();

    }
}
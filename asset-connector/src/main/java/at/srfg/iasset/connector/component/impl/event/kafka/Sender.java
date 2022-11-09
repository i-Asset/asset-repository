package at.srfg.iasset.connector.component.impl.event.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import at.srfg.iasset.repository.connectivity.rest.ClientFactory;

/**
 * Producer sending the provided payload to the outer messaging infrastructure
 * @author dglachs
 *
 */
public final class Sender {
	private Logger logger = LoggerFactory.getLogger(Sender.class);
	private Producer<Long, String> producer;
	private String hosts;
	private final String topic;
	public Sender(String topic) {
		this(topic, "iasset.sensornet.salzburgresearch.at:9092");
	}
	public Sender(String topic, String hosts) {
		this.topic = topic;
		this.hosts = hosts;
		startProducer();
	}
	public String getTopic() {
		return topic;
	}
	public void startProducer() {
		producer = ProducerCreator.createProducer(topic, hosts);
	}
	
	public void sendMessage(String payload) {
		sendMessage(payload,null);
	}
	public void sendMessage(EventPayload eventPayload) {
		try {
			String payload = ClientFactory.getObjectMapper().writeValueAsString(eventPayload);
			sendMessage(payload);
		} catch (JsonProcessingException e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	public void sendMessage(String payload, Callback callback) {
		ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(topic, payload);
		try {
			if ( callback != null ) {
				producer.send(record,callback);
			}
			else {
				producer.send(record);
			}
		}
		finally {
			
		}
		
	}

	public void close() {
		if ( producer != null) {
			try {
				producer.close();
			} 
			finally {
				producer = null;
			}
			
		}
	}
}

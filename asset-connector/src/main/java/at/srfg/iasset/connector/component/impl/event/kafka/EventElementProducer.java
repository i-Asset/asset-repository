package at.srfg.iasset.connector.component.impl.event.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.aas4j.v3.model.BasicEventElement;

public class EventElementProducer {
	private Producer<Long, String> producer;
	private String hosts = "iasset.salzburgresearch.at:9092";
	private String topic;
	public EventElementProducer(BasicEventElement element) {
		this.topic = element.getIdShort();
	}
	public void startProducer() {
		producer = ProducerCreator.createProducer(topic, hosts);
	}
	
	public void sendMessage(String payload) {
		sendMessage(payload,null);
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
		try {
			producer.close();
		} 
		finally {
			producer = null;
		}
	}
}

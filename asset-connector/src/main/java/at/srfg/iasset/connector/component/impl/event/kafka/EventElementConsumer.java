package at.srfg.iasset.connector.component.impl.event.kafka;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.Uuid;
import org.apache.kafka.common.errors.WakeupException;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.srfg.iasset.connector.component.event.EventConsumer;
import at.srfg.iasset.connector.component.event.PayloadConsumer;

/**
 * Consumer for incoming messages. Registers for a given topic
 * @author dglachs
 *
 */
public class EventElementConsumer implements EventConsumer {
    private Logger logger = LoggerFactory.getLogger(BasicEventElement.class);
    private Consumer<Long, String> consumer;
    private String topics;
    private String hosts;
    // 
    private PayloadConsumer payloadConsumer;
    
    
    
    public EventElementConsumer(String topic, PayloadConsumer payloadConsumer)  {
    	this.topics = topic;
    	this.hosts = "iasset.sensornet.salzburgresearch.at:9092";
    	this.payloadConsumer = payloadConsumer;
    }
    

	@Override
	public void run() {
		// configure with hosts 
		consumer = ConsumerCreator.createConsumer(Uuid.randomUuid().toString(), topics, hosts);
		try {
			while (true) {
				ConsumerRecords<Long, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
				for (ConsumerRecord<Long, String> record : records) {
					Map<String, Object> data = new HashMap<>();
					data.put("partition", record.partition());
					data.put("offset", record.offset());
					data.put("value", record.value());
					logger.trace(data.toString());
					// notify the components messaging environment,that a new event message arrived!
					payloadConsumer.processIncomingMessage(record.topic(), "" + record.key(), record.value());
					consumer.commitAsync();
				}
			}
		} catch (WakeupException e) {
			logger.debug("Closing Consumer for topic:" + topics);
		} finally {
			consumer.close();
			logger.debug("Consumer closed for topic:" + topics);
		}
	}
    public void shutdown() {
    	if ( consumer!=null ) {
    		consumer.wakeup();
    	}
    }
    @Override
    public void close() {
    	shutdown();
    }

  

}

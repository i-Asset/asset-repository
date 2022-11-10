package at.srfg.iasset.connector.component.impl.event.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.srfg.iasset.connector.component.impl.event.EventProcessorImpl;
import at.srfg.iasset.repository.model.helper.EventPayloadHelper;

/**
 * Consumer for incoming messages. Registers for a given topic
 * @author dglachs
 *
 */
public class EventElementConsumer implements Runnable {
    private Logger logger = LoggerFactory.getLogger(BasicEventElement.class);
    private Consumer<Long, String> consumer;
    private String topics;
    private String hosts;
    // 
    private EventPayloadHelper payloadHelper;
    
    
    
    public EventElementConsumer(String topic, EventPayloadHelper payloadHelper)  {
    	this.topics = topic;
    	this.hosts = "iasset.sensornet.salzburgresearch.at:9092";
    	this.payloadHelper = payloadHelper;
    }
    

	@Override
	public void run() {
		// configure with hosts 
		consumer = ConsumerCreator.createConsumer(payloadHelper.getTopic(), topics, hosts);
		try {
			while (true) {
				ConsumerRecords<Long, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
				for (ConsumerRecord<Long, String> record : records) {
					Map<String, Object> data = new HashMap<>();
					data.put("partition", record.partition());
					data.put("offset", record.offset());
					data.put("value", record.value());
					logger.trace(data.toString());
					// processor.processIncomingMessage(record);
					payloadHelper.processIncomingMessage(record.topic(), "" + record.key(), record.value());
					consumer.commitAsync();
				}
			}
		} catch (WakeupException e) {
			// ignore for shutdown
		} finally {
			consumer.close();
		}
	}
    public void shutdown() {
        consumer.wakeup();
    }

  

}

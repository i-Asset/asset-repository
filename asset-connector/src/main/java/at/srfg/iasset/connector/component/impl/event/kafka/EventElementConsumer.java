package at.srfg.iasset.connector.component.impl.event.kafka;

import java.time.Duration;
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

/**
 * Consumer for incoming messages. Registers for a given topic
 * @author dglachs
 *
 */
public class EventElementConsumer implements Runnable {
    private Logger logger = LoggerFactory.getLogger(BasicEventElement.class);
    private EventProcessorImpl processor;
    private Consumer<Long, String> consumer;
    private Set<String> topics;
    private String hosts;

    
    
    
	public EventElementConsumer(Set<String> topic, EventProcessorImpl processor) {

    	this.processor = processor;
    	// use idShort as default topic and the default hosts setting
    	// TODO: extract topic & hosts from eventElement!
    	this.topics = topic;
    	this.hosts = "iasset.sensornet.salzburgresearch.at:9092";
		
	}
	@Override
	public void run() {
		// configure with hosts 
		// TODO: name the consumer properly
		consumer = ConsumerCreator.createConsumer(processor.getClass().getName(), topics, hosts);
		try {

			ConsumerRecords<Long, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
			for (ConsumerRecord<Long, String> record : records) {
				Map<String, Object> data = new HashMap<>();
				data.put("partition", record.partition());
				data.put("offset", record.offset());
				data.put("value", record.value());
				logger.trace(data.toString());
//				processor.processIncomingMessage(record);
				processor.processIncomingMessage(record.topic(), "" + record.key(), record.value());
				consumer.commitAsync();
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

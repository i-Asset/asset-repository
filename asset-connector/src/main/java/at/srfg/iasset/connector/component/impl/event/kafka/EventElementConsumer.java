package at.srfg.iasset.connector.component.impl.event.kafka;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.srfg.iasset.connector.component.impl.event.EventProcessorImpl;
import at.srfg.iasset.repository.component.ServiceEnvironment;

public class EventElementConsumer {
    private Logger logger = LoggerFactory.getLogger(BasicEventElement.class);
    private EventProcessorImpl processor;
    private Consumer<Long, String> consumer;
    private BasicEventElement eventElement;
    private String topic;
    private String hosts;
    private ServiceEnvironment environment;
    
    
    
	public EventElementConsumer(EventProcessorImpl processor, BasicEventElement element) {

    	this.processor = processor;
    	this.eventElement = element;
    	// use idShort as default topic and the default hosts setting
    	// TODO: extract topic & hosts from eventElement!
    	this.topic = element.getIdShort();
    	this.hosts = "iasset.salzburgresearch.at:9092";
		
	}
    
	public void startConsumer() {
		// configure with hosts
		consumer = ConsumerCreator.createConsumer(eventElement.getIdShort(), topic, hosts);
		
		
        ConsumerRecords<Long, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
        for (ConsumerRecord<Long, String> record : records) {
            Map<String, Object> data = new HashMap<>();
            data.put("partition", record.partition());
            data.put("offset", record.offset());
            data.put("value", record.value());
            logger.trace(data.toString());
            
            try {
                processor.processIncomingMessage(record.topic(), ""+record.key(), record.value());
                consumer.commitAsync();
            } catch (WakeupException e) {
            	// ignore for shutdown
            } finally {
            	consumer.close();
            }
        }
    }
    public void shutdown() {
        consumer.wakeup();
    }    

}

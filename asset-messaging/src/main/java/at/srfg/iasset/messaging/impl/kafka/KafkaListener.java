package at.srfg.iasset.messaging.impl.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.LongDeserializer;

import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.MessageConsumer;
import at.srfg.iasset.messaging.impl.MessageHandler;

public class KafkaListener implements MessageConsumer {
    public static Integer MAX_NO_MESSAGE_FOUND_COUNT=100;
    public static String OFFSET_RESET_LATEST="latest";
    public static String OFFSET_RESET_EARLIER="earliest";
    public static Integer MAX_POLL_RECORDS=1;

	final Consumer<Long, byte[]> consumer;
	public KafkaListener(String hosts, String clientId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, clientId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET_RESET_LATEST);
        // 
        consumer = new KafkaConsumer<>(props);
	}

	@Override
	public void subscribe(String topic, MessageHandler handler) throws MessagingException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                consumer.wakeup();

//                // join the main thread to allow the execution of the code in the main thread
//                try {
//                    mainThread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
		
		Thread runner = new Thread(new Runnable() {
			
			@Override
			public void run() {
				consumer.subscribe(Collections.singletonList(topic));
				// TODO Auto-generated method stub
				try {
					
					// subscribe consumer to our topic(s)
					consumer.subscribe(Arrays.asList(topic));
					
					// poll for new data
					while (true) {
						ConsumerRecords<Long, byte[]> records =
								consumer.poll(Duration.ofMillis(100));
						
						for (ConsumerRecord<Long, byte[]> record : records) {
							
							Map<String, Object> data = new HashMap<>();
							data.put("partition", record.partition());
							data.put("offset", record.offset());
							data.put("value", record.value());
							// notify the components messaging environment,that a new event message arrived!
							handler.acceptMessage(record.topic(), record.value());
							consumer.commitAsync();
							
						}
					}
					
				} catch (WakeupException e) {
					// we ignore this as this is an expected exception when closing a consumer
//		            log.info("Wake up exception!");
				} catch (Exception e) {
//	      	      log.error("Unexpected exception", e);
				} finally {
					consumer.close(); // this will also commit the offsets if need be.
//		            log.info("The consumer is now gracefully closed.");
				}
				
			}
		});
		runner.start();

	}
	private void shutdown() {
		if (consumer != null) {
			consumer.wakeup();
		}
	}

	@Override
	public void close() {
		shutdown();
	}
}

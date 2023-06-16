package at.srfg.iasset.messaging.impl.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;

import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.MessageProducer;

public class KafkaSender implements MessageProducer {
	private final Producer<Long, byte[]> producer;
	public KafkaSender(String hosts, String clientId) {
		Properties props = new Properties();
		// TODO: improve property resolution
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
		// props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
		// CustomPartitioner.class.getName());
		producer = new KafkaProducer<>(props);

	}
	@Override
	public void send(String topic, byte[] message) throws MessagingException {
		ProducerRecord<Long, byte[]> record = new ProducerRecord<Long, byte[]>(topic, message);
		producer.send(record);
	}

	@Override
	public void close() {
		if ( producer!=null) {
			try {
				producer.close();
			} finally {
//				producer = null;
			}
		}
		

	}

}

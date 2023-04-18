package at.srfg.iasset.connector.component.impl.event;

import java.util.Map;

import at.srfg.iasset.connector.component.event.EventConsumer;
import at.srfg.iasset.connector.component.event.EventProducer;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public interface BrokerFactory {
	EventConsumer getConsumer(String topic, MessageBroker broker);
	<T> EventProducer<T> getProducer(String topic, MessageBroker broker);
	class Instance implements BrokerFactory {
		private static Map<String, Class<?>> consumerMap;
		private static Map<String, Class<?>> producerMap;
		static {
			
			ScanResult scanResult = new ClassGraph()
					.acceptPackages(BrokerFactory.class.getPackageName())
					.enableAllInfo()
					.scan();
		}
		
		@Override
		public EventConsumer getConsumer(String topic, MessageBroker broker) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> EventProducer<T> getProducer(String topic, MessageBroker broker) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}

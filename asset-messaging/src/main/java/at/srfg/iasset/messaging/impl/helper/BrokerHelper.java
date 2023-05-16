package at.srfg.iasset.messaging.impl.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.impl.BrokerFactory;
import at.srfg.iasset.messaging.impl.EventHelper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class BrokerHelper {
	
	private static Map<MessageBroker.BrokerType, BrokerFactory> factory = new HashMap<>();
	static {
		ScanResult scanResult = new ClassGraph()
				.enableAllInfo()
				.scan();
		
		List<Class<?>> factoryClasses = scanResult.getClassesImplementing(BrokerFactory.class).loadClasses();
		for (Class<?> clazz : factoryClasses) {
			@SuppressWarnings("unchecked")
			Class<? extends BrokerFactory> factoryClass = (Class<? extends BrokerFactory>) clazz;
			try {
				BrokerFactory aFactory = ConstructorUtils.invokeConstructor(factoryClass);
				factory.put(aFactory.getBrokerType(), aFactory);
			
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
					| InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static boolean canCreate(MessageBroker broker) {
		return factory.get(broker.getBrokerType()) != null;
	}
	public static EventConsumer createConsumer(EventHelper helper) {
		if ( canCreate(helper.getBroker())) {
			return factory.get(helper.getBroker().getBrokerType()).createConsumer(helper);
		}
		return null;
	}
	public static <T> EventProducer<T> createProducer(EventHelper helper, Class<T> payloadType) {
		if ( canCreate(helper.getBroker())) {
			return factory.get(helper.getBroker().getBrokerType()).getProducer(helper, payloadType);
		}
		return null;
	}
}

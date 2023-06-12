package at.srfg.iasset.messaging.impl.helper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.BrokerFactory;
import at.srfg.iasset.messaging.impl.MessageConsumer;
import at.srfg.iasset.messaging.impl.MessageProducer;
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
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * Create a {@link MessageConsumer} connected with the provided {@link MessageBroker}.
	 * 
	 * @param broker The {@link MessageBroker} to connect to
	 * @param clientId The unique clientId
	 * @return
	 * @throws MessagingException
	 */
	public static MessageConsumer createConsumer(MessageBroker broker, String clientId) throws MessagingException {
		if ( canCreate(broker)) {
			return factory.get(broker.getBrokerType()).createConsumer(broker, clientId);
		}
		throw new MessagingException("Cannot create consumer!");
	}
	private static boolean canCreate(MessageBroker broker) {
		return factory.get(broker.getBrokerType()) != null;
	}
	/**
	 * Create a MessageProducer connected with the provided MessageBroker (hosts, broker type)
	 * @param broker The {@link MessageBroker} to connect to
	 * @param clientId The unique clientId
	 * @return
	 * @throws MessagingException
	 */
	public static MessageProducer createProducer(MessageBroker broker, String clientId) throws MessagingException {
		if ( canCreate(broker)) {
			return factory.get(broker.getBrokerType()).createProducer(broker, clientId);
		}
		throw new MessagingException("Cannot create producer - broker element misconfigured!");
	}
}

package at.srfg.iasset.messaging.impl;

import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;

public interface BrokerFactory {
	/**
	 * Create a {@link EventConsumer} connected with a MessageBroker
	 * @param host The host address of the MessageBroker to connect
	 * @return
	 */
	EventConsumer createConsumer(EventHelper helper);
	/**
	 * Create a {@link EventProducer} connected with a MessageBroker
	 * @param <T> 
	 * @param host The host address of the MessageBroker to connect
	 * @param type The type of the payload
	 * @return {@link EventProducer}
	 */
	<T> EventProducer<T> getProducer(EventHelper helper, Class<T> type);
	
	MessageBroker.BrokerType getBrokerType();
//	
//	class Instance implements BrokerFactory {
//		private static Map<String, Class<?>> consumerMap;
//		private static Map<String, Class<?>> producerMap;
//		static {
//			
//			ScanResult scanResult = new ClassGraph()
//					.enableAllInfo()
//					.scan();
//			List<Class<?>> mapperClasses = scanResult.getClassesImplementing(BrokerFactory.class).loadClasses();
//			
////			for ( Class<?> clazz : mapperClasses) {
////				Class<? extends ValueMapper> vmClass = (Class<? extends ValueMapper>) clazz;
////				TypeToken typeVariable = TypeToken.of(vmClass).resolveType(ValueMapper.class.getTypeParameters()[0]);
////				Class<? extends SubmodelElement> clazz2 = (Class<? extends SubmodelElement>) typeVariable.getRawType();
////				try {
////					ValueMapper valueMapper = ConstructorUtils.invokeConstructor(vmClass);
////					producerMap.put(clazz2, valueMapper);
////				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
////					e.printStackTrace();
////				}
////			}
//		}
		

		
//	}
}

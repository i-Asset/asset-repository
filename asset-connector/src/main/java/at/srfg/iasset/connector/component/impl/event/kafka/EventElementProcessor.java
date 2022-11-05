//package at.srfg.iasset.connector.component.impl.event.kafka;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.eclipse.aas4j.v3.model.BasicEventElement;
//import org.eclipse.aas4j.v3.model.ModelingKind;
//import org.eclipse.aas4j.v3.model.StateOfEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import at.srfg.iasset.repository.component.ServiceEnvironment;
//import at.srfg.iasset.repository.event.EventProcessor;
///**
// * Represents the event processing unit for a given messaging broker
// * @author dglachs
// */
//public class EventElementProcessor {
//	Logger logger = LoggerFactory.getLogger(EventElementProcessor.class);
//	
//	private EventProcessor processor;
//	private String hosts;
//
//	
//	private ServiceEnvironment environment;
//	
//	Map<String, Set<BasicEventElement>> inputElements = new HashMap<String, Set<BasicEventElement>>();
//	Map<String, Set<BasicEventElement>> outputElements = new HashMap<String, Set<BasicEventElement>>();
//	
//	Map<String, MessageProducer> producer;
//
//	List<EventElementConsumer> consumers = new ArrayList<EventElementConsumer>();
//	
//	public EventElementProcessor(ServiceEnvironment environment) {
//		this.environment = environment;
//	}
//	public void register(BasicEventElement eventElement) {
//		if ( ModelingKind.INSTANCE.equals(eventElement.getKind()) ) {
//			if ( eventElement.getDirection()!= null) {
//				if ( StateOfEvent.ON.equals(eventElement.getState())) {
//					String topic = eventElement.getMessageTopic();
//					if ( topic == null ) {
//						topic = eventElement.getIdShort();
//					}
//					
//					switch (eventElement.getDirection()) {
//					case INPUT:
//						//
//						if (! inputElements.containsKey(topic)) {
//							inputElements.put(topic, new HashSet<>());
//						}
//						inputElements.get(topic).add(eventElement);
//						break;
//					case OUTPUT:
//						if (! outputElements.containsKey(topic)) {
//							outputElements.put(topic, new HashSet<>());
//						}
//						outputElements.get(topic).add(eventElement);
//						break;
//					}
//				}
//			}
//		}	
//	}
//	public void processIncomingMessage(ConsumerRecord<Long, String> record) {
//		logger.trace(record.topic());
//	}
//    private void startMessageConsumer(String clientName) {
//        int numConsumers = 3;
//        // currently we do not distinguish between 
//        String hosts = "iasset.salzburgresearch.at:9092";
//        String topic = clientName;
//        final ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
//        for (int i = 0; i < numConsumers; i++) {
//            // 
//        	// 
//            EventElementConsumer consumer = new EventElementConsumer(inputElements.keySet(), this);
//            		//ConsumerCreator.createConsumer(clientName, topic, clientName));
//            consumers.add(consumer);
//            executor.submit(consumer);
//        }
//
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                for (EventElementConsumer consumer : consumers) {
//                    consumer.shutdown();
//                }
//                executor.shutdown();
//                try {
//                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//    public void start() {
//    	startMessageConsumer(hosts);
//    	startMessageProducer();
//    }
//    public void stop() {
//    	
//    }
//    private void startMessageProducer() {
//    	// all outgoing 
//    	
//    }
//	private class MessageProducer implements Runnable {
//		private long timeOut;
//		Sender producer;
//		private Thread runner;
//		boolean alive = true;
//		BasicEventElement eventElement;
//		
//		MessageProducer(BasicEventElement element) {
//			// use 2 seconds by default
//			// TODO: use timeout from event element
//			this.timeOut = 2000;
//			this.eventElement = element;
//			producer = new Sender(element.getMessageTopic());
//					
//		}
//		
//		public void start() {
//			alive = true;
//			runner = new Thread(this);
//			runner.start();
//		}
//		public void stop() {
//			alive = false;
//			
//		}
//		
//		@Override
//		public void run() {
//			while (alive) {
//				try {
//					// obtain the current values
//					Object value = environment.getElementValue(eventElement.getObserved());
//					if ( value != null) {
////						processor.send(value);
//					}
//					Thread.sleep(timeOut);
//				} catch (InterruptedException e) {
//					
//				}
//				
//			}
//		}
//		
//	}
//
//}

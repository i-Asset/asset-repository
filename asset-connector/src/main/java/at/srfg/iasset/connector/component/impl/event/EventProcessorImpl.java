package at.srfg.iasset.connector.component.impl.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.ModelingKind;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.StateOfEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.component.impl.event.kafka.EventElementConsumer;
import at.srfg.iasset.connector.component.impl.event.kafka.EventElementProducer;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.event.EventHandler;
import at.srfg.iasset.repository.event.EventProcessor;
import at.srfg.iasset.repository.event.EventProducer;
import at.srfg.iasset.repository.model.helper.EventPayloadHelper;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class EventProcessorImpl implements EventProcessor {
	final ObjectMapper objectMapper;
	final ServiceEnvironment environment;
	
	final Map<String, EventElementConsumer> eventConsumer;
	
	/**
	 * Mapping for EventListeners, the reference used is the key!
	 */
//	final Map<EventPayloadHelper, Set<EventHandler<?>>> eventHandler = new HashMap<EventPayloadHelper,Set<EventHandler<?>>>();
	final Map<EventPayloadHelper, Set<EventHandler<?>>> eventElementConsumer = new HashMap<EventPayloadHelper, Set<EventHandler<?>>>();
	/**
	 * Mapping for {@link EventProducer}
	 */
	final Map<Reference, EventProducer<?>> eventProducer = new HashMap<Reference, EventProducer<?>>();
	
	List<EventPayloadHelper> payloadHelper = new ArrayList<EventPayloadHelper>();
//	List<EventElementConsumer> consumers;
	
	ExecutorService executor;
	List<MessageProducer> outgoingProducer = new ArrayList<EventProcessorImpl.MessageProducer>();
	// 
	public EventProcessorImpl(ObjectMapper objectMapper, ServiceEnvironment environment) {
		this.objectMapper = objectMapper;
		this.environment = environment;
		this.eventConsumer = new HashMap<String, EventElementConsumer>();
	}
	public void registerEventElement(BasicEventElement eventElement) {
		// only instance elements may be registered to act!
		if ( ModelingKind.INSTANCE.equals(eventElement.getKind()) ) {
			if ( eventElement.getDirection()!= null) {
				if ( StateOfEvent.ON.equals(eventElement.getState())) {
					String topic = eventElement.getMessageTopic();
					if ( topic == null ) {
						topic = eventElement.getIdShort();
					}
					collectEventElement(eventElement);
				}
			}
		}	
	}

	private void collectEventElement(BasicEventElement eventElement) {
		EventPayloadHelper helper = new EventPayloadHelper(eventElement).initialize(environment); 
		payloadHelper.add(helper);
		// 
		if (!eventConsumer.containsKey(eventElement.getMessageTopic())) {
			EventElementConsumer consumer = new EventElementConsumer(
					eventElement.getMessageTopic(), 
					helper);
			
			eventConsumer.put(eventElement.getMessageTopic(), consumer);
			
		}
		
	}
	
	
	

	@Override
	public <T> void registerHandler(String semanticId, EventHandler<T> handler) {
		registerHandler(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, semanticId), handler);
	}
	@Override
	public <T> void registerHandler(Reference semanticId, EventHandler<T> handler) {
		List<EventPayloadHelper> mapper = findPayloadHelper(semanticId);
		mapper.stream().forEach(new Consumer<EventPayloadHelper>() {

			@Override
			public void accept(EventPayloadHelper t) {
				if (t.isConsuming()) {
					if (!eventConsumer.containsKey(t.getTopic())) {
						EventElementConsumer consumer = new EventElementConsumer(t.getTopic(), t);
						eventConsumer.put(t.getTopic(), consumer);
					}
					t.addEventHandler(handler);
				}
			}
		});
		
	}


	@Override
	public <T> EventProducer<T> getProducer(Reference semanticReference, Class<T> clazz) {
		Optional<EventPayloadHelper> mapper = findPayloadHelper(semanticReference)
				.stream()
				.filter(new Predicate<EventPayloadHelper>() {

					@Override
					public boolean test(EventPayloadHelper t) {
						return t.isProducing();
					}
				}).findFirst();
		if ( mapper.isPresent()) {
			EventElementProducer<T> producer = new EventElementProducer<T>(mapper.get());
			return (EventProducer<T>) producer;
		}
		
		throw new IllegalArgumentException("No messaging infrastructure for provided reference!");
	}

	

	@Override
	public <T> EventProducer<T> getProducer(String semanticReference, Class<T> clazz) {
		return getProducer(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, semanticReference), clazz);
	}



//	public void sendTestEvent(EventPayload payload) {
//		try {
//			String strPayload = objectMapper.writeValueAsString(payload);
//			processIncomingMessage(strPayload, strPayload, strPayload);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Optional<EventHandler<?>> handler = findHandler(payload);
//		handler.ifPresent(new Consumer<EventHandler<?>>() {
//
//			@Override
//			public void accept(EventHandler<?> t) {
//				acceptPayload(t, payload);
//				
//			}
//			private <T> void acceptPayload(EventHandler<T> handler, Object payload) {
//				T val = objectMapper.convertValue(payload, handler.getPayloadType());
//				handler.onEventMessage(null, val);
//			}
//		});
//		
//	}

	
	private List<EventPayloadHelper> findPayloadHelper(EventPayload payload) {
		List<Reference> matchingReferences = new ArrayList<Reference>();
		if (payload.getObservableSemanticId() != null) {
			matchingReferences.add(payload.getObservableSemanticId());
		}
		if (payload.getSourceSemanticId() != null) {
			matchingReferences.add(payload.getSourceSemanticId());
		}
		if (payload.getObservableReference() != null) {
			matchingReferences.add(payload.getObservableReference());
		}
		if (payload.getSource() != null) {
			matchingReferences.add(payload.getSource());
		}
		return payloadHelper.stream()
			.filter(new Predicate<EventPayloadHelper>() {
	
				@Override
				public boolean test(EventPayloadHelper t) {
					// 
					return t.matches(matchingReferences);
				}
			})
			.collect(Collectors.toList());

	}
	private List<EventPayloadHelper> findPayloadHelper(Reference reference) {
		return payloadHelper.stream()
				.filter(new Predicate<EventPayloadHelper>() {
		
					@Override
					public boolean test(EventPayloadHelper t) {
						return t.matches(reference);
					}
				})
				.collect(Collectors.toList());
	}
	@Override
	public void startEventProcessing() {
		// do not start producer automatically
		// startEventProducer();
		startEventConsumer();
	}
	@Override
	public void stopEventProcessing() {
		stopEventConsumer();
		stopEventProducer();
		
	}
	private void startEventProducer() {
		payloadHelper.stream().filter(new Predicate<EventPayloadHelper>() {
	
			@Override
			public boolean test(EventPayloadHelper t) {
				return t.isActive() && t.isProducing();
			}
		})
		.forEach(new Consumer<EventPayloadHelper>() {
	
			@Override
			public void accept(EventPayloadHelper t) {
				if ( t.getTopic() != null) {
					MessageProducer runner = new MessageProducer(t);
					outgoingProducer.add(runner);
					runner.start(); 
				}
				
			}
		});
	}
	private void stopEventProducer() {
		outgoingProducer.forEach(new Consumer<MessageProducer>() {

			@Override
			public void accept(MessageProducer t) {
				t.stop();
				
			}
		});
		outgoingProducer.clear();
	}
	private void startEventConsumer() {
		executor = Executors.newCachedThreadPool();
		for ( String topic : eventConsumer.keySet()) {
			executor.submit(eventConsumer.get(topic));
		}
		
//		consumers = new ArrayList<EventElementConsumer>();
//		// possibly separate based on broker type and hosts
//		Set<String> topics = payloadHelper.stream()
//				.filter(new Predicate<EventPayloadHelper>() {
//					
//					@Override
//					public boolean test(EventPayloadHelper t) {
//						return t.isConsuming() && t.getTopic()!=null;
//					}
//				})
//				.map(new Function<EventPayloadHelper, String>(){
//					
//					@Override
//					public String apply(EventPayloadHelper t) {
//						return t.getTopic();
//					}
//					
//				})
//				.collect(Collectors.toSet());
//		int numConsumers = 3;
//		executor = Executors.newFixedThreadPool(numConsumers);
//		for (int i = 0; i < numConsumers; i++) {
//			// 
//			
//			EventElementConsumer consumer = new EventElementConsumer(topics, this);
//			consumers.add(consumer);
//			executor.submit(consumer);
//		}
//		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (EventElementConsumer consumer : eventConsumer.values()) {
					consumer.shutdown();
				}
				executor.shutdown();
				try {
					executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void stopEventConsumer() {
		for (String topic : eventConsumer.keySet()) {
			eventConsumer.get(topic).shutdown();
		}
		eventConsumer.clear();
//		for (EventElementConsumer consumer : consumers) {
//			consumer.shutdown();
//			
//		}
//		executor.shutdown();
//		consumers.clear();
	}
	private class MessageProducer implements Runnable {
		private long timeOut;
		EventElementProducer<Object> producer;
		EventPayloadHelper payloadHelper;
		
		private Thread runner;
		boolean alive = true;
		
		MessageProducer(EventPayloadHelper eventElement) {
			// use 2 seconds by default
			// TODO: use timeout from event element
			this.payloadHelper = eventElement;
			this.timeOut = 2000;
					
		}
		
		public void start() {
			alive = true;
			runner = new Thread(this);
			runner.start();
		}
		public void stop() {
			alive = false;
			
		}
		
		@Override
		public void run() {
			producer = new EventElementProducer<Object>(payloadHelper);
			try {
				while (alive) {
					try {
						// obtain the current values
						Object value = payloadHelper.getPayload();
						if ( value != null) {
							producer.sendEvent(value);
						}
						Thread.sleep(timeOut);
					} catch (InterruptedException e) {
						
					}
				}
			} finally {
				producer.stop();
			}
			System.out.println("Exiting " + payloadHelper.getTopic());
		}
		
	}

}

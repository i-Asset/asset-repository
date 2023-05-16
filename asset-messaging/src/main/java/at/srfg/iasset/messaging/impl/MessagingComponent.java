package at.srfg.iasset.messaging.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;
import at.srfg.iasset.messaging.impl.helper.MessageBroker.BrokerType;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.utils.ReferenceUtils;
/**
 * Component providing the messaging functionality
 * 
 * @author dglachs
 *
 */
public class MessagingComponent implements ConnectorMessaging {
	/**
	 * ObjectMapper is required for transforming payload messages
	 */
	final ObjectMapper objectMapper;
	final ServiceEnvironment environment;
	
	final Map<String, EventConsumer> eventConsumer;
	

	List<EventPayloadHelper> payloadHelper = new ArrayList<EventPayloadHelper>();
	
	ExecutorService executor;
	List<EventProducer<?>> outgoingProducer = new ArrayList<EventProducer<?>>();
	// 
	public MessagingComponent(ObjectMapper objectMapper, ServiceEnvironment environment) {
		this.objectMapper = objectMapper;
		this.environment = environment;
		this.eventConsumer = new HashMap<String, EventConsumer>();
	}
	public void removeEventElement(Reference source) {
		payloadHelper.removeAll(findPayloadHelper(source));
	}
	public void registerEventElement(Reference source) {
		Optional<BasicEventElement> theSourceElement = environment.resolve(source, BasicEventElement.class);
		if ( theSourceElement.isPresent()) {
			
			Set<Reference> matchingReferences = new HashSet<Reference>();
			BasicEventElement sourceElement = theSourceElement.get();
			Optional<Referable> observedElement = environment.resolve(sourceElement.getObserved());
			// check for the broker element, this element should specify the surrounding 
			// 
			MessageBroker broker = environment.resolveValue(sourceElement.getMessageBroker(), MessageBroker.class)
					// TODO: use broker settings from configuration!!
					.orElseGet(new Supplier<MessageBroker>() {

						@Override
						public MessageBroker get() {
							MessageBroker broker = new MessageBroker();
							broker.setBrokerType(BrokerType.MQTT);
							broker.setHosts("tcp://localhost:1883");
							return broker;
						}
					});
			
			
			if ( observedElement.isPresent()) {
				Optional<Reference> sourceSemantic = initializeSemantics(source, matchingReferences);
				Optional<Reference> observedSemantic = initializeSemantics(sourceElement.getObserved(), matchingReferences);
				if ( observedSemantic.isPresent()) {
				}
				
				// source and observed are mandatory
				EventPayloadHelper helper = new EventPayloadHelper()
						// reference to source, and the resolved source element
						.source(source, sourceElement)
						// reference to observed and the resolved observed element
						.observed(sourceElement.getObserved(), observedElement.get())
						// reference to source semantic
						.sourceSemantic(sourceSemantic)
						// reference to observed semantic
						.observedSemantic(observedSemantic)
						// use message broker (hosts and broker type (kafka/mqtt))
						.messageBroker(broker);
				
				// keep the helper in the list, so that the helper can be found by EventHandlers ...
				payloadHelper.add(helper);
				
			}
			
		}
	}

	// TODO: check with Reference.referredSemanticId!!!
	private Optional<Reference> initializeSemantics(Reference reference, Set<Reference> matching) {
		if ( reference != null ) {
			matching.add(reference);
			if ( !ReferenceTypes.GLOBAL_REFERENCE.equals(reference.getType())) {
				Optional<SubmodelElement> semanticReference = environment.resolve(reference, SubmodelElement.class);
				if ( semanticReference.isPresent() && semanticReference.get().getSemanticId() != null) {
					return initializeSemantics(semanticReference.get().getSemanticId(), matching);
				}
			}
			else {
				return Optional.of(reference);
			}
		}
		return Optional.empty();
		
	}

	@Override
	public <T> void registerHandler(String semanticId, EventHandler<T> handler) {
		registerHandler(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, semanticId), handler);
	}
	@Override
	public <T> void registerHandler(Reference semanticId, EventHandler<T> handler) {
		// find the payload helper(s) covering the semantic reference
		List<EventPayloadHelper> mapper = findPayloadHelper(semanticId);
		mapper.stream().forEach(new Consumer<EventPayloadHelper>() {

			@Override
			public void accept(EventPayloadHelper t) {
				// register handler only with consuming connections
				if (t.isConsuming()) {
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
			// obtain a messaging producer, configured to send typed objects
			// as the messaging component 
			EventProducer<T> producer = mapper.get().getEventProducer(clazz);
			outgoingProducer.add(producer);
			// register a shutdown hook, to ensure the producer is closed whenever 
			// the VM is stopped!
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					producer.close();
				}
			});

			return (EventProducer<T>) producer;
		}
		// TODO: check environment, possibly load the containing submodel from the server environment
		throw new IllegalArgumentException("No messaging infrastructure for provided reference!");
	}


	@Override
	public <T> EventProducer<T> getProducer(String semanticReference, Class<T> clazz) {
		return getProducer(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, semanticReference), clazz);
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
	private void stopEventProducer() {
		outgoingProducer.forEach(new Consumer<EventProducer<?>>() {

			@Override
			public void accept(EventProducer<?> t) {
				t.close();
				
			}
		});
		outgoingProducer.clear();
	}
	private ExecutorService startEventConsumer() {
		if ( executor == null || executor.isTerminated() ) {
			executor = Executors.newCachedThreadPool();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					for (EventConsumer consumer : eventConsumer.values()) {
						consumer.close();
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
		return executor;
	}
	private void stopEventConsumer() {
		payloadHelper.forEach(new Consumer<EventPayloadHelper>() {

			@Override
			public void accept(EventPayloadHelper t) {
				t.stop();
				
			}
		});

	}

}

package at.srfg.iasset.connector.component.event;

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
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.ModelingKind;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.component.ConnectorMessaging;
import at.srfg.iasset.connector.component.impl.event.EventPayloadHelper;
import at.srfg.iasset.connector.component.impl.event.kafka.EventElementConsumer;
import at.srfg.iasset.connector.component.impl.event.kafka.EventElementProducer;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class MessagingComponent implements ConnectorMessaging {
	final ObjectMapper objectMapper;
	final ServiceEnvironment environment;
	
	final Map<String, EventConsumer> eventConsumer;
	
	/**
	 * Mapping for EventListeners, the reference used is the key!
	 */
	final Map<EventPayloadHelper, Set<EventHandler<?>>> eventElementConsumer = new HashMap<EventPayloadHelper, Set<EventHandler<?>>>();
	/**
	 * Mapping for {@link EventProducer}
	 */
	final Map<Reference, EventProducer<?>> eventProducer = new HashMap<Reference, EventProducer<?>>();
	
	List<EventPayloadHelper> payloadHelper = new ArrayList<EventPayloadHelper>();
//	List<EventElementConsumer> consumers;
	
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
			if ( observedElement.isPresent()) {
				Optional<Reference> sourceSemantic = initializeSemantics(source, matchingReferences);
				Optional<Reference> observedSemantic = initializeSemantics(sourceElement.getObserved(), matchingReferences);
				if ( observedSemantic.isPresent()) {
				}
				// TODO: define subjectId
				Optional<Reference> subjectId = Optional.empty();
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
						// reference to subject id
						.subjectId(subjectId);
				// keep the helper in the list, so that the helper can be found by EventHandlers ...
				payloadHelper.add(helper);
				
			}
			
		}
	}

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
		List<EventPayloadHelper> mapper = findPayloadHelper(semanticId);
		mapper.stream().forEach(new Consumer<EventPayloadHelper>() {

			@Override
			public void accept(EventPayloadHelper t) {
				if (t.isConsuming()) {
//					if (!eventConsumer.containsKey(t.getTopic())) {
//						
//						EventConsumer consumer = new EventElementConsumer(t.getTopic(), t);
//						eventConsumer.put(t.getTopic(), consumer);
//					}
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
			outgoingProducer.add(producer);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					producer.close();
				}
			});

			return (EventProducer<T>) producer;
		}
		
		throw new IllegalArgumentException("No messaging infrastructure for provided reference!");
	}

	

	@Override
	public <T> EventProducer<T> getProducer(String semanticReference, Class<T> clazz) {
		return getProducer(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, semanticReference), clazz);
	}



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

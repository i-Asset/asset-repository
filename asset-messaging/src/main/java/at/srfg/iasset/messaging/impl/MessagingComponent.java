package at.srfg.iasset.messaging.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.EventElementHandler;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
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
	// TODO: obtain clientId from configuration
	final String clientId = UUID.randomUUID().toString();
	
	final Map<String, EventConsumer> eventConsumer;
	
	/**
	 * EventElementHandler represent the runtime environment
	 * for each of the {@link EventElement}s in the {@link ServiceEnvironment}
	 */
	List<EventElementHandler> eventElementHandler = new ArrayList<EventElementHandler>();
	
	ExecutorService executor;
	List<EventProducer<?>> outgoingProducer = new ArrayList<EventProducer<?>>();
	// 
	public MessagingComponent(ObjectMapper objectMapper, ServiceEnvironment environment) {
		this.objectMapper = objectMapper;
		this.environment = environment;
		this.eventConsumer = new HashMap<String, EventConsumer>();
	}
	/**
	 * Search for {@link EventElementHandler} which have
	 * all of the provided references configured
	 * @param reference The references required!
	 * @return
	 */
	private List<EventElementHandler> findEventElementHandler(Reference... reference) {
		return eventElementHandler.stream()
				.filter(new Predicate<EventElementHandler>() {
					
					@Override
					public boolean test(EventElementHandler t) {
						return t.handlesReferences(reference);
					}
					
				})
				.collect(Collectors.toList());

	}
	public void removeEventElement(Reference source) {
		eventElementHandler.removeAll(findEventElementHandler(source));
	}
	public void registerEventElement(Reference source) {
		try {
			EventElementHandler handler = new EventElementHandlerImpl(environment, source);
			eventElementHandler.add(handler);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Optional<BasicEventElement> theSourceElement = environment.resolve(source, BasicEventElement.class);
//		if ( theSourceElement.isPresent()) {
//			//
//			
//			Set<Reference> matchingReferences = new HashSet<Reference>();
//			BasicEventElement sourceElement = theSourceElement.get();
//			Optional<Referable> observedElement = environment.resolve(sourceElement.getObserved());
//			// check for the broker element, this element should specify the surrounding 
//			// 
//			MessageBroker broker = environment.resolveValue(sourceElement.getMessageBroker(), MessageBroker.class)
//					// TODO: use broker settings from configuration!!
//					.orElseGet(new Supplier<MessageBroker>() {
//						// FIXME: use from config!
//						@Override
//						public MessageBroker get() {
//							MessageBroker broker = new MessageBroker();
//							broker.setBrokerType(BrokerType.MQTT);
//							broker.setHosts("tcp://localhost:1883");
//							return broker;
//						}
//					});
//			
//			
//			if ( observedElement.isPresent()) {
//				EventElementHandler handler = new EventElementHandlerImpl(theSourceElement.get(), observedElement.get());
//				// 
//				Optional<Reference> sourceSemantic = initializeSemantics(source, matchingReferences);
//				Optional<Reference> observedSemantic = initializeSemantics(sourceElement.getObserved(), matchingReferences);
//				if ( observedSemantic.isPresent()) {
//				}
//				
//				// source and observed are mandatory
//				EventPayloadHelper helper = new EventPayloadHelper()
//						// reference to source, and the resolved source element
//						.source(source, sourceElement)
//						// reference to observed and the resolved observed element
//						.observed(sourceElement.getObserved(), observedElement.get())
//						// reference to source semantic
//						.sourceSemantic(sourceSemantic)
//						// reference to observed semantic
//						.observedSemantic(observedSemantic)
//						// use message broker (hosts and broker type (kafka/mqtt))
//						.messageBroker(broker);
//				
//				// keep the helper in the list, so that the helper can be found by EventHandlers ...
////				payloadHelper.add(helper);
//				eventElementHandler.add(handler);
//				
//				
//			}
//			
//		}
	}

	// TODO: check with Reference.referredSemanticId!!!
	/**
	 * Collect the references which cause the triggering of the event! 
	 * @param reference
	 * @param matching
	 * @return
	 */
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
	public <T> void registerHandler(EventHandler<T> handler, String ... references) throws MessagingException {
		if ( references == null) {
			registerHandler(handler, (Reference)null);
		}
		registerHandler(handler, ReferenceUtils.asGlobalReferences(references));
	}
	@Override
	public <T> void registerHandler(EventHandler<T> handler, Reference ... references) throws MessagingException {
		// find the event element helper(s) covering the semantics of the event handler
		List<EventElementHandler> elementHandler = findEventElementHandler(handler.getSemanticId());
		// 
		elementHandler.stream().forEach(new Consumer<EventElementHandler>() {
			public void accept(EventElementHandler eventElementHandler) {
				if (eventElementHandler.isConsuming()) {
					// register the event element handler
					try {
						eventElementHandler.registerHandler(handler, references);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
	}

	@Override
	public <T> EventProducer<T> getProducer(Reference semanticReference, Class<T> clazz) {
		Optional<EventElementHandler> mapper = findEventElementHandler(semanticReference)
				.stream()
				.filter(new Predicate<EventElementHandler>() {

					@Override
					public boolean test(EventElementHandler t) {
						return t.isDirection(Direction.OUTPUT);
					}
				}).findFirst();
		if ( mapper.isPresent()) {
			// obtain a messaging producer, configured to send typed objects
			// as the messaging component 
			EventProducer<T> producer = mapper.get().getProducer(clazz);
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

//	private List<EventPayloadHelper> findPayloadHelper(Reference ...reference ) {
//		return payloadHelper.stream()
//				.filter(new Predicate<EventPayloadHelper>() {
//		
//					@Override
//					public boolean test(EventPayloadHelper t) {
//						return t.matchesReference(Arrays.asList(reference));
//					}
//				})
//				.collect(Collectors.toList());
//	}
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
		
		return executor;
	}
	private void stopEventConsumer() {
		eventElementHandler.forEach(new Consumer<EventElementHandler>() {

			@Override
			public void accept(EventElementHandler t) {
				t.shutdown();
				
			}
		});

	}
	@Override
	public <T> EventProducer<T> getProducer(BasicEventElement eventElement, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}

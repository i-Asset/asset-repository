package at.srfg.iasset.connector.component.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.EventElementHandler;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.EventElementHandlerImpl;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;
import at.srfg.iasset.messaging.impl.helper.MessageBroker.BrokerType;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
/**
 * Component providing the messaging functionality
 * 
 * @author dglachs
 *
 */
@ApplicationScoped
public class ConnectorMessagingCDI implements ConnectorMessaging {
	/**
	 * ObjectMapper is required for transforming payload messages
	 */
	@Inject
	private ObjectMapper objectMapper;
	/**
	 * The Service environment provides access to the currently 
	 * active/loaded model
	 */
	@Inject
	private ServiceEnvironment environment;
	
	@Inject
	private ConnectorMessagingSettings settings;
	
	// TODO: override clientId from configuration
	final String clientId = UUID.randomUUID().toString();
	
	private final Map<String, EventConsumer> eventConsumer = new HashMap<>();
	
	/**
	 * EventElementHandler represent the runtime environment
	 * for each of the {@link EventElement}s in the {@link ServiceEnvironment}
	 */
	private List<EventElementHandler> eventElementHandler = new ArrayList<EventElementHandler>();
	
	private ExecutorService executor;
	private List<EventProducer<?>> outgoingProducer = new ArrayList<EventProducer<?>>();

	@PreDestroy
	protected void shutDownMessaging() {
		for (EventProducer<?> producer: outgoingProducer) {
			producer.close();
		}
		for (EventConsumer consumer: eventConsumer.values()) {
			consumer.close();
		}
	}
	/**
	 * Search for {@link EventElementHandler} which have
	 * all of the provided references configured
	 * @param reference The references required!
	 * @return
	 */
	private List<EventElementHandler> findEventElementHandler(Reference ... reference) {
		// when no filter set ... return all
		if ( reference == null || reference.length == 0) {
			return eventElementHandler;
		}
		else {
			// perform filtering
			return eventElementHandler.stream()
					.filter(new Predicate<EventElementHandler>() {
						
						@Override
						public boolean test(EventElementHandler t) {
							return t.handlesReferences(reference);
						}
					})
					.collect(Collectors.toList());
		}

	}
	private List<EventElementHandler> findEventElementHandler(List<Reference> reference) {
		// when no filter set ... return all
		if ( reference == null || reference.size() == 0) {
			return eventElementHandler;
		}
		else {
			// perform filtering
			return eventElementHandler.stream()
					.filter(new Predicate<EventElementHandler>() {
						
						@Override
						public boolean test(EventElementHandler t) {
							return t.handlesReferences(reference);
						}
					})
					.collect(Collectors.toList());
		}

	}
	/**
	 * Remove the {@link EventElement} from the messaging environment.
	 */
	public void removeEventElement(Reference source) {
		List<EventElementHandler> handlers = findEventElementHandler(source);
		for ( EventElementHandler handler : handlers) {
			// stop 
			handler.shutdown();
		}
		eventElementHandler.removeAll(findEventElementHandler(source));
	}
	/**
	 * Register an {@link EventElement} 
	 */
	public void registerEventElement(Reference source) throws MessagingException {
		Optional<BasicEventElement> theSource = this.environment.resolve(source, BasicEventElement.class);
		if ( theSource.isEmpty()) {
			throw new MessagingException("Source element not properly configured: sourceElement not found");
		}
		Optional<Referable> theObserved = this.environment.resolve(theSource.get().getObserved());
		if ( theObserved.isEmpty()) {
			// deactivate the event's state
			throw new MessagingException(
					"Observed element not properly configured: " + theSource.get().getIdShort());
		}
		MessageBroker broker = this.environment.resolveValue(theSource.get().getMessageBroker(), MessageBroker.class)
				// 
				.orElse(getBrokerDefault());
		
		EventElementHandler handler = new EventElementHandlerImpl(this, theSource.get(), source, theObserved.get(), theSource.get().getObserved(), broker);
		eventElementHandler.add(handler);
	}
	private MessageBroker getBrokerDefault() {
		MessageBroker broker = new MessageBroker();
		broker.setHosts(settings.getHosts());
		broker.setBrokerType(BrokerType.valueOf(settings.getBrokerType()));
		return broker;
	}

	@Override
	public <T> void registerHandler(EventHandler<T> handler, String semanticId, String ... references) throws MessagingException {
		if ( references == null) {
			registerHandler(handler, ReferenceUtils.asGlobalReference(semanticId),(Reference)null);
		}
		else {
			registerHandler(handler, ReferenceUtils.asGlobalReference(semanticId), ReferenceUtils.asGlobalReferences(references));
		}
	}
	@Override
	public <T> void registerHandler(EventHandler<T> handler, Reference semanticReference, Reference ... references) throws MessagingException {
		// find the event element helper(s) covering the semantics of the event handler
		List<EventElementHandler> elementHandler = findEventElementHandler(semanticReference);
		for (EventElementHandler eh : elementHandler) {
			eh.registerHandler(handler, references);
		}
		
	}
	@Override
	public <T> void registerHandler(EventHandler<T> handler, Reference semanticReference, List<Reference> references) throws MessagingException {
		List<EventElementHandler> elementHandler = findEventElementHandler(semanticReference);
		for (EventElementHandler eh : elementHandler) {
			eh.registerHandler(handler, references.toArray(new Reference[0]));
		}

		
	}

	@Override
	public <T> void registerHandler(EventHandler<T> handler, String semanticReference, String topic, String... additinalRefs)
			throws MessagingException {
		if ( additinalRefs == null) {
			registerHandler(handler, ReferenceUtils.asGlobalReference(semanticReference), topic, (Reference)null);
		}
		else {
			registerHandler(handler, ReferenceUtils.asGlobalReference(semanticReference), topic, ReferenceUtils.asGlobalReferences(additinalRefs));
		}
		
	}
	@Override
	public <T> void registerHandler(EventHandler<T> handler, Reference semanticReference, String topic, List<Reference> references)
			throws MessagingException {
		
		registerHandler(handler, semanticReference, topic, references.toArray(new Reference[0]));
		
	}
	@Override
	public <T> void registerHandler(EventHandler<T> handler, Reference semanticReference, String topic, Reference ... references ) throws MessagingException {
		List<EventElementHandler> elementHandler = findEventElementHandler(semanticReference);
		for (EventElementHandler eh : elementHandler) {
			eh.registerHandler(handler, topic, references);
		}

		
	}
	@Override
	public <T> EventProducer<T> getProducer(Reference semanticReference, Class<T> clazz) {
		// Filter EventElementHandler's, use first Handler with Direction.OUTPUT
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
		return getProducer(ReferenceUtils.asGlobalReference(semanticReference), clazz);
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

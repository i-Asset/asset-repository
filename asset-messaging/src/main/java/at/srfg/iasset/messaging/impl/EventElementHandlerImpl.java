package at.srfg.iasset.messaging.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.HasSemantics;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.impl.DefaultEventPayload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.EventElementHandler;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.helper.BrokerHelper;
import at.srfg.iasset.messaging.impl.helper.EventHandlerWrapped;
import at.srfg.iasset.messaging.impl.helper.EventProducerImpl;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;
import at.srfg.iasset.messaging.impl.helper.MessageBroker.BrokerType;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.helper.payload.EventPayloadValue;
import at.srfg.iasset.repository.model.helper.payload.ReferenceValue;
import at.srfg.iasset.repository.utils.ReferenceUtils;
/**
 * Class providing the mediation from/to the external
 * messaging environment
 * @author dglachs
 *
 */
public class EventElementHandlerImpl implements EventElementHandler, MessageHandler {
	/**
	 * Reference to the source element
	 */
	Reference sourceReference;
	/**
	 * The source element, provides access to observedReference
	 * 
	 */
	BasicEventElement source;
	/**
	 * The semantic id of the source element (ModelReference)
	 */
	Reference sourceSemantic;
	/**
	 * The reference to the observed element
	 */
	Reference observedReference;
	/**
	 * The semantic id of the observed element (ModelReference)
	 */
	Reference observedSemanticId; 
	/**
	 * The observed Element
	 */
	Referable observed;
	/**
	 * ObjectMapper configured for AAS de/serialization!
	 */
	ObjectMapper objectMapper = ClientFactory.getObjectMapper();
	/**
	 * The message broker outlining the type and host's address
	 */
	MessageBroker broker;
	/**
	 * The connection to the messaging environment, used for sending messages!
	 * There is only one producer per {@link EventElement}
	 */
	MessageProducer producer;
	/**
	 * The connection to the messaging environment, used for reading messages!
	 * There is only one consumer/listener per {@link EventElement}
	 */
	MessageConsumer consumer;
	/**
	 * The list of matching references. The list of matching references
	 * is used to determine which {@link EventElementHandler} to use
	 * when requesting a producer or registering a consumer. 
	 */
	final Set<Reference> matching = new HashSet<>();
	/**
	 * manage a list of "active" eventhandlers
	 */
	final Set<EventHandlerWrapped<?>> eventHandler = new HashSet<>();
	/**
	 * The (local) service environment
	 */
	ServiceEnvironment environment;
	String clientId;
	String topic;
	/**
	 * 
	 * @param environment
	 * @param sourceReference
	 * @throws MessagingException
	 */
	public EventElementHandlerImpl(ServiceEnvironment environment, Reference sourceReference) throws MessagingException {
		
		this.environment = environment;
		// keep the reference
		this.sourceReference = sourceReference;
		// keep the reference in the list
		this.matching.add(sourceReference);
		// resolve the source element from the provided source reference - reference must point to a event element
		Optional<BasicEventElement> theSource = environment.resolve(sourceReference, BasicEventElement.class);
		if ( theSource.isEmpty()) {
			throw new MessagingException("Source element not properly configured!");
		}
		else {
			this.source = theSource.get();
		}
		// use the source to obtain 
		this.sourceSemantic = this.source.getSemanticId();
		this.matching.add(sourceSemantic);
		this.observedReference = this.source.getObserved();
		this.matching.add(observedReference);
		this.matching.add(observedSemanticId);
		// resolve the observed element
		Optional<Referable> theObserved = this.environment.resolve(observedReference);
		if ( theObserved.isEmpty()) {
			throw new MessagingException("Observed element not properly configured: " + new ReferenceValue(observedReference).getValue());
		}
		else {
			this.observed = theObserved.get();
			if ( this.observed instanceof HasSemantics) {
				HasSemantics observedSemantics = (HasSemantics) this.observed;
				this.observedSemanticId = observedSemantics.getSemanticId();
				if ( this.observedSemanticId!=null) {
					this.matching.add(this.observedSemanticId);
					this.matching.addAll(observedSemantics.getSupplementalSemanticIds());
				}
			}
		}
		// resolve the broker element
		//
		this.broker = environment.resolveValue(this.source.getMessageBroker(), MessageBroker.class)
				// TODO: use broker settings from configuration!!
				.orElseGet(new Supplier<MessageBroker>() {
					// FIXME: use from config!
					@Override
					public MessageBroker get() {
						MessageBroker broker = new MessageBroker();
						try {
							BrokerType defaultType = BrokerType.valueOf(environment.getConfigProperty("connector.network.broker.type"));
							broker.setBrokerType(defaultType);
							broker.setHosts(environment.getConfigProperty("connector.network.broker.hosts"));
						} catch (Exception e) {
							
						}
						
						return broker;
					}
				});
		this.clientId = new ReferenceValue(sourceReference).getValue();
		this.topic = this.source.getMessageTopic();
	}
	@Override
	public <T> EventProducer<T> getProducer(Class<T> type) {
		if ( producer == null) {
			try {
				producer = BrokerHelper.createProducer(broker, clientId);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		EventProducerImpl<T> eventProducer = new EventProducerImpl<>(type, this);
		return (EventProducer<T>) eventProducer;
	}
	private EventPayload asPayload(Reference subjectId, String topic, byte[] payload) {
		return new DefaultEventPayload.Builder()
				.source(sourceReference)
				.sourceSemanticId(sourceSemantic)
				.observableReference(observedReference)
				.observableSemanticId(observedSemanticId)
				.subjectId(subjectId)
				.payload(payload)
				.timeStamp(Instant.now().toString())
				.topic(topic)
				
				.build();
	}
	private <T> T fromMessaging(byte[] incoming, Class<T> clazz) throws MessagingException {
		try {
			return objectMapper.readValue(incoming, clazz);
		} catch (IOException e) {
			throw new MessagingException("Message not readable!");
		}
	}
	private List<Reference> payloadRef(EventPayload payload) {
		List<Reference> pRef = new ArrayList<>();
		if ( payload.getSource()!=null) {
			pRef.add(payload.getSource());
		}
		if ( payload.getSourceSemanticId()!=null) {
			pRef.add(payload.getSourceSemanticId());
		}
		if ( payload.getObservableReference()!=null) {
			pRef.add(payload.getObservableReference());
		}
		if ( payload.getObservableSemanticId()!=null) {
			pRef.add(payload.getObservableSemanticId());
		}
		if ( payload.getSubjectId()!=null) {
			pRef.add(payload.getSubjectId());
		}
		return pRef;
	}
	private List<String> payloadRef(EventPayloadValue payload) {
		List<String> pRef = new ArrayList<>();
		if ( payload.getSource()!=null) {
			pRef.add(payload.getSource().getValue());
		}
		if ( payload.getSourceSemanticId()!=null) {
			pRef.add(payload.getSourceSemanticId().getValue());
		}
		if ( payload.getObservableReference()!=null) {
			pRef.add(payload.getObservableReference().getValue());
		}
		if ( payload.getObservableSemanticId()!=null) {
			pRef.add(payload.getObservableSemanticId().getValue());
		}
		if ( payload.getSubjectId()!=null) {
			pRef.add(payload.getSubjectId().getValue());
		}
		return pRef;
	}
	@Override
	public boolean handlesReferences(Reference... references) {
		if ( references == null) {
			return false;
		}
		return Arrays.asList(references).stream().allMatch(new Predicate<Reference>() {

			@Override
			public boolean test(Reference t) {
				// each of the provided references must be in the 
				// matching list
				return matching.contains(t);
			}});
	}

	@Override
	public boolean isActive() {
		return StateOfEvent.ON.equals(source.getState());
	}

	@Override
	public boolean isConsuming() {
		return Direction.INPUT.equals(source.getDirection());
	}

	@Override
	public boolean isDirection(Direction direction) {
		return direction.equals(source.getDirection());
	}

	@Override
	public <T> void registerHandler(EventHandler<T> handler, Reference ... references ) throws MessagingException {
		if (consumer == null ) {
			consumer = BrokerHelper.createConsumer(broker, clientId);
			consumer.subscribe(topic, this);
		}
		// register a shutdown hook, so that the consumer is closed
		// when the VM is stopped
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// close the listeners
				consumer.close();
			}
		});
		
		EventHandlerWrapped<T> wapped = new EventHandlerWrapped<>(handler, references);
		eventHandler.add(wapped);
	
	}
	@Override
	public <T> void registerHandler(EventHandler<T> handler) throws MessagingException {
		registerHandler(handler, null);
		
	}
	@Override
	public <T> void removeHandler(EventHandler<T> handler) {
		EventHandlerWrapped<?> toRemove = null;
		for (EventHandlerWrapped<?> eh : eventHandler) {
			if ( eh.isHandler(handler)) {
				toRemove = eh;
				break;
			}
		}
		if ( toRemove!=null) {
			eventHandler.remove(toRemove);
		}
		// no handler left - close the consumer
		if ( eventHandler.size() ==0 ) {
			consumer.close();
			consumer = null;
		}
	}
	@Override
	public void shutdown() {
		if ( consumer != null) {
			consumer.close();
		}
		if ( producer != null) {
			producer.close();
		}
		
	}

	@Override
	public void acceptMessage(String topic, byte[] message) throws MessagingException {
		EventPayload p = fromMessaging(message, EventPayload.class);
		// 
		List<Reference> matching = payloadRef(p);
		
		for (EventHandlerWrapped<?> handler : eventHandler ) {
			if ( handler.matchesReference(matching)) {
				// 
				handler.handleMessage(p, p.getPayload());
			}
		}
	}

	@Override
	public void acceptMessage(String topic, String message) throws MessagingException {
		// TODO Auto-generated method stub
		
	}
	public void sendEvent(Object eventPayload) throws MessagingException {
		sendEvent(eventPayload, (Reference)null);
	}
	public void sendEvent(Object eventPayload, String subjectId) throws MessagingException {
		Reference subjectReference = ReferenceUtils.asGlobalReference(subjectId);
		sendEvent(eventPayload, subjectReference);
	}
	public void sendEvent(Object eventPayload, Reference subjectReference) throws MessagingException {
		byte[] bytes;
		try {
			bytes = objectMapper.writeValueAsBytes(eventPayload);
			EventPayload p = asPayload(subjectReference, topic, bytes);
			bytes = objectMapper.writeValueAsBytes(p);
			// send to the messaging infrastructure
			producer.send(topic, bytes);
			// 
			this.source.setLastUpdate(Instant.now().toString());
		} catch (JsonProcessingException e) {
			throw new MessagingException(e.getLocalizedMessage());
		}
		
	}
}

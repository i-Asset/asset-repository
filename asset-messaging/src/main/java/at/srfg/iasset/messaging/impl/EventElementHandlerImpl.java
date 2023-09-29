package at.srfg.iasset.messaging.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.BasicEventElement;
import org.eclipse.digitaltwin.aas4j.v3.model.Direction;
import org.eclipse.digitaltwin.aas4j.v3.model.EventElement;
import org.eclipse.digitaltwin.aas4j.v3.model.EventPayload;
import org.eclipse.digitaltwin.aas4j.v3.model.HasSemantics;
import org.eclipse.digitaltwin.aas4j.v3.model.ModelReference;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.StateOfEvent;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEventPayload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.ConnectorMessaging;
import at.srfg.iasset.messaging.EventElementHandler;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.messaging.impl.helper.BrokerHelper;
import at.srfg.iasset.messaging.impl.helper.EventHandlerWrapped;
import at.srfg.iasset.messaging.impl.helper.EventProducerImpl;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.helper.ValueHelper;
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
	ModelReference sourceReference;
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
	ModelReference observedReference;
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
	private MessageProducer producer;
	/**
	 * The connection to the messaging environment, used for reading messages!
	 * There is only one consumer/listener per {@link EventElement}
	 */
	private MessageConsumer consumer;
	/**
	 * The list of matching references. The list of matching references
	 * is used to determine which {@link EventElementHandler} to use
	 * when requesting a producer or registering a consumer. 
	 */
	private final Set<Reference> matching = new HashSet<>();
	/**
	 * manage a list of "active" eventhandlers
	 */
	private final Set<EventHandlerWrapped<?>> eventHandler = new HashSet<>();
	/**
	 * The (local) service environment
	 */
//	private ServiceEnvironment environment;
	String clientId;
	String topic;
	/**
	 * 
	 * @param environment
	 * @param sourceReference
	 * @throws MessagingException
	 */
	public EventElementHandlerImpl(
					ConnectorMessaging parent,
					BasicEventElement source,			// The "resolved" element
					ModelReference sourceReference,		// The reference to the source Element
					Referable observed,					// the "resolved" observed element
					ModelReference observedReference,	// the reference to the observed element
					MessageBroker messageBroker			// broker settings (host, type)
			) {
		this.source = source;
		if ( this.source instanceof HasSemantics) {
			// manage the semantic references
			HasSemantics sourceSemantics = (HasSemantics) this.source;
			this.observedSemanticId = sourceSemantics.getSemanticId();
			if ( this.observedSemanticId!=null) {
				this.matching.add(this.observedSemanticId);
				this.matching.addAll(sourceSemantics.getSupplementalSemanticIds());
			}
		}
		this.sourceReference = sourceReference;
		this.matching.add(sourceReference);
		this.observed = observed;
		this.observedReference = observedReference;
		this.matching.add(observedReference);

		if ( this.observed instanceof HasSemantics) {
			// manage the semantic references
			HasSemantics observedSemantics = (HasSemantics) this.observed;
			this.observedSemanticId = observedSemantics.getSemanticId();
			if ( this.observedSemanticId!=null) {
				this.matching.add(this.observedSemanticId);
				this.matching.addAll(observedSemantics.getSupplementalSemanticIds());
			}
		}
		this.broker = messageBroker;
		this.clientId = new ReferenceValue(sourceReference).getValue() + "-" + UUID.randomUUID().toString();
		// TODO: handle missing topic
		this.topic = this.source.getMessageTopic();

	}
	
	
	@Override
	public <T> EventProducer<T> getProducer(Class<T> type) {
		if ( producer == null) {
			try {
				producer = BrokerHelper.createProducer(broker, clientId);
				Runtime.getRuntime().addShutdownHook(new Thread("Shutdown-Hook."+clientId) {
					@Override
					public void run() {
						// close the listeners
						producer.close();
					}
				});

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
	public boolean handlesReferences(List<Reference> references) {
		// when no references provided
		if ( references == null || references.size() == 0) {
			return false;
		}
		return references.stream().allMatch(new Predicate<Reference>() {

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
		registerHandler(handler, topic, references);	
	}
	@Override
	public <T> void registerHandler(EventHandler<T> handler, String topic, Reference ... references ) throws MessagingException {
		if ( Direction.INPUT.equals(source.getDirection())) {
			if (consumer == null ) {
				consumer = BrokerHelper.createConsumer(broker, clientId);
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
			// subscribe the current handler for the provided topic!
			consumer.subscribe(topic, this);
			EventHandlerWrapped<T> wapped = new EventHandlerWrapped<>(objectMapper, handler, observed, references);
			eventHandler.add(wapped);
			
		}
	
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
			// when observed is a SubmodelElement - perform validation
			if ( SubmodelElement.class.isInstance(observed)) {
				SubmodelElement observedElement = SubmodelElement.class.cast(observed);
				JsonNode valueAsNode = objectMapper.convertValue(eventPayload, JsonNode.class);
				ValueHelper.applyValue(observedElement, valueAsNode);
				eventPayload = ValueHelper.toValue(observedElement);
				
			}
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

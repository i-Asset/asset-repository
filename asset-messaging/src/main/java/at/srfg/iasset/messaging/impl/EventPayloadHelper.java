package at.srfg.iasset.messaging.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EventElement;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.impl.DefaultEventPayload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.messaging.EventConsumer;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.impl.helper.BrokerHelper;
import at.srfg.iasset.messaging.impl.helper.MessageBroker;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
/**
 * Helper class providing messaging functionality for a single {@link EventElement}.
 * 
 * <p>
 * 
 * </p>
 * 
 * @author dglachs
 *
 */
public class EventPayloadHelper implements PayloadConsumer, EventHelper {
	// source
	Reference source;
	// the source element defining the topic
	BasicEventElement sourceElement;
	// semantic reference of the source element
	Reference sourceSemantic;
	// observed
	Reference observed;
	// the resolved observed element
	Referable observedElement;
	// semantic reference of the observed element
	Reference observedSemantic;
	// should be reference to the event element responsible for sending
	Reference subjectId;
	// The host's address
	String hosts;
	MessageBroker broker;
	/**
	 * Set of references which determine when a incoming message is to be 
	 * handled.
	 */
	private Set<Reference> matchingReferences;
	private Set<EventHandler<?>> handler;
	private EventConsumer consumer;
	// need to have the object mapper available
	ObjectMapper objectMapper = ClientFactory.getObjectMapper();

	public EventPayloadHelper() {
		this.matchingReferences = new HashSet<Reference>();
		this.handler = new HashSet<EventHandler<?>>();
		
	}
	public EventPayloadHelper source(Reference source, BasicEventElement sourceElement) {
		this.source = source;
		this.sourceElement = sourceElement;
		this.matchingReferences.add(source);
		return this;
	}
	public EventPayloadHelper sourceSemantic(Optional<Reference> sourceSemantic) {
		sourceSemantic.ifPresent(new Consumer<Reference>() {

			@Override
			public void accept(Reference t) {
				EventPayloadHelper.this.sourceSemantic = t;
				EventPayloadHelper.this.matchingReferences.add(t);
				
			}});
		return this;
	}
	public EventPayloadHelper messageBroker(MessageBroker broker) {
		this.broker = broker;
		return this;
	}
	/**
	 * Build Method
	 * @param observed
	 * @param observedElement
	 * @return
	 */
	public EventPayloadHelper observed(Reference observed, Referable observedElement) {
		// TODO check null
		this.observed = observed;
		this.matchingReferences.add(observed);
		this.observedElement = observedElement;
		return this;
	}
	/**
	 * Build method
	 * @param observedSemantic
	 * @return
	 */
	public EventPayloadHelper observedSemantic(Optional<Reference> observedSemantic) {
		observedSemantic.ifPresent(new Consumer<Reference>() {

			@Override
			public void accept(Reference t) {
				EventPayloadHelper.this.observedSemantic = t;
				EventPayloadHelper.this.matchingReferences.add(t);
				
			}});
		return this;
	}
	public EventPayloadHelper subjectId(Optional<Reference> subjectId) {
		subjectId.ifPresent(new Consumer<Reference>() {

			@Override
			public void accept(Reference t) {
				EventPayloadHelper.this.subjectId = t;
				EventPayloadHelper.this.matchingReferences.add(t);
				
			}});
		return this;
	}
	public EventPayloadHelper hosts(String hosts) {
		this.hosts = hosts;

		return this;
		
	}
	public EventPayloadHelper matches(Reference ... reference) {
		this.matchingReferences.addAll(Arrays.asList(reference));
		return this;
	}
	
	/**
	 * Register an {@link EventHandler} with the current messaging infrastructure
	 * @param handler
	 */
	public void addEventHandler(EventHandler<?> handler) {
		this.handler.add(handler);
		// check whether the consumer is already present
		if ( consumer == null )  {
			// the groupId must be unique for each event element
			// 
			// the topic may contain wildcards
			
			// TODO: check for broker type and hosts
			consumer = BrokerHelper.createConsumer(this);
			if ( consumer != null) {
				// 
				consumer.subscribe(getTopic());
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
		}
	}
	public <T> EventProducer<T> getEventProducer(Class<T> payloadType) {
		return BrokerHelper.createProducer(this, payloadType);
	}
	
	public void removeHandler(EventHandler<?> handler) {
		
		// TODO: implement
	}
	public void acceptMessage(String topic,  byte[] message ) {
		try {
			final EventPayload fullPayload = fromByteArray(message);
			for (EventHandler<?> eventHandler: handler) {
					Object payload = objectMapper.readerFor(eventHandler.getPayloadType()).readValue(fullPayload.getPayload());
					
					acceptPayload(fullPayload, payload, eventHandler);
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void processIncomingMessage(String topic, byte[] message) {
		try {
			final EventPayload fullPayload = fromByteArray(message);
			for (EventHandler<?> eventHandler: handler) {
					Object payload = objectMapper.readerFor(eventHandler.getPayloadType()).readValue(fullPayload.getPayload());
					
					acceptPayload(fullPayload, payload, eventHandler);
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void processIncomingMessage(String topic, String message) {
		try {
			final EventPayload fullPayload = objectMapper.readerFor(EventPayload.class).readValue(message);
			for (EventHandler<?> eventHandler: handler) {
					Object payload = objectMapper.readerFor(eventHandler.getPayloadType()).readValue(fullPayload.getPayload());
					
					acceptPayload(fullPayload, payload, eventHandler);
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public EventPayload toEventPayload(Object payload) {
		try {
			return new DefaultEventPayload.Builder()
					.source(source)
					.sourceSemanticId(sourceSemantic)
					.observableReference(observed)
					.observableSemanticId(observedSemantic)
					.subjectId(subjectId)
					.payload(objectMapper.writeValueAsString(payload))
					.timeStamp(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE))
					.topic(sourceElement.getMessageTopic())
					.build();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Convert the payload object into the {@link EventHandler}'s type and 
	 * invoke the {@link EventHandler#onEventMessage(EventPayload, Object)}
	 * method.
	 * @param <T>
	 * @param handler
	 * @param payload
	 */
	private <T> void acceptPayload(EventPayload fullPayload, Object payload, EventHandler<T> handler) {
		T val = objectMapper.convertValue(payload, handler.getPayloadType());
		handler.onEventMessage(fullPayload, (T) val);
	}
	/**
	 * Obtain the messaging topic defined in {@link BasicEventElement}
	 * @return the message topic associated with this {@link EventPayloadHelper}
	 */
	public String getTopic() {
		return sourceElement.getMessageTopic();
	}
	public MessageBroker getBroker() {
		return broker;
	}
	public boolean matches(Reference reference) {
		return matchingReferences.contains(reference);
	}
	public boolean isActive() {
		return !StateOfEvent.OFF.equals(sourceElement.getState());
	}
	public boolean isProducing() {
		return Direction.OUTPUT.equals(sourceElement.getDirection() );
	}
	public boolean isConsuming() {
		return Direction.INPUT.equals(sourceElement.getDirection() );
	}
	public boolean matches(List<Reference> references) {
		return references.stream().filter(new Predicate<Reference>() {
	
				@Override
				public boolean test(Reference t) {
					return matches(t);
				}
			}).findFirst()
			.isPresent();
	}
	public EventPayload asPayload(String payload) {
		return new DefaultEventPayload.Builder()
		.source(source)
		.sourceSemanticId(sourceSemantic)
		.observableReference(observed)
		.observableSemanticId(observedSemantic)
		.subjectId(subjectId)
		.payload(payload)
		.timeStamp(LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE))
		.topic(sourceElement.getMessageTopic())
		.build();
	}
	@Override
	public void stop() {
		if (consumer != null) {
			consumer.close();
		}
		
	}
	@Override
	public byte[] toByteArray(EventPayload payload) {
		try {
			return objectMapper.writeValueAsBytes(payload);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public EventPayload fromByteArray(byte[] payload) {
		try {
			return objectMapper.readValue(payload, EventPayload.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

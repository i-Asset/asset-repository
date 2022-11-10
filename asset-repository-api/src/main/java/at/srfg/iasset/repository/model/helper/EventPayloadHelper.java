package at.srfg.iasset.repository.model.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.impl.DefaultEventPayload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.event.EventHandler;

public class EventPayloadHelper {
	final BasicEventElement eventElement;
	SubmodelElement observedElement;
	Reference observed;
	Reference observedSemantic;
	// should be reference to the event element responsible for sending
	Reference source;
	Reference sourceSemantic;
	Reference subjectId;
	private List<Reference> matchingReferences;
	private Set<EventHandler<?>> handler;
	ObjectMapper objectMapper = ClientFactory.getObjectMapper();

	

	public EventPayloadHelper(BasicEventElement eventElement) {
		this.eventElement = eventElement;
		this.handler = new HashSet<EventHandler<?>>();
	}
	public EventPayloadHelper initialize(ServiceEnvironment environment) {
		this.matchingReferences = new ArrayList<Reference>();
		this.observed = eventElement.getObserved();
		this.observedElement = environment.resolve(this.observed, SubmodelElement.class).orElse(null);
		this.observedSemantic = initializeSemantics(environment, matchingReferences, observed);
		
		this.sourceSemantic = initializeSemantics(environment, matchingReferences, eventElement.getSemanticId());
		return this;
	}
	
	private Reference initializeSemantics(ServiceEnvironment environment, List<Reference> matching, Reference reference) {
		if ( reference != null ) {
			matching.add(reference);
			if ( !ReferenceTypes.GLOBAL_REFERENCE.equals(reference.getType())) {
				Optional<SubmodelElement> semanticReference = environment.resolve(reference, SubmodelElement.class);
				if ( semanticReference.isPresent() && semanticReference.get().getSemanticId() != null) {
					return initializeSemantics(environment, matching,semanticReference.get().getSemanticId());
				}
			}
			else {
				return reference;
			}
		}
		return null;
		
	}
	public void addEventHandler(EventHandler<?> handler) {
		this.handler.add(handler);
	}
	public void processIncomingMessage(String topic, String key, String message) {

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

	public String getTopic() {
		return eventElement.getMessageTopic();
	}
	public Object getPayload() {
		return ValueHelper.toValue(this.observedElement);
	}
	public boolean matches(Reference reference) {
		return matchingReferences.contains(reference);
	}
	public boolean isActive() {
		return !StateOfEvent.OFF.equals(eventElement.getState());
	}
	public boolean isProducing() {
		return Direction.OUTPUT.equals(eventElement.getDirection() );
	}
	public boolean isConsuming() {
		return Direction.INPUT.equals(eventElement.getDirection() );
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
		.topic(eventElement.getMessageTopic())
		.build();
	}
	
}

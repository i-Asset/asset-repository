package at.srfg.iasset.connector.component.impl.event.kafka;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.BasicEventElement;
import org.eclipse.aas4j.v3.model.Direction;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.HasSemantics;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.StateOfEvent;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.impl.DefaultEventPayload;

import at.srfg.iasset.repository.component.ServiceEnvironment;

public class EventPayloadHelper {
	final BasicEventElement eventElement;
	
	Reference observed;
	Reference observedSemantic;
	// should be reference to the event element responsible for sending
	Reference source;
	Reference sourceSemantic;
	Reference subjectId;
	private List<Reference> matchingReferences;

	public EventPayloadHelper(BasicEventElement eventElement) {
		this.eventElement = eventElement;
	}
	public EventPayloadHelper initialize(ServiceEnvironment environment) {
		this.matchingReferences = new ArrayList<Reference>();
		this.observed = eventElement.getObserved();
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
	public String getTopic() {
		return eventElement.getMessageTopic();
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

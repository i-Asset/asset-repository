package at.srfg.iasset.repository.model.helper.payload.mapper;

import org.eclipse.digitaltwin.aas4j.v3.model.EventPayload;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEventPayload;

import at.srfg.iasset.repository.model.helper.payload.EventPayloadValue;
import at.srfg.iasset.repository.model.helper.payload.PayloadValueHelper;
import at.srfg.iasset.repository.model.helper.payload.PayloadValueMapper;
import at.srfg.iasset.repository.model.helper.value.BlobValue;

public class EventPayloadValueMapper implements PayloadValueMapper<EventPayload, EventPayloadValue> {

	@Override
	public EventPayloadValue mapToValue(EventPayload modelElement) {
		EventPayloadValue value = new EventPayloadValue();
		value.setSource(PayloadValueHelper.toValue(modelElement.getSource()));
		value.setSourceSemanticId(PayloadValueHelper.toValue(modelElement.getSourceSemanticId()));
		value.setObservableReference(PayloadValueHelper.toValue(modelElement.getObservableReference()));
		value.setObservableSemanticId(PayloadValueHelper.toValue(modelElement.getObservableSemanticId()));
		value.setSubjectId(PayloadValueHelper.toValue(modelElement.getSubjectId()));
		value.setTopic(modelElement.getTopic());
		value.setTimestamp(modelElement.getTimeStamp());
		// check for proprietary content type
		value.setPayload(new BlobValue("application/json", modelElement.getPayload()));
		return value;
	}

	@Override
	public EventPayload mapFromValue(EventPayloadValue valueElement) {
		EventPayload payload = new DefaultEventPayload.Builder()
				.source(PayloadValueHelper.fromValue(valueElement.getSource(), Reference.class))
				.sourceSemanticId(PayloadValueHelper.fromValue(valueElement.getSourceSemanticId(), Reference.class))
				.observableReference(PayloadValueHelper.fromValue(valueElement.getObservableReference(), Reference.class))
				.observableSemanticId(PayloadValueHelper.fromValue(valueElement.getObservableSemanticId(), Reference.class))
				.subjectId(PayloadValueHelper.fromValue(valueElement.getSubjectId(), Reference.class))
				.topic(valueElement.getTopic())
				.timeStamp(valueElement.getTimestamp())
				.payload(valueElement.getPayload().getValue())
				.build();
		return payload;
	}

}

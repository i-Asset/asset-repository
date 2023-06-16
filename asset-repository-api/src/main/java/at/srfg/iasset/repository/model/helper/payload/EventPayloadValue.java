package at.srfg.iasset.repository.model.helper.payload;

import java.time.Instant;

import at.srfg.iasset.repository.model.helper.value.BlobValue;

public class EventPayloadValue extends PayloadValue {
	private ReferenceValue source;
	private ReferenceValue sourceSemanticId;
	private ReferenceValue observableReference;
	private ReferenceValue observableSemanticId;
	private String topic;
	private ReferenceValue subjectId;
	private String timestamp;
	private BlobValue payload;
	
	public EventPayloadValue() {
		
	}
	public ReferenceValue getSource() {
		return source;
	}

	public void setSource(ReferenceValue source) {
		this.source = source;
	}

	public ReferenceValue getSourceSemanticId() {
		return sourceSemanticId;
	}

	public void setSourceSemanticId(ReferenceValue sourceSemanticId) {
		this.sourceSemanticId = sourceSemanticId;
	}

	public ReferenceValue getObservableReference() {
		return observableReference;
	}

	public void setObservableReference(ReferenceValue observableReference) {
		this.observableReference = observableReference;
	}

	public ReferenceValue getObservableSemanticId() {
		return observableSemanticId;
	}

	public void setObservableSemanticId(ReferenceValue observableSemanticId) {
		this.observableSemanticId = observableSemanticId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public ReferenceValue getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(ReferenceValue subjectId) {
		this.subjectId = subjectId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public BlobValue getPayload() {
		return payload;
	}

	public void setPayload(BlobValue payload) {
		this.payload = payload;
	}
}

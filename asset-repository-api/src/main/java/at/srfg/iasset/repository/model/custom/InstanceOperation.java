package at.srfg.iasset.repository.model.custom;

import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;

public class InstanceOperation extends DefaultOperation implements Operation {
	public InstanceOperation() {
		
	}
	public InstanceOperation(Operation other) {
		setEmbeddedDataSpecifications(other.getEmbeddedDataSpecifications());
		setDescriptions(other.getDescriptions());
		setDisplayNames(other.getDisplayNames());
		setEmbeddedDataSpecifications(other.getEmbeddedDataSpecifications());
		setExtensions(other.getExtensions());
		setIdShort(other.getIdShort());
		setQualifiers(other.getQualifiers());
		setSemanticId(other.getSemanticId());
		setSupplementalSemanticIds(other.getSupplementalSemanticIds());
		setInoutputVariables(other.getInoutputVariables());
		setInputVariables(other.getInputVariables());
		setOutputVariables(other.getOutputVariables());
	}
	
	@JsonIgnore
	private OperationCallback callback;

	public void callback(OperationCallback callbackFunction) {
		this.callback = callbackFunction;
	}
	public OperationCallback callback() {
		if ( callback == null ) {
			throw new UnsupportedOperationException("Operation not provided: " + getIdShort());
		}
		return this.callback;
	}

	public void invokeOperation(OperationInvocation invocation) throws OperationInvocationException {
		if ( callback != null) {
			callback.execute(invocation);
		}
	}


}

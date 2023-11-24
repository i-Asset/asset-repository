package at.srfg.iasset.repository.model.operation;

import org.eclipse.digitaltwin.aas4j.v3.model.Reference;


public class SemanticOperationRequest<T> {

	private Reference operationReference;
	
	private Reference operationSemanticId;
	
	private OperationRequestValue request;
	
	public SemanticOperationRequest(OperationRequestValue request) {
		this.request = request;
	}

	/**
	 * @return the operationReference
	 */
	public Reference getOperationReference() {
		return operationReference;
	}

	/**
	 * @param operationReference the operationReference to set
	 */
	public void setOperationReference(Reference operationReference) {
		this.operationReference = operationReference;
	}

	/**
	 * @return the operationSemanticId
	 */
	public Reference getOperationSemanticId() {
		return operationSemanticId;
	}

	/**
	 * @param operationSemanticId the operationSemanticId to set
	 */
	public void setOperationSemanticId(Reference operationSemanticId) {
		this.operationSemanticId = operationSemanticId;
	}

	/**
	 * @return the request
	 */
	public OperationRequestValue getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(OperationRequestValue request) {
		this.request = request;
	}

}

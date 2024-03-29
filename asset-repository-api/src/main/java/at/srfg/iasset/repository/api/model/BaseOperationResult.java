package at.srfg.iasset.repository.api.model;

public class BaseOperationResult extends Result {
	private ExecutionState executionState;
	
	/**
	 * @return the executionState
	 */
	public ExecutionState getExecutionState() {
		return executionState;
	}

	/**
	 * @param executionState the executionState to set
	 */
	public void setExecutionState(ExecutionState executionState) {
		this.executionState = executionState;
	}
	

}

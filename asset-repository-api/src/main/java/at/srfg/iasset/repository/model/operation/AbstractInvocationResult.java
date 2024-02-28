package at.srfg.iasset.repository.model.operation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.Operation;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.srfg.iasset.repository.api.model.BaseOperationResult;
import at.srfg.iasset.repository.model.InvocationResult;
/**
 * Denotes the results of an {@link Operation} and provides
 * access to the <code>outputArguments</code> as well as <code>inoutputArguments</code> 
 * used during the operation execution!
 */
public abstract class AbstractInvocationResult<T> extends BaseOperationResult implements InvocationResult<T> {
	@JsonProperty("outpuArguments")
	private List<T> outputArguments = new ArrayList<>();
	@JsonProperty("inoutputArguments")
	private List<T> inoutputArguments = new ArrayList<>();

	@Override
	public void setOutputArguments(List<T> inputVariables) {
		this.outputArguments = inputVariables;
	}
	@Override
	public List<T> getOutputArguments() {
		return outputArguments;
	}
	@Override
	public void setInoutputArguments(List<T> inoutputVariables) {
		this.inoutputArguments = inoutputVariables;
	}
	@Override
	public List<T> getInoutputArguments() {
		return inoutputArguments;
	}
}

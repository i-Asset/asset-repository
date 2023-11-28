package at.srfg.iasset.repository.model;

import java.util.List;

public interface InvocationResult<T> {

	void setOutputArguments(List<T> outputArguments);

	List<T> getOutputArguments();

	void setInoutputArguments(List<T> inoutpuArguments);

	List<T> getInoutputArguments();
	
	default T getOutputArgument(int index) {
		return getOutputArguments().get(index);
	}
	default T getInputArgument(int index) {
		return getInoutputArguments().get(index);
	}

	default InvocationResult<T> outputArgument(T output) {
		getOutputArguments().add(output);
		return this;
	}

	default InvocationResult<T> inoutputArgument(T inoutput) {
		getInoutputArguments().add(inoutput);
		return this;
	}
	default InvocationResult<T> inoutputArguments(List<T> inout) {
		setInoutputArguments(inout);
		return this;
	}
	default InvocationResult<T> ioutputArguments(List<T> out) {
		setOutputArguments(out);
		return this;
	}

}
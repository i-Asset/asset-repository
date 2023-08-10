package at.srfg.iasset.repository.model;

import java.time.Duration;
import java.util.List;

public interface InvocationRequest<T> {

	void setDuration(Duration duration);

	Duration getDuration();

	default void setDuration(long timeInMillis) {
		setDuration(Duration.ofMillis(timeInMillis));
	}

	void setInputArguments(List<T> inputVariables);

	List<T> getInputArguments();

	void setInoutputArguments(List<T> inoutputVariables);

	List<T> getInoutputArguments();
	
	default T getInputArgument(int index) {
		return getInputArguments().get(index);
	}
	default T getInoutputArgument(int index) {
		return getInoutputArguments().get(index);
	}
	default InvocationRequest<T> inputArgument(T input) {
		getInputArguments().add(input);
		return this;
	}
	default InvocationRequest<T> inoutputArgument(T input) {
		getInoutputArguments().add(input);
		return this;
	}
	default InvocationRequest<T> inputArguments(List<T> input) {
		setInputArguments(input);
		return this;
	}
	default InvocationRequest<T> inoutputArguments(List<T> input) {
		setInoutputArguments(input);
		return this;
	}
	default InvocationRequest<T> duration(long timeInMillis) {
		setDuration(timeInMillis);
		return this;
	}
}
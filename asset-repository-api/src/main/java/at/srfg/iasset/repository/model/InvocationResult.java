package at.srfg.iasset.repository.model;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.OperationVariable;
/**
 * Interface handling invocation results. 
 * 
 * @param <T> The type of the data payload: Either {@link OperationVariable} or {@link Object}
 */
public interface InvocationResult<T> {
	/**
	 * Setter for the output arguments
	 * @param outputArguments
	 */
	void setOutputArguments(List<T> outputArguments);
	/**
	 * Getter for the <code>output</code> arguments, must not return null
	 */
	List<T> getOutputArguments();
	/**
	 * Setter for the inoutputArguments
	 * @param inoutpuArguments
	 */
	void setInoutputArguments(List<T> inoutputArguments);
	/**
	 * Getter for the <code>inoutput</code> arguments, must not return null
	 * @return
	 */
	List<T> getInoutputArguments();
	/**
	 * Getter for the <code>output</code> argument element at the provided index
	 * @param index
	 * @return
	 */
	default T getOutputArgument(int index) {
		try {
			return getOutputArguments().get(index);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Getter for the <code>inoutput</code> argument element at the provided index
	 * @param index
	 * @return
	 */
	default T getInoutputArgument(int index) {
		try {
			return getInoutputArguments().get(index);
		} catch (Exception e) {
			return null;
		}	
	}
	/**
	 * Convenience method for adding an <code>output</code> arguments
	 * @param output The output argument
	 * @return {@link InvocationResult} <code>this</code>
	 */
	default InvocationResult<T> outputArgument(T output) {
		getOutputArguments().add(output);
		return this;
	}
	/**
	 * Convenience method for adding an <code>inoutput</code> argument
	 * @param inoutput The inoutput argument
	 * @return {@link InvocationResult} <code>this</code>
	 */
	default InvocationResult<T> inoutputArgument(T inoutput) {
		getInoutputArguments().add(inoutput);
		return this;
	}
	/**
	 * Convenience method for setting the <code>inoutput</code> arguments
	 * @param inout
	 * @return
	 */
	default InvocationResult<T> inoutputArguments(List<T> inout) {
		setInoutputArguments(inout);
		return this;
	}
	/**
	 * Convenience method for setting the <code>inoutput</code> arguments
	 * @param inout
	 * @return
	 */
	default InvocationResult<T> outputArguments(List<T> out) {
		setOutputArguments(out);
		return this;
	}

}
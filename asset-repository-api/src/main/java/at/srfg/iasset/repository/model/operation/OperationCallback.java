package at.srfg.iasset.repository.model.operation;

import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;

/**
 * Simple Interface for providing callback implementations
 * 
 * @author dglachs
 *
 */
public interface OperationCallback {
	/**
	 * Execute the desired method. The method receives an 
	 * {@link OperationInvocation} object providing access
	 * to the input/output parameters: 
	 * <ul>
	 * <li>{@link OperationInvocation#getInput(Object)} (only for operations with exactly one input parameter
	 * <li>{@link OperationInvocation#getInput(String, Object)} for accessing the named variables
	 * <li>{@link OperationInvocation#setOutput(Object)} for setting the named result variable
	 * <li>{@link OperationInvocation#setOutput(String, Object)} for setting the named result variable
	 * </ul>
	 * Implementors may first access the input parameters, execute the requested tasks and should 
	 * then update the output parameters. 
	 * @param invocation The {@link OperationInvocation} object
	 * @return <code>true</code> if operation execution was successful, <code>false</code> otherwise
	 */
	public boolean execute(OperationInvocation invocation) throws OperationInvocationException;

}

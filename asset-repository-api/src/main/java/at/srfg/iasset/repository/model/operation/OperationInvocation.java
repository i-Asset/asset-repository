package at.srfg.iasset.repository.model.operation;

import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;

import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;
/**
 * Interface for operation handling!
 * @author dglachs
 *
 */
public interface OperationInvocation {
	/**
	 * Provide access to the {@link Operation} element 
	 * @return
	 */
	Operation getOperation();
	/**
	 * Set a new value to an Input or Inoutput-Variable of the operation!
	 * <p>Note: This method is only applicable to operations with exactly one
	 * input parameter!
	 * </p>
	 * 
	 * @param <T>
	 * @param value The new value
	 * @throws ValueMappingException 
	 */
	<T> void setInput(T value);
	/**
	 * Set a new value to an Input or Inoutput-Variable of the operation!
	 * 
	 * @param <T>
	 * @param value The new value
	 * @return The actual {@link OperationInvocation} for chaining 
	 */
	<T> OperationInvocation setInput(String idShort, T value);
	/**
	 * Obtain the value of the single Input- or Inoutput-Variable!
	 * <p>Note: This method is only applicable to operations with exactly one
	 * input parameter!
	 * </p>
	 * 
	 * @param <T>
	 * @param clazz The type of the (expected) value
	 * @return The value of the input parameter (Type-Safe)
	 */
	<T> T getInput(Class<T> clazz);
	/**
	 * Obtain the value of the named Input- or Inoutput-Variable
	 * @param idShort The idShort of the {@link OperationVariable}'s value element 
	 * @param clazz The type of the (expected) value
	 * @return The value of the input parameter (Type-Safe)
	 */ 
	<T> T getInput(String idShort, Class<T> clazz);
	/**
	 * Store the result of the method invocation in the single output variable
	 * <p>Note: This method is only applicable to operations with exactly one
	 * input parameter!
	 * </p>
	 * @param <T>
	 * @param value The new value of the output variable
	 */
	<T> void setOutput(T value);
	/**
	 * Store the result of the method invocation in the single output variable
	 * <p>Note: This method is only applicable to operations with exactly one
	 * input parameter!
	 * </p>
	 * @param <T>
	 * @param value The new value of the output variable
	 * @return 
	 */
	<T> OperationInvocation setOutput(String idShort, T value);
//	/**
//	 * Execute the operation with the remote asset administration shell
//	 * @return
//	 */
//	OperationInvocationResult invoke();
	/**
	 * Execute the operation with the identified shell, submodel and path
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return
	 */
	OperationInvocationResult invoke(String aasIdentifier, String submodelIdentifier, String path);
	/**
	 * Execute the operation, it is up to the semantic middleware to 
	 * identify the responsible AAS instance.
	 * @return
	 * @throws OperationInvocationException 
	 */
	OperationInvocationResult invoke() throws OperationInvocationException;
	/**
	 * Execute the operation with the named aasIdentifier 
	 * @param aasIdentifier
	 * @param reference
	 * @return
	 */
	OperationInvocationResult invoke(String aasIdentifier, Reference reference);
}

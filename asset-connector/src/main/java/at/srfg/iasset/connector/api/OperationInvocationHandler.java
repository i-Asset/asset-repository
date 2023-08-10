package at.srfg.iasset.connector.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.OperationVariable;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;

public class OperationInvocationHandler implements OperationInvocation, OperationInvocationResult {
	
	private final Operation operation;
	private final ServiceEnvironment serviceEnvironment;
	private ObjectMapper objectMapper;
	
	
	public OperationInvocationHandler(Operation operation, ServiceEnvironment environment, ObjectMapper mapper) {
		this.operation = operation;
		this.serviceEnvironment = environment;
		this.objectMapper = mapper;
		
	}
	/**
	 * Apply the incoming request to the operation!
	 * @param request
	 */
	public void applyOperationRequestValue(OperationRequestValue request) {
		// map all requested input arguments
		int inputSize = Math.min(operation.getInputVariables().size(), request.getInputArguments().size());
		for (int i = 0; i < inputSize;i++) {
			// map the current request to the operation's settings
			applyParameter(operation.getInputVariables().get(i).getValue(), request.getInputArgument(i));
		}
		// map all requested output arguments 
		int inoutputSize = Math.min(operation.getInoutputVariables().size(), request.getInoutputArguments().size());
		for (int i = 0; i < inoutputSize;i++) {
			// map the current request to the operation's settings
			applyParameter(operation.getInoutputVariables().get(i).getValue(), request.getInoutputArgument(i));
		}
	}
	public void applyOperationRequest(OperationRequest request) {
		operation.setInputVariables(request.getInputArguments());
		operation.setInoutputVariables(request.getInoutputArguments());
	}
	public void applyOperationResultValue(OperationResultValue result) {
		int inputSize = Math.min(operation.getOutputVariables().size(), result.getOutputArguments().size());
		for (int i = 0; i < inputSize;i++) {
			// map the current request to the operation's settings
			applyParameter(operation.getOutputVariables().get(i).getValue(), result.getOutputArgument(i));
		}
		// map all requested output arguments 
		int inoutputSize = Math.min(operation.getInoutputVariables().size(), result.getInoutputArguments().size());
		for (int i = 0; i < inoutputSize;i++) {
			// map the current request to the operation's settings
			applyParameter(operation.getInoutputVariables().get(i).getValue(), result.getInputArgument(i));
		}
	}
	private OperationRequestValue getOperationRequestValue() {
		OperationRequestValue resultValue = new OperationRequestValue();
		for (OperationVariable variable : operation.getInoutputVariables()) {
			SubmodelElement modelElement = variable.getValue();
			resultValue.inoutputArgument(ValueHelper.toValue(modelElement));
		}
		for (OperationVariable variable : operation.getInputVariables()) {
			SubmodelElement modelElement = variable.getValue();
			resultValue.inputArgument(ValueHelper.toValue(modelElement));
		}
		return resultValue;
	}
	public OperationResultValue getOperationResultValue() {
		OperationResultValue resultValue = new OperationResultValue();
		for (OperationVariable variable : operation.getInoutputVariables()) {
			SubmodelElement modelElement = variable.getValue();
			resultValue.inoutputArgument(ValueHelper.toValue(modelElement));
		}
		for (OperationVariable variable : operation.getOutputVariables()) {
			SubmodelElement modelElement = variable.getValue();
			resultValue.outputArgument(ValueHelper.toValue(modelElement));
		}
		return resultValue;
	}
	public OperationResult getOperationResult() {
		OperationResult resultValue = new OperationResult();
		resultValue.setInoutputArguments(operation.getInoutputVariables());
		resultValue.setOutputArguments(operation.getOutputVariables());
		return resultValue;
	}
	public <T> void setInput(T param) {
		switch (countInputVariables()) {
		case 0: // no input variables 
			throw new IllegalArgumentException("No input variable defined! ");
		case 1: // only one input variable
			Optional<OperationVariable> iv = findInputVariable(null);
			// apply value 
			if ( iv.isPresent()) {
				// applies the value in the operation's model hierarchy
				applyParameter(iv.get().getValue(), param);
				// 
			}
			break;
		default:
			// multiple input parameters are present
			throw new IllegalStateException("Multiple InputVariables present, use setParameter(key, value)");
		}
	}
	public <T> OperationInvocation setInput(String key, T param) {
		switch (countInputVariables()) {
		case 0: // no input variables 
			throw new IllegalArgumentException("No input variable defined! ");
		default: // only one input variable
			Optional<OperationVariable> iv = findInputVariable(key);
			// apply value 
			if ( iv.isPresent()) {
				// validate the provided value
				applyParameter(iv.get().getValue(), param);
				// 
			}
			break;
		}
		return this;
	}
//	/**
//	 * Map the collected data to an {@link OperationRequest}
//	 */
//	public OperationResultValue getValueResult() {
//		//
//		return getOperationResultValue();
//	}

	/**
	 * Invoke the Operation with the provided asset identifier!
	 * @param assetIdentifier The identifier of the AAS responsible for execution
	 * @return the result of the invocation
	 */
	
//	public MethodInvocationResult invoke(String assetIdentifier) {
//		OperationRequestValue valueRequest = new OperationRequestValue(operation, objectMapper);
//		Optional<AssetAdministrationShell> shell = serviceEnvironment.getAssetAdministrationShell(assetIdentifier);
//		if ( shell.isPresent()) {
//			// execute the request with the identified asset!
//			Object result = serviceEnvironment.invokeOperation(assetIdentifier, assetIdentifier, assetIdentifier, valueRequest);
//		}
//		return null;
//	}
	private <T> void applyParameter(SubmodelElement submodelElement, T value) {
		// a standard object mapper is sufficient for this conversion!
		JsonNode valueAsNode = objectMapper.convertValue(value, JsonNode.class);
		// need to validate the input value with the model 
		ValueHelper.applyValue(serviceEnvironment, submodelElement, valueAsNode);
	}

	private List<OperationVariable> inputVariables() {
		List<OperationVariable> input = new ArrayList<>();
		input.addAll(operation.getInputVariables());
		input.addAll(operation.getInoutputVariables());
		return input;
	}
	private List<OperationVariable> outputVariables() {
		List<OperationVariable> output = new ArrayList<>();
		output.addAll(operation.getOutputVariables());
		output.addAll(operation.getInoutputVariables());
		return output;
	}
	private Optional<OperationVariable> findInputVariable(String idShort) {
		switch (countInputVariables()) {
		case 0:
			throw new IllegalStateException("No output variable is present!");
		case 1: 
			if ( idShort == null) {
				return inputVariables().stream().findFirst();
			}
			// no break!
		default:
			if ( idShort == null) {
				throw new IllegalStateException("Multiiple output variables are present present!");
			}
			return inputVariables().stream().filter(new Predicate<OperationVariable>() {
				@Override
				public boolean test(OperationVariable t) {
					return t.getValue().getIdShort().equalsIgnoreCase(idShort);
				}
			})
			.findFirst();
			
		}
	}
	private Optional<OperationVariable> findOutputVariable(String idShort) {
		switch (countOutputVariables()) {
		case 0:
			throw new IllegalStateException("No output variable is present!");
		case 1: 
			if ( idShort == null) {
				return outputVariables().stream().findFirst();
			}
			// no break!
		default:
			if ( idShort == null) {
				throw new IllegalStateException("Multiiple output variables are present present!");
			}
			return outputVariables().stream().filter(new Predicate<OperationVariable>() {
				@Override
				public boolean test(OperationVariable t) {
					return t.getValue().getIdShort().equalsIgnoreCase(idShort);
				}
			})
			.findFirst();
			
		}
	}

	private int countInputVariables() {
		return operation.getInoutputVariables().size() 
				+ operation.getInputVariables().size();
	}
	private int countOutputVariables() {
		return operation.getInoutputVariables().size() 
				+ operation.getOutputVariables().size();
	}
	@Override
	public Operation getOperation() {
		return operation;
	}
	@Override

	public <T> T getInput(String key, Class<T> clazz) {
		switch (countInputVariables()) {
		case 0: // no input variables 
			throw new IllegalArgumentException("No Input variable defined! ");
		default:
			Optional<OperationVariable> iv = findInputVariable(key);
			// apply value 
			if ( iv.isPresent()) {
				// 
				SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
				return objectMapper.convertValue(paramterValue, clazz);
			}
			break;
			// multiple input parameters are present
		}
		return null; 
	}

	public <T> T getInput(Class<T> clazz) {
		switch (countInputVariables()) {
		case 0: // no input variables 
			throw new IllegalArgumentException("No Input variable defined! ");
		case 1: // only one input variable
			Optional<OperationVariable> iv = findInputVariable(null);
			// apply value 
			if ( iv.isPresent()) {
				// 
				SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
				return objectMapper.convertValue(paramterValue, clazz);
			}
			break;
		default:
			// multiple input parameters are present
			throw new IllegalStateException("Multiple InputVariables present, use getResult(key, type)");
		}
		return null; 
	}
	@Override
	public <T> T getResult(Class<T> clazz) {
		return getResult(null, clazz);
	}
	@Override
	public <T> T getResult(String key, Class<T> clazz) {
		Optional<OperationVariable> iv = findOutputVariable(key);
		// apply value 
		if ( iv.isPresent()) {
			// 
			SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
			return objectMapper.convertValue(paramterValue, clazz);
		}
		return null; 
	}


	@Override
	public <T> void setOutput(T param) {
		setOutput(null, param);
	}
	@Override
	public <T> OperationInvocation setOutput(String key, T param) {
		Optional<OperationVariable> iv = findOutputVariable(key);
		// apply value 
		if ( iv.isPresent()) {
			// validate the provided value
			applyParameter(iv.get().getValue(), param);
			// 
		}
		return this;
	}	
	@Override
	public Object getResult() {
		return getResult(null, Object.class);
	}
	@Override
	public Object getResult(String idShort) {
		return getResult(idShort, Object.class);
	}
	@Override
	public <T> List<T> getResultList(Class<T> clazz) {
		return getResultList(null, clazz);
	}
	@Override
	public <T> List<T> getResultList(String idShort, Class<T> clazz) {
		Optional<OperationVariable> iv = findOutputVariable(idShort);
		// apply value 
		if ( iv.isPresent()) {
			//			
			SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
			return objectMapper.convertValue(
					paramterValue, 
					objectMapper.getTypeFactory().constructCollectionLikeType(List.class, clazz)
				);
		}
		return new ArrayList<>(); 
	}
	@Override
	public OperationInvocationResult invoke() {
		// 
		OperationResultValue result = serviceEnvironment.invokeOperationValue(null, null, null, getOperationRequestValue());
		applyOperationResultValue(result);

		
		return this;
//		serviceEnvironment.invokeOperation(null, null, null, objectMapper)
	}
	@Override
	public OperationInvocationResult invoke(String aasIdentifier, String submodelIdentifier, String path) {
		OperationResultValue result = serviceEnvironment.invokeOperationValue(aasIdentifier, submodelIdentifier, path, getOperationRequestValue());
		applyOperationResultValue(result);

		
		return this;
	}
	
}

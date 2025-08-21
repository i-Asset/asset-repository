package at.srfg.iasset.connector.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.HasKind;
import org.eclipse.digitaltwin.aas4j.v3.model.ModellingKind;
import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.api.model.Message;
import at.srfg.iasset.repository.api.model.MessageType;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
/**
 * Handler covering the execution of operations
 */
public class OperationInvocationHandler implements OperationInvocation, OperationInvocationResult {
	
	private final Operation operation;
	private final ServiceEnvironment serviceEnvironment;
//	private final IAssetAdministrationShellInterface shellInterface;
	private final ConnectionProvider connectionProvider;
	private final String submodelIdentifier;
	private final String pathToOperation;
	
	private ObjectMapper objectMapper;
	
	public OperationInvocationHandler(
		InstanceOperation operation,
		ServiceEnvironment environment, 
		ObjectMapper mapper) {
		this(null, null, null, operation, environment, mapper);
	}

	public OperationInvocationHandler(
			ConnectionProvider shellConnection,
			String submodelIdentifier, 
			String pathToOperation,
			Operation operation,
			ServiceEnvironment environment, 
			ObjectMapper mapper) {
		this.connectionProvider = shellConnection;
		this.submodelIdentifier = submodelIdentifier;
		this.pathToOperation = pathToOperation;
		this.operation = operation;
		this.serviceEnvironment = environment;
		this.objectMapper = mapper;
	}
	private boolean isTemplate(HasKind hasKind) {
		return ModellingKind.TEMPLATE.equals(hasKind.getKind());
	}
	/**
	 * Apply the incoming request to the operation!
	 * @param request
	 * @throws ValueMappingException 
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
	private void applyOperationResultValue(OperationResultValue result) {
		int inputSize = Math.min(operation.getOutputVariables().size(), result.getOutputArguments().size());
		for (int i = 0; i < inputSize;i++) {
			// map the current request to the operation's settings
			applyParameter(operation.getOutputVariables().get(i).getValue(), result.getOutputArgument(i));
		}
		// map all requested output arguments 
		int inoutputSize = Math.min(operation.getInoutputVariables().size(), result.getInoutputArguments().size());
		for (int i = 0; i < inoutputSize;i++) {
			// map the current request to the operation's settings
			applyParameter(operation.getInoutputVariables().get(i).getValue(), result.getInoutputArgument(i));
		}
	}
	private OperationRequestValue getOperationRequestValue() throws ValueMappingException {
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
	public OperationResultValue getOperationResultValue(boolean success) {
		OperationResultValue resultValue = new OperationResultValue();
		try {
			for (OperationVariable variable : operation.getInoutputVariables()) {
				SubmodelElement modelElement = variable.getValue();
				resultValue.inoutputArgument(ValueHelper.toValue(modelElement));
			}
			for (OperationVariable variable : operation.getOutputVariables()) {
				SubmodelElement modelElement = variable.getValue();
				resultValue.outputArgument(ValueHelper.toValue(modelElement));
			}
			resultValue.setSuccess(success);
			return resultValue;
			
		} catch (ValueMappingException e) {
			// create the result with success false
			resultValue.setSuccess(false);
			Message message = new Message();
			message.setMessageType(MessageType.ERROR);
			message.setText(e.getLocalizedMessage());
			message.setTimestamp(Instant.now().toString());
			resultValue.addMessagesItem(message);
			return resultValue;
		}
	}
	public OperationResult getOperationResult(boolean success) {
		OperationResult resultValue = new OperationResult();
		resultValue.setInoutputArguments(operation.getInoutputVariables());
		resultValue.setOutputArguments(operation.getOutputVariables());
		resultValue.setSuccess(success);
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
	public OperationInvocation setInput(Object ...params) throws ValueMappingException {
		for ( int i = 0; i< Math.min(params.length, countInputVariables()); i++) {
			OperationVariable v = inputVariables().get(i);
			applyParameter(v.getValue(), params[i]);
		}
		return this;
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
	/**
	 * Apply the provided parameter to the {@link SubmodelElement}
	 * @param <T>
	 * @param submodelElement The element reflecting the provided object
	 * @param value The (typed) value object
	 * @throws ValueMappingException 
	 */
	private <T> void applyParameter(SubmodelElement submodelElement, T value) {
		// 
		try {
		JsonNode valueAsNode = objectMapper.convertValue(value, JsonNode.class);
		// need to validate the input value with the model
		ValueHelper.applyValue(serviceEnvironment, submodelElement, valueAsNode);
		} catch (ValueMappingException e) {
//			throw new OperationInvocationException(e.getMessage());
		}
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
				try {
					SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
					return objectMapper.convertValue(paramterValue, clazz);
				} catch (ValueMappingException e) {
					
				}
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
				try {
					SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
					return objectMapper.convertValue(paramterValue, clazz);
				} catch (ValueMappingException e) {
					
				}
				// 
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
	public <T> T getResult(String key, Class<T> clazz){
		Optional<OperationVariable> iv = findOutputVariable(key);
		// apply value 
		if ( iv.isPresent()) {
			// 
			try {
				SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
				return objectMapper.convertValue(paramterValue, clazz);
			} catch (ValueMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null; 
	}


	@Override
	public <T> void setOutput(T param) {
		setOutput(null, param);
	}
	@Override
	public <T> OperationInvocation setOutput(String key, T param)  {
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
	public Object getResult()  {
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
			try {
				SubmodelElementValue paramterValue = ValueHelper.toValue(iv.get().getValue());
				return objectMapper.convertValue(
						paramterValue, 
						objectMapper.getTypeFactory().constructCollectionLikeType(List.class, clazz)
						);
				
			} catch (ValueMappingException e) {
				
			}
		}
		return new ArrayList<>(); 
	}

	@Override
	public OperationInvocationResult invoke(String aasIdentifier, String submodelIdentifier, String path) {
		try {
			OperationResultValue result = serviceEnvironment.invokeOperationValue(aasIdentifier, submodelIdentifier, path, getOperationRequestValue());
			applyOperationResultValue(result);
		} catch (ValueMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return this;
	}
	@Override
	public OperationInvocationResult invoke(String aasIdentifier, Reference reference) {
		
		return this;
	}
	@Override
	public OperationInvocationResult invoke() {
		if ( connectionProvider == null) {
			throw new IllegalStateException("Wrong usage! Use full constructor!");
		}
		try {
			OperationResultValue result = connectionProvider.getShellInterface().invokeOperation(submodelIdentifier, pathToOperation, getOperationRequestValue());
			applyOperationResultValue(result);
		} catch (ValueMappingException e) {
			OperationResultValue result = new OperationResultValue();
			result.setSuccess(false);
		}

		
		return this;
//		throw new UnsupportedOperationException("Not yet implemented!");
	}
	
}

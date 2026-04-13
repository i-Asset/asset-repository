package at.srfg.iasset.connector.api;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import at.srfg.iasset.repository.model.operation.exception.OperationInvocationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.digitaltwin.aas4j.v3.model.*;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationRequestValue;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationResult;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationResultValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
/**
 * Handler covering the execution of operations
 */
public class OperationInvocationHandler implements OperationInvocation, OperationInvocationResult {

	private static final Logger log = LoggerFactory.getLogger(OperationInvocationHandler.class);
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
		for (OperationVariable variable : operation.getInputVariables()) {
			if (!hasIdShort(variable)) {
				log.error("idShort not found in operation input variable {}", variable);
				throw new IllegalStateException("idShort missing in operation variable of " + operation);
			}
			Optional<Object> value = Optional.ofNullable(request.getInputArguments().get(variable.getValue().getIdShort()));
			if ( value.isPresent()) {
				applyParameter(variable.getValue(), value.get());
			}
		}
		for (OperationVariable variable : operation.getInoutputVariables()) {
			if (!hasIdShort(variable)) {
				log.error("idShort not found in operation inoutput variable {}", variable);
				throw new IllegalStateException("idShort missing in operation variable of " + operation);
			}
			Optional<Object> value = Optional.ofNullable(request.getInoutputArguments().get(variable.getValue().getIdShort()));
			if ( value.isPresent()) {
				applyParameter(variable.getValue(), value.get());
			}
		}
	}
	public void applyOperationRequest(OperationRequest request) {
		operation.setInputVariables(request.getInputArguments());
		operation.setInoutputVariables(request.getInoutputArguments());
	}

	private void applyOperationResultValue(OperationResultValue result) {
		for (OperationVariable variable : operation.getOutputVariables()) {
			if (!hasIdShort(variable)) {
				log.error("idShort not found in operation output variable {}", variable);
				throw new IllegalStateException("idShort missing in operation variable of " + operation);
			}
			Optional<Object> value = Optional.ofNullable(result.getOutputArguments().get(variable.getValue().getIdShort()));
			if ( value.isPresent()) {
				applyParameter(variable.getValue(), value.get());
			}
		}
		for (OperationVariable variable : operation.getInoutputVariables()) {
			Optional<Object> value = Optional.ofNullable(result.getInoutputArguments().get(variable.getValue().getIdShort()));
			if ( value.isPresent()) {
				applyParameter(variable.getValue(), value.get());
			}
		}
	}
	private OperationRequestValue getOperationRequestValue() throws ValueMappingException {
		Map<String, Object> inout = new HashMap<>();
		for (OperationVariable variable : operation.getInoutputVariables()) {
			SubmodelElementValue value = ValueHelper.toValue(variable.getValue());
			if ( value != null) {
				inout.put(variable.getValue().getIdShort(), value);
			}
		}
		Map<String, Object> input = new HashMap<>();
		for (OperationVariable variable : operation.getInputVariables()) {
			SubmodelElementValue value = ValueHelper.toValue(variable.getValue());
			if ( value != null) {
				input.put(variable.getValue().getIdShort(), value);
			}
		}
		return new DefaultOperationRequestValue.Builder()
				.inoutputArguments(inout)
				.inputArguments(input)
				.build();
	}
	public OperationResultValue getOperationResultValue(boolean success) throws ValueMappingException {
		Map<String, Object> inout = new HashMap<>();
		for (OperationVariable variable : operation.getInoutputVariables()) {
			SubmodelElementValue value = ValueHelper.toValue(variable.getValue());
			if ( value != null) {
				inout.put(variable.getValue().getIdShort(), value);
			}
		}
		Map<String, Object> output = new HashMap<>();
		for (OperationVariable variable : operation.getOutputVariables()) {
			SubmodelElementValue value = ValueHelper.toValue(variable.getValue());
			if ( value != null) {
				output.put(variable.getValue().getIdShort(), value);
			}
		}

		return new DefaultOperationResultValue.Builder()
				.success(success)
				.outputArguments(output)
				.inoutputArguments(inout)
				.build();
	}
	public OperationResult getOperationResult(boolean success) {
		OperationResult resultValue = new DefaultOperationResult.Builder()
				.success(true)
				.inoutputArguments(operation.getInoutputVariables())
				.outputArguments(operation.getOutputVariables())
				.build();
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
	 */
	private <T> void applyParameter(SubmodelElement submodelElement, T value) {
		//
		try {
			JsonNode valueAsNode = objectMapper.convertValue(value, JsonNode.class);
			// need to validate the input value with the model
			Reference semanticId = submodelElement.getSemanticId();
	//		Optional<SubmodelElement> templated = serviceEnvironment.resolve(semanticId, SubmodelElement.class);
			if ( semanticId.getType() == ReferenceTypes.MODEL_REFERENCE) {
				ValueHelper.applyValue(serviceEnvironment, submodelElement, semanticId, valueAsNode);
			} else {
				ValueHelper.applyValue(serviceEnvironment, submodelElement, valueAsNode);
			}
		} catch (ValueMappingException e) {
			log.error("applying value {} failed", value, e);
			throw new IllegalArgumentException(e.getMessage(), e);
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
			throw new IllegalStateException("No input variable is present!");
		case 1:
			if ( idShort == null) {
				return inputVariables().stream().findFirst();
			}
			// no break!
		default:
			if ( idShort == null) {
				throw new IllegalStateException("Multiple input variables are present!");
			}
			return inputVariables().stream().filter(new Predicate<OperationVariable>() {
				@Override
				public boolean test(OperationVariable in) {
					try {
						if (!hasIdShort(in)) {
							log.error("idShort not found in operation variable {}", in);
							throw new IllegalStateException("idShort missing in operation variable of " + operation);
						}
						return in.getValue().getIdShort().equalsIgnoreCase(idShort);
					} catch (Exception e) {
						throw new IllegalStateException("input variable not found " + idShort);
					}
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
				throw new IllegalStateException("Multiple output variables are present!");
			}
			return outputVariables().stream().filter(new Predicate<OperationVariable>() {
				@Override
				public boolean test(OperationVariable out) {
					try {
						if (!hasIdShort(out)) {
							log.error("idShort not found in operation variable {}", out);
							throw new IllegalStateException("idShort missing in operation variable of " + operation);
						}
						return out.getValue().getIdShort().equalsIgnoreCase(idShort);
					} catch (Exception e) {
						throw new IllegalStateException("output variable not found " + idShort);
					}
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
	public <T> OperationInvocation setOutput(String idShort, T param)  {
		Optional<OperationVariable> iv = findOutputVariable(idShort);
		// apply value
		if ( iv.isPresent()) {
			log.debug("applying value {} to parameter {}", iv, param.getClass());
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
	public OperationInvocationResult invoke() throws OperationInvocationException {
		if ( connectionProvider == null) {
			throw new IllegalStateException("Wrong usage! Use full constructor!");
		}
		try {
			OperationResultValue result = connectionProvider.getShellInterface().invokeOperation(submodelIdentifier, pathToOperation, getOperationRequestValue());
			if ( result.getSuccess()) {
				applyOperationResultValue(result);
			}
			else {
				List<String> messages =
				result.getMessages().stream().map(new Function<Message, String>() {

					@Override
					public String apply(Message t) {
						return t.getText();
					}
				}).toList();
				throw new OperationInvocationException(messages.toString());

			}
		} catch (ValueMappingException e) {
			throw new OperationInvocationException(e.getMessage());
		}


		return this;
//		throw new UnsupportedOperationException("Not yet implemented!");
	}
	@Override
	public Optional<SubmodelElement> getResultVariable(String idShort) {
		Optional<OperationVariable> ov = findOutputVariable(idShort);
		if ( ov.isPresent() ) {
			return Optional.of(ov.get().getValue());
		}
		return Optional.empty();
	}

	private static boolean hasIdShort(OperationVariable variable) {
		return variable != null && variable.getValue() != null && variable.getValue().getIdShort() != null && !variable.getValue().getIdShort().isBlank();
	}

}

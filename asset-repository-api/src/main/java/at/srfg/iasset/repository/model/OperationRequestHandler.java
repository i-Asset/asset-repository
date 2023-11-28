//package at.srfg.iasset.repository.model;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//import org.eclipse.aas4j.v3.model.Operation;
//import org.eclipse.aas4j.v3.model.OperationVariable;
//import org.eclipse.aas4j.v3.model.SubmodelElement;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import at.srfg.iasset.repository.model.helper.ValueHelper;
//import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
//import at.srfg.iasset.repository.model.operation.OperationRequest;
//import at.srfg.iasset.repository.model.operation.OperationResult;
//
//public class OperationRequestHandler<T> {
//	
//	private final Operation theOperation;
//	
//	private ObjectMapper objectMapper;
//	
//	private final Map<String, Object> inputParameter = new HashMap<>();
//	private Map<String, Object> outputParameter;
//	
//	public OperationRequestHandler(Operation operation) {
//		this.theOperation = operation;
//		
//	}
//	public void setParameter(String key, Object value) {
//		//
//		Optional<OperationVariable> input = findInputVariable(key);
//		if ( input.isPresent() ) {
//			try {
//				// 1. transform to string
//				String strValue = objectMapper.writeValueAsString(value);
//				// 2. readTree into JsonNode
//				JsonNode tree = objectMapper.readTree(strValue);
//				// 3. apply Value to OperationVariable's value (e.g. a SubmodelElement
//				ValueHelper.applyValue(input.get().getValue(), tree);
//				
//			} catch (Exception e) {
//				
//			}
//			// depending on the communication style, use full or reduced
//		}
//		else {
//			throw new IllegalArgumentException(String.format("The input variable '%s' is not defined!", key));
//		}
//	}
//	protected InvocationRequest<T> createRequest() {
//		return new OperationRequest()
//				.inputArguments(theOperation.getInputVariables())
//				.inoutputArguments(theOperation.getInoutputVariables())
//				;
//	}
//	protected OperationRequest createRequest(long timeOutInMillis) {
//		return new OperationRequest()
//				.inputArguments(theOperation.getInputVariables())
//				.inoutputArguments(theOperation.getInoutputVariables())
//				.timeout(timeOutInMillis)
//				;
//	}
//	protected OperationRequestValue createValueRequest() {
//		
//		List<SubmodelElementValue> inputValues = theOperation.getInputVariables()
//			.stream()
//			.map(new Function<OperationVariable, SubmodelElementValue>() {
//
//				@Override
//				public SubmodelElementValue apply(OperationVariable t) {
//					// TODO Auto-generated method stub
//					return ValueHelper.toValue(t.getValue());
//				}})
//			.collect(Collectors.toList());
//			;
//		List<SubmodelElementValue> inoutputValues = theOperation.getInoutputVariables()
//				.stream()
//				.map(new Function<OperationVariable, SubmodelElementValue>() {
//
//					@Override
//					public SubmodelElementValue apply(OperationVariable t) {
//						// TODO Auto-generated method stub
//						return ValueHelper.toValue(t.getValue());
//					}})
//				.collect(Collectors.toList());
//				;
//		
//		return new OperationRequestValue()
//				.inputArguments(inputValues)
//				.inoutputArguments(inoutputValues)
//				;
//	}
//	protected void consumeResult(OperationResult result) {
//		theOperation.setOutputVariables(result.getOutputArguments());
//	}
//
//	/**
//	 * 
//	 * @param <T>
//	 * @param key
//	 * @param clazz
//	 * @return
//	 */
//	public <T> T getOutputParameter(String key, Class<T> clazz) {
//		if ( outputParameter == null) {
//			throw new IllegalStateException("Operation not yet executed!");
//		}
//		Optional<OperationVariable> output = findOutputVariable(key);
//		if ( output.isPresent()) {
//			return new ObjectMapper().convertValue(output.get().getValue(), clazz);
//		}
//		return null;
//	}
//	private Optional<? extends SubmodelElement> findInput(String key, Object value) {
//		List<OperationVariable> input = new ArrayList<>();
//		input.addAll(theOperation.getInputVariables());
//		input.addAll(theOperation.getInoutputVariables());
//		if ( input.size() > 0 ) {
//			Optional<OperationVariable> variable = input.stream().filter(new Predicate<OperationVariable>() {
//				@Override
//				public boolean test(OperationVariable t) {
//					return t.getValue().getIdShort().equalsIgnoreCase(key);
//				}
//			})
//			.findFirst();
//			if ( variable.isPresent()) {
////				return Optional.of(variable.get());
//			}
//		}
//		return Optional.empty();
//	}
//	private Optional<OperationVariable> findInputVariable(String key) {
//		List<OperationVariable> input = new ArrayList<>();
//		input.addAll(theOperation.getInputVariables());
//		input.addAll(theOperation.getInoutputVariables());
//		if ( input.size() > 0 ) {
//			return input.stream().filter(new Predicate<OperationVariable>() {
//				@Override
//				public boolean test(OperationVariable t) {
//					return t.getValue().getIdShort().equalsIgnoreCase(key);
//				}
//			})
//			.findFirst();
//		}
//		return Optional.empty();
//	}
//	private Optional<OperationVariable> findOutputVariable(String key) {
//		List<OperationVariable> input = new ArrayList<>();
//		input.addAll(theOperation.getOutputVariables());
//		input.addAll(theOperation.getInoutputVariables());
//		if ( input.size() > 0 ) {
//			return input.stream().filter(new Predicate<OperationVariable>() {
//				
//				@Override
//				public boolean test(OperationVariable t) {
//					return t.getValue().getIdShort().equalsIgnoreCase(key);
//				}
//			})
//			.findFirst();
//		}
//		return Optional.empty();
//	}
//
//
//}

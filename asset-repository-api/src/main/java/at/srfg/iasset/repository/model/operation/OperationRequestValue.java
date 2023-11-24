package at.srfg.iasset.repository.model.operation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationVariable;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.model.InvocationRequest;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;

public class OperationRequestValue extends AbstractInvocationRequest<Object> 
	implements InvocationRequest<Object> {
	public OperationRequestValue() {
		// default constructor
	}
	public OperationRequestValue(Operation operation, ObjectMapper mapper) {
		// 
		if ( operation.getInputVariables().size()>0) {
			List<Object> values = operation.getInputVariables()
					.stream()
					.map(new Function<OperationVariable, Object>(){

						@Override
						public Object apply(OperationVariable t) {
							SubmodelElementValue value = ValueHelper.toValue(t.getValue());
							// convert value
							return mapper.convertValue(value, Object.class);
						}})
					.collect(Collectors.toList());
			
			setInputArguments(values);
			
		}
//		setInputArguments(Collections.);
	}
}

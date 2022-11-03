package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.Range;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.RangeValue;
import at.srfg.iasset.repository.model.helper.value.type.Value;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;

public class RangeValueMapper implements ValueMapper<Range, RangeValue> {


	@Override
	public RangeValue<?> mapToValue(Range modelElement) {
		return  new RangeValue(
				ValueType.getValue(modelElement.getValueType(), modelElement.getMin()),
				ValueType.getValue(modelElement.getValueType(), modelElement.getMax()));
	}

	@Override
	public Range mapValueToElement(Range modelElement, JsonNode valueNode) {
		
		if (valueNode.hasNonNull("min")) {
			Value<?> typedValue = ValueType.getValue(modelElement.getValueType(), valueNode.get("min").asText());
			
			modelElement.setMin(typedValue.toString());
		}
		if ( valueNode.hasNonNull("max")) {
			Value<?> typedValue = ValueType.getValue(modelElement.getValueType(), valueNode.get("max").asText());
			
			modelElement.setMax(typedValue.toString());
		}
		return modelElement;
	}

}

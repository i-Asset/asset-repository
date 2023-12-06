package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.digitaltwin.aas4j.v3.model.Range;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.RangeValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.helper.value.type.Value;

public class RangeValueMapper implements ValueMapper<Range, RangeValue<?>> {


	@SuppressWarnings("unchecked")
	@Override
	public RangeValue<?> mapToValue(Range modelElement) throws ValueMappingException {
		return  new RangeValue(
				Value.getValue(modelElement.getValueType(), modelElement.getMin()),
				Value.getValue(modelElement.getValueType(), modelElement.getMax()));
	}

	@Override
	public Range mapValueToElement(Range modelElement, JsonNode valueNode) throws ValueMappingException {
		
		if (valueNode.hasNonNull("min")) {
			Value<?> typedValue = Value.getValue(modelElement.getValueType(), valueNode.get("min").asText());
			
			modelElement.setMin(typedValue.toString());
		}
		if ( valueNode.hasNonNull("max")) {
			Value<?> typedValue = Value.getValue(modelElement.getValueType(), valueNode.get("max").asText());
			
			modelElement.setMax(typedValue.toString());
		}
		return modelElement;
	}

}

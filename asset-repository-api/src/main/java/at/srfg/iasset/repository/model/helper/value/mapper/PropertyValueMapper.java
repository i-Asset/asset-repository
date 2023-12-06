package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.digitaltwin.aas4j.v3.model.Property;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.PropertyValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.helper.value.type.Value;

public class PropertyValueMapper implements ValueMapper<Property, PropertyValue>{


	@Override
	public PropertyValue mapToValue(Property modelElement) throws ValueMappingException {
		return new PropertyValue(Value.getValue(modelElement.getValueType(), modelElement.getValue()));
	}

	@Override
	public Property mapValueToElement(Property modelElement, JsonNode valueNode) throws ValueMappingException {
		if (! valueNode.isNull()) {
			Value<?> typedValue = Value.getValue(modelElement.getValueType(), valueNode.asText());
			modelElement.setValue(typedValue.toString());
		}
		// 
		return modelElement;
	}
}

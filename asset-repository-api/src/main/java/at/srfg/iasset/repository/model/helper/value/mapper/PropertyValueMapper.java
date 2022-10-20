package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.Property;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.PropertyValue;
import at.srfg.iasset.repository.model.helper.value.type.Value;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;

public class PropertyValueMapper implements ValueMapper<Property, PropertyValue>{

	@Override
	public PropertyValue getValueOnly(Property modelElement) {
		return new PropertyValue(ValueType.getValue(modelElement.getValueType(), modelElement.getValue()));
	}

	@Override
	public Property applyValue(Property modelElement, JsonNode valueNode) {
		Value<?> typedValue = ValueType.getValue(modelElement.getValueType(), valueNode.asText());
		modelElement.setValue(typedValue.toString());
		return modelElement;
	}


}

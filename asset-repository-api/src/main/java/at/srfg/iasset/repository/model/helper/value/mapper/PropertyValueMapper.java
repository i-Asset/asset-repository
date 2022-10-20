package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.Property;

import at.srfg.iasset.repository.model.helper.value.PropertyValue;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;

public class PropertyValueMapper implements ValueMapper<Property, PropertyValue>{

	@Override
	public PropertyValue getValueOnly(Property modelElement) {
		return new PropertyValue(ValueType.getValue(modelElement.getValueType(), modelElement.getValue()));
	}

	@Override
	public Property applyValues(Property modelElement, PropertyValue valueObject) {
		// 
		modelElement.setValue(valueObject.getValue().toString());
		return modelElement;
	}

}

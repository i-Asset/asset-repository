package at.srfg.iasset.repository.model.value;

import org.eclipse.aas4j.v3.model.DataTypeDefXsd;
import org.eclipse.aas4j.v3.model.Property;

public class PropertyValue extends DataElementValue<Property> {
	Value<?> typedValue; 
	
	public PropertyValue(Value<?> typedValue) {
		super(typedValue.getValueType());
		this.typedValue = typedValue;
	}
	public PropertyValue(DataTypeDefXsd dataType, String value) {
		super(ValueType.fromDataType(dataType));
		this.typedValue = ValueType.getValue(dataType, value);
	}
	public Value<?> getValue() {
		return typedValue;
	}
	public void setValue(Value<?> value) {
		this.typedValue = value;
	}
	
}

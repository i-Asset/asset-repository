package at.srfg.iasset.repository.model.helper.value;

import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;

import com.fasterxml.jackson.annotation.JsonValue;

import at.srfg.iasset.repository.model.helper.value.type.Value;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;

public class PropertyValue extends DataElementValue {
	@JsonValue
	Value<?> typedValue; 
	
	public PropertyValue(Value<?> typedValue) {
		this.typedValue = typedValue;
	}
	
	public static PropertyValue of(DataTypeDefXsd dataType, String value) {
		return new PropertyValue(ValueType.getValue(dataType, value));
	}
	
	public Value<?> getValue() {
		return typedValue;
	}
	
	public void setValue(Value<?> value) {
		this.typedValue = value;
	}
	
}

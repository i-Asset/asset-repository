package at.srfg.iasset.repository.model.helper.value;

import com.fasterxml.jackson.annotation.JsonValue;

import at.srfg.iasset.repository.model.helper.value.type.Value;

public class PropertyValue extends DataElementValue {
	@JsonValue
	Value<?> typedValue; 
	
	public PropertyValue(Value<?> typedValue) {
		this.typedValue = typedValue;
	}
	
	public Value<?> getValue() {
		return typedValue;
	}
	
	public void setValue(Value<?> value) {
		this.typedValue = value;
	}
	
}

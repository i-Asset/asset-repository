package at.srfg.iasset.repository.model.helper.value.type;

public class StringValue extends Value<String>{

	public StringValue() {
		
	}
	public StringValue(String value) {
		super(value);
	}

	@Override
	public Value<String> fromValue(String stringValue) {
		setValue(stringValue);
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.STRING;
	}

}

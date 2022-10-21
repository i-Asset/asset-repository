package at.srfg.iasset.repository.model.helper.value.type;

public class BooleanValue extends Value<Boolean>{

	public BooleanValue() {
		// default constructor
	}
	public BooleanValue(Boolean value) {
		super(value);
	}

	@Override
	public Value<Boolean> fromValue(String stringValue) {
		setValue(Boolean.parseBoolean(stringValue));
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.BOOLEAN;
	}

}

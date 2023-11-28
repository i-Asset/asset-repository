package at.srfg.iasset.repository.model.helper.value.type;

public class IntegerValue extends Value<Integer>{
	public IntegerValue() {}
	
	public IntegerValue(Integer value) {
		super(value);
	}

	@Override
	public Value<Integer> fromValue(String stringValue) {
		if ( stringValue != null) {
			setValue(Integer.parseInt(stringValue));
		}
		return this;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.INTEGER;
	}

}

package at.srfg.iasset.repository.model.helper.value.type;

public class LongValue extends Value<Long>{
	public LongValue() {}
	
	public LongValue(Long value) {
		super(value);
	}

	@Override
	public Value<Long> fromValue(String stringValue) {
		if ( stringValue != null) {
			setValue(Long.parseLong(stringValue));
		}
		return this;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.LONG;
	}

}

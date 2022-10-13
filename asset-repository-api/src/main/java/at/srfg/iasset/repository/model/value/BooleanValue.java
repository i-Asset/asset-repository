package at.srfg.iasset.repository.model.value;

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

}

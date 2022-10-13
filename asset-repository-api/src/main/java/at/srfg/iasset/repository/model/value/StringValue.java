package at.srfg.iasset.repository.model.value;

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

}

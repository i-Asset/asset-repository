package at.srfg.iasset.repository.model.value;

public class IntegerValue extends Value<Integer>{
	public IntegerValue() {}
	
	public IntegerValue(Integer value) {
		super(value);
	}

	@Override
	public Value<Integer> fromValue(String stringValue) {
		setValue(Integer.parseInt(stringValue));
		return this;
	}

}

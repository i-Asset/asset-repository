package at.srfg.iasset.repository.model.value;

public class DoubleValue extends Value<Double>{

	public DoubleValue() {} 
	
	public DoubleValue(Double value) {
		super(value);
	}

	@Override
	public Value<Double> fromValue(String stringValue) {
		setValue(Double.parseDouble(stringValue));
		return this;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.DOUBLE;
	}

}

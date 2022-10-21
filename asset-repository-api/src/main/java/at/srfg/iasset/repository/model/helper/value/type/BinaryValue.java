package at.srfg.iasset.repository.model.helper.value.type;

public class BinaryValue extends Value<byte[]>{

	public BinaryValue() {
		// default constructor
	}
	public BinaryValue(byte[] value) {
		super(value);
	}

	@Override
	public Value<byte[]> fromValue(String stringValue) {
		setValue(stringValue.getBytes());
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.BINARY;
	}
	

}

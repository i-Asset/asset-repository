package at.srfg.iasset.repository.model.value;

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

}

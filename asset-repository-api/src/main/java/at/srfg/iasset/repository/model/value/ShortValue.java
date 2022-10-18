package at.srfg.iasset.repository.model.value;

public class ShortValue extends Value<Short>{

	public ShortValue() {} 
	
	public ShortValue(Short value) {
		super(value);
	}

	@Override
	public Value<Short> fromValue(String stringValue) {
		setValue(Short.parseShort(stringValue));
		return this;
	}

}

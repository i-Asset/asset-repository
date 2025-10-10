package at.srfg.iasset.repository.model.helper.value.type;

public class DateTimeValue extends Value<XSDateTime>{

	public DateTimeValue() {
		
	}
	public DateTimeValue(XSDateTime value) {
		super(value);
	}

	@Override
	public Value<XSDateTime> fromValue(String stringValue) {
		if ( stringValue !=null ) {
			setValue(XSDateTime.parse(stringValue));
		}
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.DATE_TIME;
	}

}

package at.srfg.iasset.repository.model.helper.value.type;

import java.time.Instant;

public class DateTimeValue extends Value<Instant>{

	public DateTimeValue() {
		
	}
	public DateTimeValue(Instant value) {
		super(value);
	}

	@Override
	public Value<Instant> fromValue(String stringValue) {
		if ( stringValue !=null ) {
			setValue(Instant.parse(stringValue));
		}
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.DATE_TIME;
	}

}

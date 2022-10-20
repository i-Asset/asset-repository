package at.srfg.iasset.repository.model.helper.value.type;

import java.time.ZonedDateTime;

public class DateTimeValue extends Value<ZonedDateTime>{

	public DateTimeValue() {
		
	}
	public DateTimeValue(ZonedDateTime value) {
		super(value);
	}

	@Override
	public Value<ZonedDateTime> fromValue(String stringValue) {
		setValue(ZonedDateTime.parse(stringValue));
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.DATE_TIME;
	}

}

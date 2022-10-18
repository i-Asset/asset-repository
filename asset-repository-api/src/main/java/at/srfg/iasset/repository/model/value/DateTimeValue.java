package at.srfg.iasset.repository.model.value;

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

}

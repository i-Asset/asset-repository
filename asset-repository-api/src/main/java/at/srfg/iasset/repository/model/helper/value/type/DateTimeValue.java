package at.srfg.iasset.repository.model.helper.value.type;

import java.time.LocalDateTime;

public class DateTimeValue extends Value<LocalDateTime>{

	public DateTimeValue() {
		
	}
	public DateTimeValue(LocalDateTime value) {
		super(value);
	}

	@Override
	public Value<LocalDateTime> fromValue(String stringValue) {
		if ( stringValue !=null ) {
			setValue(LocalDateTime.parse(stringValue));
		}
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.DATE_TIME;
	}

}

package at.srfg.iasset.repository.model.helper.value.type;

import java.time.LocalDate;

public class DateValue extends Value<LocalDate>{

	public DateValue() {}
	
	public DateValue(LocalDate value) {
		super(value);
	}

	@Override
	public Value<LocalDate> fromValue(String stringValue) {
		setValue(LocalDate.parse(stringValue));
		return this;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.DATE;
	}

}

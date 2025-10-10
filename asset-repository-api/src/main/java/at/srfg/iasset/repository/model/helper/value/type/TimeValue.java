package at.srfg.iasset.repository.model.helper.value.type;

import java.time.LocalTime;

public class TimeValue extends Value<LocalTime>{

	public TimeValue() {}
	
	public TimeValue(LocalTime value) {
		super(value);
	}

	@Override
	public Value<LocalTime> fromValue(String stringValue) {
		setValue(LocalTime.parse(stringValue));
		return this;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.TIME;
	}

}

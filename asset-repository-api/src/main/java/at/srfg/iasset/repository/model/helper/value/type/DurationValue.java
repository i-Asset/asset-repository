package at.srfg.iasset.repository.model.helper.value.type;

import java.time.Duration;

public class DurationValue extends Value<Duration>{

	public DurationValue() {} 
	
	public DurationValue(Duration value) {
		super(value);
	}

	@Override
	public Value<Duration> fromValue(String stringValue) {
		if ( stringValue == null ) {
			setValue(null);
		}
		else {
			setValue(Duration.parse(stringValue));
		}
		return this;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.DURATION;
	}

}

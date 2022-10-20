package at.srfg.iasset.repository.model.helper.value.type;

import java.math.BigDecimal;

public class DecimalValue extends Value<BigDecimal>{

	public DecimalValue() {}
	public DecimalValue(BigDecimal value) {
		super(value);
	}

	@Override
	public Value<BigDecimal> fromValue(String stringValue) {
		setValue(new BigDecimal(stringValue));
		return this;
	}
	@Override
	public ValueType getValueType() {
		return ValueType.DECIMAL;
	}

}

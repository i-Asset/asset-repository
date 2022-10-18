package at.srfg.iasset.repository.model.value;

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

}

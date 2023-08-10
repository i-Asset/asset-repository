package at.srfg.iasset.repository.model.helper.value;

import at.srfg.iasset.repository.model.helper.value.type.Value;
public class RangeValue<T> extends DataElementValue {
	Value<T> min;
	Value<T> max;

	public RangeValue(Value<T> min, Value<T> max) {
		this.min = min;
		this.max = max;
	}

	public Value<T> getMin() {
		return min;
	}

	public void setMin(Value<T> min) {
		this.min = min;
	}

	public Value<T> getMax() {
		return max;
	}

	public void setMax(Value<T> max) {
		this.max = max;
	}

}

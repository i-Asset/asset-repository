package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.Range;

import at.srfg.iasset.repository.model.helper.value.RangeValue;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;

public class RangeValueMapper implements ValueMapper<Range, RangeValue> {

	@Override
	public RangeValue getValueOnly(Range modelElement) {
		return  new RangeValue(
				ValueType.getValue(modelElement.getValueType(), modelElement.getMin()),
				ValueType.getValue(modelElement.getValueType(), modelElement.getMax()));
	}

}

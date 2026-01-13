package at.srfg.iasset.repository.model.helper.value.type;

import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeDefXsd;

public class PosIntValue extends Value<Integer>{
	public PosIntValue() {}
	
	public PosIntValue(Integer value) {
		super(value);
	}
	@Override
	public Value<Integer> fromValue(String stringValue) {
		if ( stringValue != null) {
			setValue(Integer.parseInt(stringValue));
		}
		return this;
	}

	@Override
	public ValueType getValueType() {
		return ValueType.POS_INTEGER;
	}

}

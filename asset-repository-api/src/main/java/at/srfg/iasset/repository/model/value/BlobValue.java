package at.srfg.iasset.repository.model.value;

import org.eclipse.aas4j.v3.model.Blob;
import org.eclipse.aas4j.v3.model.DataTypeDefXsd;

public class BlobValue extends DataElementValue<Blob> {
	Value<?> typedValue; 
	
	public BlobValue(Value<?> typedValue) {
		super(typedValue.getValueType());
		this.typedValue = typedValue;
	}
	public BlobValue(DataTypeDefXsd dataType, String value) {
		super(ValueType.fromDataType(dataType));
		this.typedValue = ValueType.getValue(dataType, value);
	}
	public Value<?> getValue() {
		return typedValue;
	}
	public void setValue(Value<?> value) {
		this.typedValue = value;
	}
	
}

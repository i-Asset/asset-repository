package at.srfg.iasset.repository.model.value;

import org.eclipse.aas4j.v3.model.DataElement;

public abstract class DataElementValue<T extends DataElement> extends SubmodelElementValue<DataElement> {
	private ValueType valueType;
	
	public DataElementValue(ValueType type) {
		this.valueType = type;
	}

}

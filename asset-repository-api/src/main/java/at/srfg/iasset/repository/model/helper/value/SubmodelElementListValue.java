package at.srfg.iasset.repository.model.helper.value;

import java.util.ArrayList;
import java.util.List;

public class SubmodelElementListValue extends SubmodelElementValue {
	/**
	 * 
	 */
	private List<SubmodelElementValue> values;
	
	public SubmodelElementListValue() {
		this.values = new ArrayList<>();
	}

	public List<SubmodelElementValue> getValues() {
		return values;
	}

	public void setValues(List<SubmodelElementValue> values) {
		this.values = values;
	}
	
	
	
}

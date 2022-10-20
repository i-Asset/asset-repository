package at.srfg.iasset.repository.model.helper.value;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public class SubmodelElementCollectionValue extends SubmodelElementValue {
	/**
	 * 
	 */
	@JsonValue
	private Map<String, SubmodelElementValue> values;

	public SubmodelElementCollectionValue() {
		this.values = new HashMap<String, SubmodelElementValue>();
	}
	public Map<String, SubmodelElementValue> getValues() {
		return values;
	}

	public void setValues(Map<String, SubmodelElementValue> values) {
		this.values = values;
	}
	
	
	
}

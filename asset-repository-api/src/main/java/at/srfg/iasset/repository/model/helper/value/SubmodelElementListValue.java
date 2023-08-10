package at.srfg.iasset.repository.model.helper.value;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public class SubmodelElementListValue extends SubmodelElementValue {
	/**
	 * 
	 */
	@JsonValue
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

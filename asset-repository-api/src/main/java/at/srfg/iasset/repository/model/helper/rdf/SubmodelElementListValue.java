package at.srfg.iasset.repository.model.helper.rdf;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public class SubmodelElementListValue extends SubmodelElementValue {
	/**
	 * 
	 */
	@JsonValue
	private List<SubmodelElementValue> values;
	private boolean ordered;
	
	public SubmodelElementListValue(Boolean ordered) {
		this.ordered = (ordered == null ? false : ordered); 
		this.values = new ArrayList<>();
	}

	public List<SubmodelElementValue> getValues() {
		return values;
	}

	public void setValues(List<SubmodelElementValue> values) {
		this.values = values;
	}
	public void addValue(SubmodelElementValue value) {
		this.values.add(value);
	}
	
	
}

package at.srfg.iasset.repository.model.helper.rdf;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

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
	
	void addToRDF(Model model) {
		BNode collection = SimpleValueFactory.getInstance().createBNode();
		for ( String key : values.keySet()) {
			SubmodelElementValue value = values.get(key);
			value.addToRDF(model);
		}
	}
	
}

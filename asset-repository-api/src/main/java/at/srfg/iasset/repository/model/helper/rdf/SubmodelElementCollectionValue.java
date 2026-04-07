package at.srfg.iasset.repository.model.helper.rdf;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;

import com.fasterxml.jackson.annotation.JsonValue;

public class SubmodelElementCollectionValue extends SubmodelElementValue {
	private IRI type;
	/**
	 * 
	 */
	@JsonValue
	private Map<IRI, SubmodelElementValue> values;

	public SubmodelElementCollectionValue(IRI typeIRI) {
		this.type = typeIRI;
		this.values = new HashMap<IRI, SubmodelElementValue>();
	}
	public Map<IRI, SubmodelElementValue> getValues() {
		return values;
	}

	public void setValues(Map<IRI, SubmodelElementValue> values) {
		this.values = values;
	}
	public void addValue(IRI predicate, SubmodelElementValue value) {
		this.values.put(predicate, value);
	}
	void addToRDF(Model model) {

	}
	
}

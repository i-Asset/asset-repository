package at.srfg.iasset.repository.model.helper.rdf;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import com.fasterxml.jackson.annotation.JsonValue;

public class SubmodelElementCollectionValue extends SubmodelElementValue {
	/**
	 * 
	 */
	@JsonValue
	private Map<IRI, SubmodelElementValue> values;

	public SubmodelElementCollectionValue() {
		this.values = new HashMap<IRI, SubmodelElementValue>();
	}
	public Map<IRI, SubmodelElementValue> getValues() {
		return values;
	}

	public void setValues(Map<IRI, SubmodelElementValue> values) {
		this.values = values;
	}
	
	void addToRDF(Model model) {

	}
	
}

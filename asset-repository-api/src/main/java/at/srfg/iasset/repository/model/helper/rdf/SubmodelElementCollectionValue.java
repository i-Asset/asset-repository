package at.srfg.iasset.repository.model.helper.rdf;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import com.fasterxml.jackson.annotation.JsonValue;

public class SubmodelElementCollectionValue extends SubmodelElementValue {
	private final Optional<IRI> type;
	/**
	 * 
	 */
	@JsonValue
	private Map<IRI, SubmodelElementValue> values;

	public SubmodelElementCollectionValue(IRI predicate, Optional<IRI> typeIRI) {
		super(predicate);
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

    @Override
    public Optional<Value> addToModel(Resource parent, Model model) {
		Resource collection = SimpleValueFactory.getInstance().createBNode();
		if ( type.isPresent()) {
			model.add(collection, RDF.TYPE, type.get());
			// TODO: Handle namespace
		}
		values.keySet().forEach((predicate)-> {
			SubmodelElementValue child = values.get(predicate);

			Optional<Value> value = child.addToModel(collection, model);
			if ( value.isPresent()) {
				//
				model.add(collection, predicate, value.get());
			}

		});
		return Optional.of(collection);
    }

	
}

package at.srfg.iasset.repository.model.helper.rdf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.RDFCollections;

import com.fasterxml.jackson.annotation.JsonValue;

public class SubmodelElementListValue extends SubmodelElementValue {
	/**
	 * 
	 */
	@JsonValue
	private List<SubmodelElementValue> values;
	private boolean ordered;
	
	public SubmodelElementListValue(IRI predicate, Boolean ordered) {
		super(predicate);
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

    @Override
    protected Optional<Value> addToModel(Resource parent, Model model) {
		if ( ordered) {
			Resource listElement = SimpleValueFactory.getInstance().createBNode();
			//
			if ( parent == null ) {
				parent = SimpleValueFactory.getInstance().createBNode();
				model.add(parent, predicate(), listElement );
			}

			Iterator<SubmodelElementValue> listIterator = values.iterator();

			List<Value> listValues = new ArrayList<>();

			while ( listIterator.hasNext()) {
				SubmodelElementValue item = listIterator.next();
				//
				Optional<Value> itemValue = item.addToModel(listElement, model);
				if (itemValue.isPresent()) {
					listValues.add(itemValue.get());
				}
			}
			RDFCollections.asRDF(listValues, listElement, model);
			return Optional.of(listElement);

		}
		else {
			for (SubmodelElementValue item : values ) {
				Optional<Value> itemValue = item.addToModel(parent, model);
				if (itemValue.isPresent()) {
					model.add(parent, item.predicate(), itemValue.get());
				}
			}
			return Optional.of(parent);
		}
    }



	
}

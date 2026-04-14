package at.srfg.iasset.repository.model.helper.rdf;

import java.util.Optional;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;

/**
 * Value Class representing the RDF serialization
 * @author dglachs
 *
 * @param <T>
 */
public abstract class SubmodelElementValue {
	private final IRI predicate;

	public IRI predicate() {
		return predicate;
	}
	public SubmodelElementValue(IRI predicate) {
		this.predicate = predicate;
	}
	/**
	 * Map the Value's data to the RDF model
	 * @param parent
	 * @param model
	 * @return
	 */
    public abstract Optional<Value> addToModel(Resource parent, Model model);

}

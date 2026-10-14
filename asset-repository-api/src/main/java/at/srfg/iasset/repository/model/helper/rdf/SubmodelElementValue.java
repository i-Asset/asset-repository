package at.srfg.iasset.repository.model.helper.rdf;

import java.util.Optional;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
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
	public Optional<Value> asRDF(Resource parent, Model model) {
		if ( predicate != null) {
			addToNamespaces(model, predicate.getNamespace());
		}
		return addToModel(parent, model);
		
	}
	/**
	 * Map the Value's data to the RDF model
	 * @param parent
	 * @param model
	 * @return
	 */
    protected abstract Optional<Value> addToModel(Resource parent, Model model);
   
	protected void addToNamespaces(Model model, String namespace) {
		Optional<Namespace> vocab = model.getNamespace("");
		if ( vocab.isEmpty()) {
			model.setNamespace("", namespace);
		}
		else {
			if (! vocab.get().getName().equals(namespace)) {
				//
				Optional<Namespace> ns = model.getNamespaces().stream().filter((Namespace t)-> t.getName().equals(namespace)).findFirst();
				if ( ns.isEmpty()) {
					long nsCount = model.getNamespaces().stream().filter((Namespace t) -> !t.getPrefix().startsWith("ns")).count();
					model.setNamespace(String.format("ns%s", nsCount), namespace);
				}
			}
		}
	}

}

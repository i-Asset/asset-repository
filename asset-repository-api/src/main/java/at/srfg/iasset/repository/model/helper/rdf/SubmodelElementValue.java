package at.srfg.iasset.repository.model.helper.rdf;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;

/**
 * Value Class representing the RDF serialization
 * @author dglachs
 *
 * @param <T>
 */
public abstract class SubmodelElementValue {

	public void addToRDF(Resource parent, Model model) {
		
	};

}

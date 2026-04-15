package at.srfg.iasset.repository.model.helper.rdf;

import java.util.Optional;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public abstract class DataElementValue extends SubmodelElementValue {

    public DataElementValue(IRI predicate) {
        super(predicate);
    }
    @Override
    public final Optional<Value> addToModel(Resource parent, Model model) {
    	addToNamespaces(model, predicate().getNamespace());
        // handle the data element - simply add element to the model 
        Resource subject = (parent == null ? SimpleValueFactory.getInstance().createBNode() : parent);
        addToRDF(subject,model);
        // return null to indicate, that everything is set
        return Optional.empty();
	}
	    
    protected abstract void addToRDF(Resource parent, Model model);
}

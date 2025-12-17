package at.srfg.iasset.repository.model.helper.rdf.mapper;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public interface RDFMapper<M extends SubmodelElement, V extends SubmodelElementValue> {
	
	default Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, M modelElement) throws ValueMappingException {
		return new TreeModel();
	}

}

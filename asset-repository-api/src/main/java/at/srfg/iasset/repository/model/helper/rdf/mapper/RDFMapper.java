package at.srfg.iasset.repository.model.helper.rdf.mapper;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.TreeModel;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public interface RDFMapper<M extends SubmodelElement, V extends SubmodelElementValue> {
	/**
	 * Mapt the submodel element and its children to a {@link Model}
	 * @param rdfMetaModel
	 * @param parent
	 * @param modelElement
	 * @return
	 * @throws ValueMappingException
	 */
	default Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, M modelElement) throws ValueMappingException {
		return new TreeModel();
	}
	default Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, Model model, M modelElement) throws ValueMappingException {
		return new TreeModel();
	}
	default Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, IRI predicate, M modelElement) throws ValueMappingException {
		return new TreeModel();
	}

	default M mapToElement(RDFEnvironment rdfMetaModel, Resource parent, Model model,M modelElement) throws ValueMappingException {
		return modelElement;
	}

}	

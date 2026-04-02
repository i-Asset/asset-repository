package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementListValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class SubmodelElementListMapper implements RDFMapper<SubmodelElementList, SubmodelElementListValue> {

	@Override
	public Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, Model model, SubmodelElementList modelElement)
			throws ValueMappingException {
		// TODO Auto-generated method stub
		BNode first = SimpleValueFactory.getInstance().createBNode();
		
		if ( modelElement.getOrderRelevant()) {
		}
		List<SubmodelElement> values = modelElement.getValue();
		
		return RDFMapper.super.mapToRDF(rdfMetaModel, parent, model, modelElement);
	}

	@Override
	public SubmodelElementList mapToElement(RDFEnvironment rdfMetaModel, Resource parent, Model model,
			SubmodelElementList modelElement) throws ValueMappingException {
		// TODO Auto-generated method stub
		return RDFMapper.super.mapToElement(rdfMetaModel, parent, model, modelElement);
	}

}

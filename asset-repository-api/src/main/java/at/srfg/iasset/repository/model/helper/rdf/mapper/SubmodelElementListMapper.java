package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.Iterator;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.RDFCollections;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.RDFHelper;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementListValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class SubmodelElementListMapper implements RDFMapper<SubmodelElementList, SubmodelElementListValue> {
	

	@Override
	public Optional<SubmodelElementListValue> mapToValue(SubmodelElementList modelElement, RDFEnvironment rdfEnvironment)
			throws ValueMappingException {
		Optional<IRI> listProperty = rdfEnvironment.getSemanticIdentifier(modelElement);

		Optional<IRI> listSemantics = rdfEnvironment.getSemanticIdentifier(modelElement.getSemanticIdListElement());
		SubmodelElementListValue listValue = new SubmodelElementListValue(listProperty.get(), modelElement.getOrderRelevant());
		for ( SubmodelElement element : modelElement.getValue()) {
			RDFHelper.toValue(element, rdfEnvironment).ifPresent((value)-> listValue.addValue(value));
		}
		return Optional.of(listValue);
	}

	    @Override
	public SubmodelElementList mapToElement(RDFEnvironment rdfMetaModel, Resource parent, Model model,
			SubmodelElementList modelElement) throws ValueMappingException {
		// TODO Auto-generated method stub
//		RDFCollections.asValues(model, parent, null, null)
		return RDFMapper.super.mapToElement(rdfMetaModel, parent, model, modelElement);
	}


}

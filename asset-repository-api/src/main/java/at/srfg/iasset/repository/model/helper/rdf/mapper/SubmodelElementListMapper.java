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
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.RDFHelper;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementListValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class SubmodelElementListMapper implements RDFMapper<SubmodelElementList, SubmodelElementListValue> {
	

	@Override
	public SubmodelElementListValue mapToValue(SubmodelElementList modelElement, RDFEnvironment rdfEnvironment)
			throws ValueMappingException {
		SubmodelElementListValue listValue = new SubmodelElementListValue(modelElement.getOrderRelevant());
		for ( SubmodelElement element : modelElement.getValue()) {
			listValue.addValue(RDFHelper.toValue(element, rdfEnvironment));
		}
		return listValue;
	}

	@Override
	public Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, SubmodelElementList modelElement)
			throws ValueMappingException {
		// TODO Auto-generated method stub
		Model model = new TreeModel();
		if (parent == null ) {
			parent = SimpleValueFactory.getInstance().createBNode();
			model.setNamespace("xs", XSD.NAMESPACE);
		}
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement.getSemanticId());
		if ( property.isPresent()) {
			// 
			BNode listHead = SimpleValueFactory.getInstance().createBNode();
			// create property for list
			model.add(parent, property.get(), listHead);
			
			Iterator<SubmodelElement>	listIterator =		modelElement.getValue().iterator();
			BNode current = listHead;
			//
			while (listIterator.hasNext()) {
				BNode value = SimpleValueFactory.getInstance().createBNode();
				SubmodelElement listChild = listIterator.next();
				model.addAll(RDFHelper.toRDF(rdfMetaModel, value, listChild));
				
				model.add(current, RDF.FIRST, value );
				
				if ( listIterator.hasNext())  {
					BNode next = SimpleValueFactory.getInstance().createBNode();
					model.add(current, RDF.REST, next);
					
					current = next;
					
				}
				else {
					model.add(current, RDF.REST, RDF.NIL);
				}
			}
		}

		return model;
	}

	@Override
	public SubmodelElementList mapToElement(RDFEnvironment rdfMetaModel, Resource parent, Model model,
			SubmodelElementList modelElement) throws ValueMappingException {
		// TODO Auto-generated method stub
		return RDFMapper.super.mapToElement(rdfMetaModel, parent, model, modelElement);
	}

}

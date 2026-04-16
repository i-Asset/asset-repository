package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.ArrayList;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.RDFCollections;

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
		if ( parent == null) {
			Optional<Resource> root = rdfMetaModel.getSemanticIdentifier(modelElement).map((IRI t) -> {
				// search for statements with the semantic identifier as predicate!
				return model.filter(null, t, null)
					// obtain all the subjects
					.subjects()
					.stream()
					// filter those subjects which are not used as value in the model
					.filter((Resource r)-> !model.contains(null, null, r))
					// 
					.findFirst()
					.orElse(null);
			});
			if ( root.isPresent()) {
				parent = root.get();
			}

		}
		// need to synchronize the values based on the index
		ArrayList<Value> values = RDFCollections.asValues(model, parent, new ArrayList<>() ); 
		// traverse the list 
		for (int i = 0; i < Math.min(values.size(), modelElement.getValue().size()); i++) {
			if ( Resource.class.isInstance( values.get(i))) {
				RDFHelper.fromRDF(rdfMetaModel, Resource.class.cast(values.get(i)), model, modelElement.getValue().get(i));
			}
		}

		return modelElement;
	}


}

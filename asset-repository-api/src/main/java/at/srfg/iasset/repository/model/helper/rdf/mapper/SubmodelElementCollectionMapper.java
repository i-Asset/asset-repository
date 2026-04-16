package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.RDFHelper;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementCollectionValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class SubmodelElementCollectionMapper implements RDFMapper<SubmodelElementCollection, SubmodelElementCollectionValue>{
	

	@Override
	public Optional<SubmodelElementCollectionValue> mapToValue(SubmodelElementCollection modelElement,
			RDFEnvironment rdfEnvironment) throws ValueMappingException {
		
		
		Optional<IRI> typeIRI = rdfEnvironment.getTypeInformation(modelElement.getSemanticId());

		Optional<IRI> property = rdfEnvironment.getSemanticIdentifier(modelElement);
		SubmodelElementCollectionValue valueElement = new SubmodelElementCollectionValue(property.orElse(null), typeIRI);
		
		for (SubmodelElement child : modelElement.getValue()) {
			RDFHelper.toValue(child, rdfEnvironment).ifPresent((value) -> valueElement.addValue(value.predicate(), value));
		}
		return Optional.of(valueElement);
	}


	@Override
	public SubmodelElementCollection mapToElement(RDFEnvironment rdfMetaModel, Resource parent, Model model,
			SubmodelElementCollection modelElement) throws ValueMappingException {
		// when no parent resource provided, try to find the root node!
		if ( parent == null ) {
			Optional<IRI> typeIRI = rdfMetaModel.getTypeInformation(modelElement.getSemanticId());
			if ( typeIRI.isPresent()) {
				Optional<Resource> root = model.filter(null, RDF.TYPE, typeIRI.get()).subjects()
					.stream()
					.filter((Resource t) -> {
                                            if (!model.contains(null, null, t))
                                                return true;
                                            //
                                            return false;
                                })
				.findFirst();
				if ( root.isPresent()) {
					// keep the root
					parent = root.get();
				}
				
			
			}
		}
		
		for (SubmodelElement submodelElement : modelElement.getValue()) {
			Optional<IRI> predicate = rdfMetaModel.getSemanticIdentifier(submodelElement.getSemanticId());
			if ( predicate.isPresent()) {
				Optional<Value> subject = model.filter(parent, predicate.get(), null).objects().stream().findFirst();
				if ( subject.isPresent() && subject.get().isResource()) {
					RDFHelper.fromRDF(rdfMetaModel, (Resource)subject.get(), model, submodelElement);
					
				}
				else {
					RDFHelper.fromRDF(rdfMetaModel, parent, model, submodelElement);
				}
			}
		}
		return modelElement;
	}

	 

}

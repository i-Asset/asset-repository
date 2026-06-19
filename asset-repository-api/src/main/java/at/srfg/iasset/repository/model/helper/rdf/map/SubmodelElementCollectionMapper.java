package at.srfg.iasset.repository.model.helper.rdf.map;

import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class SubmodelElementCollectionMapper extends SubmodelElementMapper<SubmodelElementCollection> {

	@Override
	protected Optional<Value> addToModel(SubmodelElementCollection modelElement, Resource subject,  Model model, RDFEnvironment rdfEnvironment) {
		// create the collection's resource node
		Resource collection = SimpleValueFactory.getInstance().createBNode();
		rdfEnvironment.getSemanticIdentifier(modelElement).ifPresent((predicate)->{
			// manage namespace
			addNameSpace(model, predicate);
			// check for the type
			rdfEnvironment.getTypeInformation(modelElement.getSemanticId()).ifPresent((type) -> {
				addNameSpace(model, type);
				model.add(collection, RDF.TYPE, type);
			});
			// add the elements
			if (! modelElement.getValue().isEmpty()) {
				// 
				modelElement.getValue().forEach((element) -> {
					
					try {
						getMapper(element).toRDF(collection, modelElement, model, rdfEnvironment);
					} catch (ValueMappingException e) {
						e.printStackTrace();
					}
				});
				// when parent element present 
				if (subject != null ) {
					model.add(subject, predicate, collection);
				}
			}
		});
		return Optional.empty();
	}

}

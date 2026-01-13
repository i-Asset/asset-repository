package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.base.CoreDatatype.XSD;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.RDFHelper;
import at.srfg.iasset.repository.model.helper.rdf.SubmodelElementCollectionValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class SubmodelElementCollectionMapper implements RDFMapper<SubmodelElementCollection, SubmodelElementCollectionValue>{

	@Override
	public Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, SubmodelElementCollection modelElement) {
		// TODO Auto-generated method stub
		Model model = new TreeModel();
		model.setNamespace("xsd", XSD.NAMESPACE);
		Resource collectionResource = SimpleValueFactory.getInstance().createBNode();
		// try to find the type information for the collection!
		Optional<IRI> typeIRI = rdfMetaModel.getTypeInformation(modelElement.getSemanticId());
		// type statement is not mandatory! 
		if (typeIRI.isPresent()) {
			model.add(collectionResource, RDF.TYPE, typeIRI.get());
			// TODO: better namespace management
			model.setNamespace("", typeIRI.get().getNamespace());
		}
		// when parent present, the property IRI must be present as well in order to properly
		// chain the statements!
		if ( parent != null) {
			Optional<IRI> propertyIri = rdfMetaModel.getSemanticIdentifier(modelElement.getSemanticId()); 
			if ( propertyIri.isPresent() ) {
				// link to existing 
				model.add(parent, propertyIri.get(), collectionResource);
			}
			else {
				// parent present, use default namespace
				SimpleValueFactory.getInstance().createIRI(rdfMetaModel.getDefaultNamespace(), modelElement.getIdShort());
			}
		}
		// 
		for (SubmodelElement submodelElement : modelElement.getValue() ) {
			try {
				model.addAll(RDFHelper.toRDF(rdfMetaModel, collectionResource, submodelElement));
			} catch (ValueMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// return empty model
		return model;
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
					.filter(new Predicate<Resource>() {

					@Override
					public boolean test(Resource t) {
						if (!model.contains(null, null, t))
							return true;
						//
						return false;
					}
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

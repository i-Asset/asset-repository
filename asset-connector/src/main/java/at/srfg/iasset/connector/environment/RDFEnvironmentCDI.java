package at.srfg.iasset.connector.environment;

import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.HasSemantics;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.helper.IdType;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RDFEnvironmentCDI implements RDFEnvironment {
	
	private final Model metaModel = new TreeModel();
	
	private Repository rdf4J = new SailRepository(new MemoryStore());
	@Inject
	private ServiceEnvironment serviceEnvironment;
	

	@Override
	public Optional<IRI> getSemanticIdentifier(HasSemantics element) {
		return getSemanticIdentifier(element.getSemanticId());
	}



	@Override
	public Optional<IRI> getSemanticIdentifier(Reference semanticId) {

		//
		if ( semanticId != null) {
			Reference referredSemanticId = semanticId.getReferredSemanticId();
			if ( referredSemanticId!=null) {
				return getSemanticIdentifier(referredSemanticId);
			}
			// resolve the reference!
			Optional<Referable> semanticElementTypeOpt = serviceEnvironment.resolve(semanticId);
			// may be 
			if ( semanticElementTypeOpt.isPresent()) {
				Referable semanticElementType = semanticElementTypeOpt.get();
				if ( ConceptDescription.class.isInstance(semanticElementType)) {
					// reference pointed to ConceptDescription
					ConceptDescription cDesc = ConceptDescription.class.cast(semanticElementType);
					//
					if ( IdType.isIRI(cDesc.getId())) {
						return Optional.of(SimpleValueFactory.getInstance().createIRI(cDesc.getId()));
					}
				}
				//  
				if (SubmodelElement.class.isInstance(semanticElementType)) {
					return getSemanticIdentifier(SubmodelElement.class.cast(semanticElementType).getSemanticId());
				}
				if (Submodel.class.isInstance(semanticElementType) ) {
					return getSemanticIdentifier(Submodel.class.cast(semanticElementType).getSemanticId());
				}
			}
		}
		return Optional.empty();
	}

	

	@Override
	public Optional<IRI> getTypeInformation(Reference semanticId) {
		if ( semanticId != null) {
			Reference referredSemanticId = semanticId.getReferredSemanticId();
			if ( referredSemanticId!=null) {
				return getSemanticIdentifier(referredSemanticId);
			}
			// resolve the reference!
			Optional<Referable> semanticElementTypeOpt = serviceEnvironment.resolve(semanticId);
			// may be 
			if ( semanticElementTypeOpt.isPresent()) {
				Referable semanticElementType = semanticElementTypeOpt.get();
				if ( ConceptDescription.class.isInstance(semanticElementType)) {
					// reference pointed to ConceptDescription
					ConceptDescription cDesc = ConceptDescription.class.cast(semanticElementType);
					//
					Optional<Reference> typeRef = cDesc.getIsCaseOf().stream().filter(new Predicate<Reference>() {

						@Override
						public boolean test(Reference t) {
							// is it a 
							if ( IdType.isIRI(ReferenceUtils.firstKeyValue(t))) {
								return true;
							}
							return false;
						}
						
					})
					.findFirst();
					if ( typeRef.isPresent() ) {
						return Optional.of(SimpleValueFactory.getInstance().createIRI(ReferenceUtils.firstKeyValue(typeRef.get())));
					}
				}
				//  
				if (SubmodelElement.class.isInstance(semanticElementType)) {
					return getTypeInformation(SubmodelElement.class.cast(semanticElementType).getSemanticId());
				}
				if (Submodel.class.isInstance(semanticElementType) ) {
					return getTypeInformation(Submodel.class.cast(semanticElementType).getSemanticId());
				}
			}
		}
		return Optional.empty();
	}



	@Override
	public boolean isClass(IRI iri) {
		// check whether the provided IRI is a rdfs:Class
		try (RepositoryConnection conn = rdf4J.getConnection()) {
			String query = String.format("ASK { %s %s %s}", iri.stringValue(), RDF.TYPE.stringValue(), RDFS.CLASS.stringValue());
			return conn.prepareBooleanQuery(query).evaluate();
			
		}
	}

	@Override
	public boolean isDataTypeProperty(IRI iri) {
		try (RepositoryConnection conn = rdf4J.getConnection()) {
			String query = String.format("ASK { %s %s %s}", iri.stringValue(), RDF.TYPE.stringValue(), RDF.PROPERTY.stringValue());
			return conn.prepareBooleanQuery(query).evaluate();
			
		}
	}

	@Override
	public boolean isObjectProperty(IRI iri) {
		return metaModel.contains(iri, RDF.TYPE, OWL.OBJECTPROPERTY);
	}

	@Override
	public void addModel(Model model) {
		try (RepositoryConnection conn = rdf4J.getConnection()){
			conn.add(model);
		}

	}



	@Override
	public String getDefaultNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

}

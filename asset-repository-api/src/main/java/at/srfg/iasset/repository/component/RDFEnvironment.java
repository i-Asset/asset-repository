package at.srfg.iasset.repository.component;

import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.HasSemantics;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;

public interface RDFEnvironment {
	/**
	 * Retrieve the semantic IRI for the given element - may resolve 
	 * from the central repository. 
	 * <p>The IRI is retrieved from the meta-model by checking
	 * the element's {@link Reference#getReferredSemanticId()} or following
	 * the {@link ReferenceTypes#MODEL_REFERENCE} to identify the type information. 
	 * </p>
	 * @param element The semanticId 
	 * @return IRI or {@link Optional#empty()} when not found
	 */
	public Optional<IRI> getSemanticIdentifier(Reference element);
	public Optional<IRI> getSemanticIdentifier(HasSemantics element);
	/**
	 * Retrieve the type IRI for the given element. 
	 * <p>The type IRI is resolved by looking for a linked {@link ConceptDescription}
	 * and verifying the {@link ConceptDescription#getIsCaseOf()} references.
	 * </p>
	 * @param element The semanticId
	 * @return IRI or {@link Optional#empty()} when not found
	 */
	public Optional<IRI> getTypeInformation(Reference element);
	/**
	 * Check whether the provided IRI is part of the model AND is a subclass of
	 * <code>rdfs:Class</code>
	 * 
	 * @param iri
	 * @return
	 */
	public boolean isClass(IRI iri);
	/**
	 * Check whether the provided IRI is part of the model AND is a subclass of
	 * <code>rdfs:Property</code>
	 * 
	 * @param iri
	 * @return
	 */
	public boolean isDataTypeProperty(IRI iri);
	public boolean isObjectProperty(IRI iri);
	
	public String getDefaultNamespace();
	/**
	 * Add meta model definition to the RDF environment
	 * @param model
	 */
	public void addModel(Model model);
}

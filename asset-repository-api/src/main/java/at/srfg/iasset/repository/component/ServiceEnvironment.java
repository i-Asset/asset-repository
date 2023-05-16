package at.srfg.iasset.repository.component;

import java.util.List;
import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Identifiable;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
/**
 * 
 * @author dglachs
 *
 */
public interface ServiceEnvironment {
	/**
	 * Obtain a {@link Submodel}. The {@link Submodel} is only returned when it exists and is assigned to the {@link AssetAdministrationShell}
	 * @param aasIdentifier The {@link AssetAdministrationShell} containing/referencing the {@link Submodel}
	 * @param submodelIdentifier The {@link Submodel} to retrieve
	 * @return The {@link Submodel} when found, {@link Optional#empty()} otherwise
	 */
	Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier);
	/**
	 * Obtain a {@link Submodel} based on it's identifier. 
	 * @param submodelIdentifier The submodel to retieve
	 * @return The {@link Submodel} when found, {@link Optional#empty()} otherwise
	 */
	Optional<Submodel> getSubmodel(String submodelIdentifier);
	/**
	 * Check for the {@link AssetAdministrationShell}
	 * @param identifier
	 * @return
	 */
	Optional<AssetAdministrationShell> getAssetAdministrationShell(String identifier);
	/**
	 * Add/Update the {@link AssetAdministrationShell}
	 * @param theShell
	 * @return
	 */
	AssetAdministrationShell setAssetAdministrationShell(String aasIdentifier, AssetAdministrationShell theShell);
	/**
	 * Check for the {@link ConceptDescription}
	 * @param identifier
	 * @return
	 */
	Optional<ConceptDescription> getConceptDescription(String identifier);
	/**
	 * Remove an {@link AssetAdministrationShell} by its identifier
	 * @param identifier
	 */
	boolean deleteAssetAdministrationShellById(String identifier);
	/**
	 * Remove a {@link Submodel} (reference) from the AAS
	 * @param aasIdentifier
	 * @param ref The reference to delete
	 * @return
	 */
	boolean deleteSubmodelReference(String aasIdentifier, Reference ref);
	/**
	 * Resolve a reference from the Environment
	 * @param <T>
	 * @param reference The reference pointing to the model element
	 * @param type The expected type of the referenced model element 
	 * @return
	 */
	<T extends Referable> Optional<T> resolve(Reference reference, Class<T> type);
	/**
	 * Resolve the reference from the environment
	 * @param reference
	 * @return
	 */
	Optional<Referable> resolve(Reference reference);
	/**
	 * Resolve the referenced element <b>ValueOnly</b>
	 * @param <T>
	 * @param reference The reference to resolve
	 * @param path The path inside the referenced element.
	 * @param type The (expected) type of the ValueOnly
	 * @return
	 */
	<T> Optional<T> resolveValue(Reference reference, String path, Class<T> type);
	/**
	 * Resolve the referenced element <b>ValueOnly</b>
	 * @param <T>
	 * @param reference The reference to resolve
	 * @param type The (expected) type of the ValueOnly
	 * @return
	 */
	<T> Optional<T> resolveValue(Reference reference, Class<T> type);
	/**
	 * Retrieve all asset {@link AssetAdministrationShell} 
	 * @return
	 */
	List<AssetAdministrationShell> getAllAssetAdministrationShells();
	/**
	 * Remove a {@link SubmodelElement} from a {@link Submodel}
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return <code>true</code> when successful, <code>false</code> otherwise
	 */
	boolean deleteSubmodelElement(String aasIdentifier, String submodelIdentifier, String path);
	/**
	 * Update a {@link SubmodelElement}, the element is identified with is's complete idShort path!
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param idShortPath
	 * @param body
	 */
	SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, String idShortPath, SubmodelElement body);

	/**
	 * Find a {@link SubmodelElement} in the reference {@link Submodel}. 
	 * The {@link Submodel} must be assigned to the {@link AssetAdministrationShell}
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return
	 */
	Optional<SubmodelElement> getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path);
	/**
	 * {@link Submodel} is {@link Identifiable}, therefore it can be processed without 
	 * 
	 * @param submodelIdentifier The identifier pointing to the {@link Submodel}
	 * @param path The idShort-path pointing to the requested {@link SubmodelElement}
	 * @return The {@link Submodel} or {@link Optional#empty()} when not found
	 */
	Optional<SubmodelElement> getSubmodelElement(String submodelIdentifier, String path);

	/**
	 * Update a submodel in the repository. Maintain the list of assigned repositories with the AAS
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param submodel
	 * @return
	 */
	Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, Submodel submodel);
	/**
	 * 
	 * @param aasIdentifier
	 * @param element
	 * @return
	 */
	Optional<Referable> getSubmodelElement(AssetAdministrationShell aasIdentifier, Reference element);
	/**
	 * Obtain the ValueOnly representation of a SubmodelElement
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return
	 */
	Object getElementValue(String aasIdentifier, String submodelIdentifier, String path);
	/**
	 * Obtain the ValueOnly representation of a SubmodelElement
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return
	 */
	Object getElementValue(String submodelIdentifier, String path);
	/**
	 * Obtain the ValueOnly representation of a {@link Reference} of type {@link ReferenceTypes#MODEL_REFERENCE}
	 * @param reference
	 * @return The value only serialization of the referenced element
	 */
	Object getElementValue(Reference reference);
	/**
	 * Obtain the full representation of a {@link Referable} element. 
	 * @param reference The {@link Referable} of type {@link ReferenceTypes#MODEL_REFERENCE}
	 * @return The {@link Referable} element or <code>null</code> when not found
	 */
	Optional<Referable> getSubmodelElement(Reference reference);
	/**
	 * Update a {@link SubmodelElement} based on it's ValueOnly representation
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param value
	 */
	void setElementValue(String aasIdentifier, String submodelIdentifier, String path, Object value);
	/**
	 * Add/replace a {@link ConceptDescription}
	 * @param cdIdentifier
	 * @param conceptDescription
	 * @return
	 */
	ConceptDescription setConceptDescription(String cdIdentifier, ConceptDescription conceptDescription);
	/** 
	 * Retrieve all {@link Submodel} references from an {@link AssetAdministrationShell}
	 * @param aasIdentifier
	 * @return
	 */
	List<Reference> getSubmodelReferences(String aasIdentifier);
	/**
	 * Update the submodel reference list of the {@link AssetAdministrationShell}
	 * @param aasIdentifier
	 * @param submodels
	 * @return
	 */
	List<Reference> setSubmodelReferences(String aasIdentifier, List<Reference> submodels);
	/**
	 * Remove a submodel reference from the {@link AssetAdministrationShell}. The {@link Submodel} itsel
	 * is not affected by this operation!
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @return
	 */
	List<Reference> deleteSubmodelReference(String aasIdentifier, String submodelIdentifier);
	/**
	 * Add a new {@link SubmodelElement} to the identified {@link Submodel}
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param element
	 */
	SubmodelElement setSubmodelElement(String aasIdentifier, String submodelIdentifier, SubmodelElement element);
	/**
	 * Execute the identified operation
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @param parameterMap
	 * @return
	 */
	Object invokeOperation(String aasIdentifier, String submodelIdentifier, String path, Object parameterMap);
	/**
	 * Execute the identified operation
	 * @param operation
	 * @param parameter
	 * @return
	 */
	Object invokeOperation(Reference operation, Object parameter);
	/**
	 * Find all {@link SubmodelElement} of the requested type and with the required semanticId 
	 * @param <T>
	 * @param semanticId
	 * @param clazz
	 * @return
	 */
	<T extends SubmodelElement> List<T> getSubmodelElements(String aasIdentifier, String submodelIdentifier, Reference semanticId, Class<T> clazz);
	/**
	 * Obtain the value only representation of the {@link SubmodelElement} and convert it to the provided type!
	 * @param <T>
	 * @param submodelIdentifier
	 * @param path
	 * @param clazz
	 * @return
	 */
	<T> T getElementValue(String submodelIdentifier, String path, Class<T> clazz);

	
	

}

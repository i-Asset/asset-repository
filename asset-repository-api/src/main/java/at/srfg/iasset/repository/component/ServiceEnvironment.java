package at.srfg.iasset.repository.component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

public interface ServiceEnvironment {
	/**
	 * Obtain the {@link Submodel}
	 * @param identifier
	 * @return
	 */
	@Deprecated
	Optional<Submodel> getSubmodel(String identifier);
	/**
	 * Obtain a {@link Submodel} 
	 * @param aasIdentifier The {@link AssetAdministrationShell} containing/referencing the {@link Submodel}
	 * @param submodelIdentifier The {@link Submodel}
	 * @return The {@link Submodel} when found, {@link Optional#empty()} otherwise
	 */
	Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier);
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
	 * Remove a {@link Submodel} (reference) from the 
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
	 * Retrieve all asset {@link AssetAdministrationShell} 
	 * @return
	 */
	List<AssetAdministrationShell> getAllAssetAdministrationShells();
	/**
	 * Remove a {@link SubmodelElement} from a {@link Submodel}
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return
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
	Optional<Referable> getSubmodelElement(String aasIdentifier, Reference element);
	/**
	 * Obtain the ValueOnly representation of a SubmodelElement
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @param path
	 * @return
	 */
	Object getElementValue(String aasIdentifier, String submodelIdentifier, String path);
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
	 * @param id
	 * @param submodels
	 * @return
	 */
	List<Reference> setSubmodelReferences(String aasIdentifier, List<Reference> submodels);
	/**
	 * Remove a submodel reference from the {@link AssetAdministrationShell}. The {@link Submodel} itsel
	 * is not affected by this operation!
	 * @param id
	 * @param submodelIdentifier
	 * @return
	 */
	List<Reference> deleteSubmodelReference(String id, String submodelIdentifier);
	/**
	 * Add a new {@link SubmodelElement} to the identified {@link Submodel}
	 * @param id
	 * @param base64Decode
	 * @param element
	 */
	SubmodelElement setSubmodelElement(String id, String submodelIdentifier, SubmodelElement element);
	/**
	 * Execute the identified operation
	 * @param id
	 * @param base64Decode
	 * @param path
	 * @param parameterMap
	 * @return
	 */
	Map<String, Object> invokeOperation(String id, String base64Decode, String path, Map<String, Object> parameterMap);

}

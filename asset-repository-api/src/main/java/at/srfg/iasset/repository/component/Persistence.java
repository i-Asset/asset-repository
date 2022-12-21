package at.srfg.iasset.repository.component;

import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Environment;
import org.eclipse.aas4j.v3.model.Submodel;

public interface Persistence extends Environment {
	/**
	 * Store the provided {@link AssetAdministrationShell} with the {@link Environment}.
	 * @param assetAdministrationShell The shell to add
	 * @return The persisted {@link AssetAdministrationShell}
	 */
	AssetAdministrationShell persist(AssetAdministrationShell assetAdministrationShell);
	/**
	 * Store the provided {@link ConceptDescription} with the {@link Environment}.
	 * @param conceptDescription The concept to add
	 * @return The persisted {@link ConceptDescription}
	 */
	ConceptDescription persist(ConceptDescription conceptDescription);
	/**
	 * Store the provided {@link Submodel} with the {@link Environment}.
	 * @param submodel The submodel to add
	 * @return The persisted {@link Submodel}
	 */
	Submodel persist(Submodel submodel);
	/**
	 * Store the provided {@link AssetAdministrationShellDescriptor} with the {@link Environment}.
	 * @param descriptor The descriptor to add
	 * @return The persisted {@link AssetAdministrationShellDescriptor}
	 */
	AssetAdministrationShellDescriptor persist(AssetAdministrationShellDescriptor descriptor);
	/**
	 * Remove a {@link Submodel} from the {@link Environment}
	 * @param submodelIdentifier The submodel identifier
	 */
	void deleteSubmodelById(String submodelIdentifier);
	/**
	 * Remove an {@link AssetAdministrationShell} from the {@link Environment}
	 * @param aasIdentifier The aas identifier
	 */
	void deleteAssetAdministrationShellById(String aasIdentifier);
	/**
	 * Remove a {@link ConceptDescription} from the {@link Environment}
	 * @param cdIdentifier The identifier of the concept description
	 */
	void deleteConceptDescriptionById(String cdIdentifier);
	/**
	 * Remove a {@link AssetAdministrationShellDescriptor} from the {@link Environment}
	 * @param descriptorIdentifier The descriptor's identifier
	 */
	void deleteAssetAdministrationShellDescriptorById(String descriptorIdentifier);
	/**
	 * Retrieve a {@link ConceptDescription} from the {@link Environment}
	 * @param cdIdentifier the identifier of the concept
	 * @return The {@link ConceptDescription} or {@link Optional#empty()} when not found
	 */
	Optional<ConceptDescription> findConceptDescriptionById(String cdIdentifier);
	/**
	 * Retrieve an {@link AssetAdministrationShell} based on it's identifier
	 * @param aasIdentifier The id of the AAS
	 * @return the {@link AssetAdministrationShell} or {@link Optional#empty()} when not found
	 */
	Optional<AssetAdministrationShell> findAssetAdministrationShellById(String aasIdentifier);
	/**
	 * Retrieve an {@link Submodel} based on it's identifier
	 * @param submodel The id of the AAS
	 * @return the {@link Submodel} or {@link Optional#empty()} when not found
	 */
	Optional<Submodel> findSubmodelById(String submodel);
	/**
	 * Retrieve an {@link AssetAdministrationShellDescriptor} based on it's identifier
	 * @param id The id of the AAS descriptior
	 * @return the {@link AssetAdministrationShellDescriptor} or {@link Optional#empty()} when not found
	 */
	Optional<AssetAdministrationShellDescriptor> findAssetAdministrationShellDescriptorById(String id);

}

package at.srfg.iasset.repository.component;

import java.util.List;
import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Submodel;

public interface Persistence {
	AssetAdministrationShell persist(AssetAdministrationShell assetAdministrationShell);
	ConceptDescription persist(ConceptDescription conceptDescription);
	Submodel persist(Submodel submodel);
	AssetAdministrationShellDescriptor persist(AssetAdministrationShellDescriptor descriptor);
	
	void deleteSubmodelById(String submodelIdentifier);
	void deleteAssetAdministrationShellById(String aasIdentifier);
	void deleteConceptDescriptionById(String cdIdentifier);
	void deleteAssetAdministrationShellDescriptorById(String descriptorIdentifier);
	
	Optional<ConceptDescription> findConceptDescriptionById(String cdIdentifier);
	Optional<AssetAdministrationShell> findAssetAdministrationShellById(String aasIdentifier);
	Optional<Submodel> findSubmodelById(String submodel);
	Optional<AssetAdministrationShellDescriptor> findAssetAdministrationShellDescriptorById(String id);
	
	List<AssetAdministrationShell> findAllAssetAdministrationShell();
	
}

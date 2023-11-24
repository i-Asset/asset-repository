package at.srfg.iasset.repository.component;

import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelDescriptor;
/**
 * Interface 
 * @author dglachs
 *
 */
public interface DirectoryService {
	/**
	 * Remove the {@link AssetAdministrationShellDescriptor} from the Directory
	 * @param aasIdentifier
	 */
	void removeShellDescriptor(String aasIdentifier);

	void removeSubmodelDescriptors(String aasIdentifier, String submodelIdentifier);

	Optional<AssetAdministrationShellDescriptor> getShellDescriptor(String aasIdentifier);
	Optional<AssetAdministrationShellDescriptor> getShellDescriptorBySupplementalSemanticId(String supplementalSemanticId);

	Optional<SubmodelDescriptor> getSubmodelDescriptor(String aasDescriptor, String submodelIdentifier);

	AssetAdministrationShell registerShellDescriptor(String aasIdentifier, AssetAdministrationShellDescriptor shell);

	Submodel registerSubmodelDescriptor(String aasIdentifier, String submodelIdentifier, SubmodelDescriptor model);

}
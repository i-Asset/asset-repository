package at.srfg.iasset.connector.environment.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;

import at.srfg.iasset.repository.component.Persistence;
import jakarta.enterprise.context.ApplicationScoped;
/**
 * Implementation providing InMemory-Persistence for
 * <ul>
 * <li>AssetAdministrationShell
 * <li>Submodel
 * <li>ConceptDescription
 * </ul>
 * 
 * @author dglachs
 *
 */
@ApplicationScoped
public class InMemoryStorage implements Persistence {
	
	
	private final Map<String, AssetAdministrationShell> assetAdministrationShell = new HashMap<String, AssetAdministrationShell>();
	private final Map<String, Submodel> submodel = new HashMap<String, Submodel>();
	private final Map<String, ConceptDescription> conceptDescription = new HashMap<String, ConceptDescription>();

	

	@Override
	public AssetAdministrationShell persist(AssetAdministrationShell assetAdministrationShell) {
		
		this.assetAdministrationShell.put(assetAdministrationShell.getId(), assetAdministrationShell);
		return assetAdministrationShell;
	}

	@Override
	public ConceptDescription persist(ConceptDescription conceptDescription) {
		this.conceptDescription.put(conceptDescription.getId(), conceptDescription);
		return conceptDescription;
	}

	@Override
	public Submodel persist(Submodel submodel) {
		this.submodel.put(submodel.getId(), submodel);
		return submodel;
	}

	@Override
	public AssetAdministrationShellDescriptor persist(AssetAdministrationShellDescriptor descriptor) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteSubmodelById(String submodelIdentifier) {
		this.submodel.remove(submodelIdentifier);

	}

	@Override
	public void deleteAssetAdministrationShellById(String aasIdentifier) {
		this.assetAdministrationShell.remove(aasIdentifier);
	}

	@Override
	public void deleteConceptDescriptionById(String cdIdentifier) {
		this.conceptDescription.remove(cdIdentifier);

	}

	@Override
	public void deleteAssetAdministrationShellDescriptorById(String descriptorIdentifier) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Optional<ConceptDescription> findConceptDescriptionById(String cdIdentifier) {
		if ( conceptDescription.containsKey(cdIdentifier)) {
			return Optional.of(conceptDescription.get(cdIdentifier));
		}
		return Optional.empty();
	}

	@Override
	public Optional<AssetAdministrationShell> findAssetAdministrationShellById(String aasIdentifier) {
		if ( assetAdministrationShell.containsKey(aasIdentifier)) {
			return Optional.of(assetAdministrationShell.get(aasIdentifier));
		}
		return Optional.empty();
	}

	@Override
	public Optional<Submodel> findSubmodelById(String submodelIdentifier) {
		if ( submodel.containsKey(submodelIdentifier)) {
			return Optional.of(submodel.get(submodelIdentifier));
		}
		return Optional.empty();
	}

	@Override
	public Optional<AssetAdministrationShellDescriptor> findAssetAdministrationShellDescriptorById(String id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<AssetAdministrationShell> getAssetAdministrationShells() {
		return assetAdministrationShell.values().stream().collect(Collectors.toList());
	}

	@Override
	public void setAssetAdministrationShells(List<AssetAdministrationShell> shells) {
		this.assetAdministrationShell.putAll(
				shells.stream()
				.collect(Collectors.toMap(AssetAdministrationShell::getId, a->a)));
		
	}

	@Override
	public void setSubmodels(List<Submodel> submodels) {
		this.submodel.putAll(
				submodels.stream()
				.collect(Collectors.toMap(Submodel::getId, s -> s)));
		
	}

	@Override
	public void setConceptDescriptions(List<ConceptDescription> conceptDescriptions) {
		this.conceptDescription.putAll(
				conceptDescriptions.stream()
				.collect(Collectors.toMap(ConceptDescription::getId, s -> s)));
		
	}

	@Override
	public List<ConceptDescription> getConceptDescriptions() {
		return conceptDescription.values().stream().collect(Collectors.toList());
	}

	@Override
	public List<Submodel> getSubmodels() {
		return submodel.values().stream().collect(Collectors.toList());
	}


}

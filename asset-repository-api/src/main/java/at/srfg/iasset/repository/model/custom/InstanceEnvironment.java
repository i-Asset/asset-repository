package at.srfg.iasset.repository.model.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.DataSpecification;
import org.eclipse.aas4j.v3.model.Environment;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;

import at.srfg.iasset.repository.utils.ReferenceUtils;

public class InstanceEnvironment implements Environment {
	
	private final Map<String, AssetAdministrationShell> assetAdministrationShell = new HashMap<String, AssetAdministrationShell>();
	private final Map<String, Submodel> submodel = new HashMap<String, Submodel>();
	private final Map<String, ConceptDescription> conceptDescription = new HashMap<String, ConceptDescription>();

	@Override
	public List<AssetAdministrationShell> getAssetAdministrationShells() {
		return assetAdministrationShell.values().stream().collect(Collectors.toList());
	}
	public void addAssetAdministrationShell(String id, AssetAdministrationShell shell) {
		shell.setId(id);
		assetAdministrationShell.put(id, shell);
	}
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String aasIdentifier) {
		return Optional.ofNullable(assetAdministrationShell.get(aasIdentifier));
	}
	

	@Override
	public void setAssetAdministrationShells(List<AssetAdministrationShell> assetAdministrationShells) {
		assetAdministrationShells.clear();
		for ( AssetAdministrationShell aas : assetAdministrationShells) {
			assetAdministrationShell.put(aas.getId(), aas);
		}
	}
	public boolean deleteAssetAdministrationShell(String aasIdentifier) {
		AssetAdministrationShell s = assetAdministrationShell.remove(aasIdentifier);
		// 
		return s != null;
	}
	@Override
	public List<Submodel> getSubmodels() {
		return submodel.values().stream().collect(Collectors.toList());
	}
	public void addSubmodel(String id, Submodel sub) {
		sub.setId(id);
		submodel.put(id, sub);
	}
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> aas = getAssetAdministrationShell(aasIdentifier);
		if ( aas.isPresent()) {
			// TODO: check that aas contains submodel
			return Optional.ofNullable(submodel.get(submodelIdentifier));
		}
		return Optional.empty();
	}
	/**
	 * Remove a submodel from the environment
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @return
	 */
	public boolean deleteSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> shell = getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent() ) {
			AssetAdministrationShell theShell = shell.get();
			Optional<Reference> subRef = ReferenceUtils.getReference(theShell.getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL);
			if ( subRef.isPresent() ) {
				theShell.getSubmodels().remove(subRef.get());
				// TODO: check for references of other submodels
				Submodel s = submodel.remove(submodelIdentifier);
				return s != null;
			}
		}
		return false;
		
		// 
		// 
		
	}

	@Override
	public void setSubmodels(List<Submodel> submodels) {
		submodel.clear();
		for ( Submodel sub : submodels) {
			submodel.put(sub.getId(), sub);
		}
		
	}

	@Override
	public List<ConceptDescription> getConceptDescriptions() {
		return conceptDescription.values().stream().collect(Collectors.toList());

	}
	public void addConceptDescription(ConceptDescription cd) {
		conceptDescription.put(cd.getId(), cd);
	}
	@Override
	public void setConceptDescriptions(List<ConceptDescription> conceptDescriptions) {
		conceptDescription.clear();
		for ( ConceptDescription cd : conceptDescriptions) {
			conceptDescription.put(cd.getId(), cd);
		}
		
	}

	@Override
	public List<DataSpecification> getDataSpecifications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataSpecifications(List<DataSpecification> dataSpecifications) {
		// TODO Auto-generated method stub
		
	}
	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		return Optional.ofNullable(conceptDescription.get(identifier));
	}


}

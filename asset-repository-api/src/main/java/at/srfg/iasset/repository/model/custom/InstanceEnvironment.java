package at.srfg.iasset.repository.model.custom;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetKind;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Environment;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;

import at.srfg.iasset.repository.component.ModelChangeProvider;
import at.srfg.iasset.repository.component.Persistence;
import at.srfg.iasset.repository.model.InMemoryStorage;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class InstanceEnvironment implements Environment {
	Persistence storage;
	
	private final ModelChangeProvider changeProvider;
	public InstanceEnvironment(ModelChangeProvider provider) {
		this.storage = new InMemoryStorage();
		this.changeProvider = provider;
	}
	@Override
	public List<AssetAdministrationShell> getAssetAdministrationShells() {
		return storage.getAssetAdministrationShells();
	}
	public List<AssetAdministrationShell> getAssetAdministrationShells(AssetKind kind) {
		return getAssetAdministrationShells().stream().filter(new Predicate<AssetAdministrationShell>() {

			@Override
			public boolean test(AssetAdministrationShell t) {
				if ( t.getAssetInformation()== null) {
					return false;
				}
				return kind.equals(t.getAssetInformation().getAssetKind());
			}
		})
		.collect(Collectors.toList());
	}
	public void addAssetAdministrationShell(String id, AssetAdministrationShell shell) {
		shell.setId(id);
		storage.persist(shell);
	}
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String aasIdentifier) {
		return storage.findAssetAdministrationShellById(aasIdentifier);
	}
	

	@Override
	public void setAssetAdministrationShells(List<AssetAdministrationShell> assetAdministrationShells) {
		storage.setAssetAdministrationShells(assetAdministrationShells);
	}
	public boolean deleteAssetAdministrationShell(String aasIdentifier) {
		storage.deleteAssetAdministrationShellById(aasIdentifier);
		return true;
	}
	@Override
	public List<Submodel> getSubmodels() {
		return storage.getSubmodels();
	}
	
	public void setSubmodel(String id, Submodel sub) {
		Optional<Submodel> existing = storage.findSubmodelById(id);
		existing.ifPresent(new Consumer<Submodel>() {

			@Override
			public void accept(Submodel t) {
				// 
			}
		});
		sub.setId(id);
		Submodel stored = storage.persist(sub);
	}
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShell> aas = getAssetAdministrationShell(aasIdentifier);
		if ( aas.isPresent()) {
			if ( ReferenceUtils.extractReferenceFromList(aas.get().getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL).isPresent() ) {
				return storage.findSubmodelById(submodelIdentifier);
			}
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
			if ( ReferenceUtils.extractReferenceFromList(shell.get().getSubmodels(), submodelIdentifier, KeyTypes.SUBMODEL).isPresent() ) {
				storage.deleteSubmodelById(submodelIdentifier);
				return true;
			}
		}
		return false;
		
		// 
		// 
		
	}

	@Override
	public void setSubmodels(List<Submodel> submodels) {
		for ( Submodel sub : submodels) {
			setSubmodel(sub.getId(), sub);
		}
	}

	@Override
	public List<ConceptDescription> getConceptDescriptions() {
		return storage.getConceptDescriptions();

	}
	public void setConceptDescription(ConceptDescription cd) {
		
		storage.persist(cd);
	}
	@Override
	public void setConceptDescriptions(List<ConceptDescription> conceptDescriptions) {
		for (ConceptDescription cd : conceptDescriptions) {
			setConceptDescription(cd);
		}
		
	}


	public Optional<ConceptDescription> getConceptDescription(String identifier) {
		Optional<ConceptDescription> conceptDescription = storage.findConceptDescriptionById(identifier);
		return conceptDescription;
	}

	public <T extends Referable> Optional<T> resolveReference(Reference reference, Class<T> clazz) {
		//AasUtils.resolve(reference, storage, clazz)
		//
		return Optional.empty();
	}
	public Optional<Submodel> getSubmodel(String subodelIdentifier) {
		Optional<Submodel> submodel = storage.findSubmodelById(subodelIdentifier);
		if ( submodel.isEmpty()) {
		}
		return submodel;
	}
}

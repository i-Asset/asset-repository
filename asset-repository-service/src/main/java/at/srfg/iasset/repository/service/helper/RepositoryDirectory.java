package at.srfg.iasset.repository.service.helper;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultSubmodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.srfg.iasset.repository.component.DirectoryService;
import at.srfg.iasset.repository.component.Persistence;

@Component
public class RepositoryDirectory implements DirectoryService {
	@Autowired
	private Persistence storage;
	

	@Override
	public void removeShellDescriptor(String aasIdentifier) {
		storage.deleteAssetAdministrationShellDescriptorById(aasIdentifier);
		
	}

	@Override
	public void removeSubmodelDescriptors(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShellDescriptor> descriptor = storage.findAssetAdministrationShellDescriptorById(aasIdentifier);
		if ( descriptor.isPresent()) {
			AssetAdministrationShellDescriptor theShellDescriptor = descriptor.get();
			List<SubmodelDescriptor> submodelDescriptors = theShellDescriptor.getSubmodelDescriptors().stream()
				.filter(new Predicate<SubmodelDescriptor>() {

					@Override
					public boolean test(SubmodelDescriptor t) {
						return ! submodelIdentifier.equalsIgnoreCase(t.getId());
					}})
				.collect(Collectors.toList());
			theShellDescriptor.setSubmodelDescriptors(submodelDescriptors);
			storage.persist(theShellDescriptor);
		}
		
	}

	@Override
	public Optional<AssetAdministrationShellDescriptor> getShellDescriptor(String aasIdentifier) {
		return storage.findAssetAdministrationShellDescriptorById(aasIdentifier);
	}

	@Override
	public Optional<SubmodelDescriptor> getSubmodelDescriptor(String aasDescriptor,
			String submodelIdentifier) {
		Optional<AssetAdministrationShellDescriptor> desc = storage.findAssetAdministrationShellDescriptorById(aasDescriptor);
		if ( desc.isPresent()) {
			return desc.get().getSubmodelDescriptors().stream()
					.filter(new Predicate<SubmodelDescriptor>() {

						@Override
						public boolean test(SubmodelDescriptor t) {
							return t.getId().equalsIgnoreCase(submodelIdentifier);
						}
					})
					.findAny();
		}
		return Optional.empty();
	}

	@Override
	public Optional<AssetAdministrationShellDescriptor> getShellDescriptorBySupplementalSemanticId(
			String supplementalSemanticId) {
		return storage.findAssetAdministrationShellDescriptorBySupplementalSemanticId(supplementalSemanticId);
		
	}

	@Override
	public AssetAdministrationShell registerShellDescriptor(String aasIdentifier, AssetAdministrationShellDescriptor shell) {
		// 
		Optional<AssetAdministrationShellDescriptor> desc = storage.findAssetAdministrationShellDescriptorById(aasIdentifier);
		// DECIDE what to do when present
		AssetAdministrationShellDescriptor descriptor = storage.persist(shell);
		// TODO: check what to copy from descrptor
		AssetAdministrationShell theShell = new DefaultAssetAdministrationShell.Builder()
				.administration(descriptor.getAdministration())
				.id(descriptor.getId())
				.idShort(descriptor.getIdShort())
				.descriptions(descriptor.getDescriptions())
				.displayNames(descriptor.getDisplayNames())
				.assetInformation(new DefaultAssetInformation.Builder()
						.globalAssetId(descriptor.getGlobalAssetId())
						.specificAssetIds(descriptor.getSpecificAssetIds())
						.build())
				.build();
		
		
		return storage.persist(theShell);
	}

	@Override
	public Submodel registerSubmodelDescriptor(String aasIdentifier, String submodelIdentifier, SubmodelDescriptor model) {
		Optional<AssetAdministrationShellDescriptor> descriptor = storage.findAssetAdministrationShellDescriptorById(aasIdentifier);
		if ( descriptor.isPresent()) {
			AssetAdministrationShellDescriptor theShellDescriptor = descriptor.get();
			List<SubmodelDescriptor> descriptors = theShellDescriptor.getSubmodelDescriptors().stream()
					.filter(new Predicate<SubmodelDescriptor>() {

						@Override
						public boolean test(SubmodelDescriptor t) {
							return ! t.getId().equalsIgnoreCase(model.getId());
						}
					})
					.collect(Collectors.toList());

			descriptors.add(model);
			theShellDescriptor.setSubmodelDescriptors(descriptors);
			storage.persist(theShellDescriptor);
			
			Optional<Submodel> sub = storage.findSubmodelById(model.getId());
			if (sub.isEmpty()) {
				// TODO: check what to copy from descriptor
				Submodel submodel = new DefaultSubmodel.Builder()
						.descriptions(model.getDescriptions())
						.displayNames(model.getDisplayNames())
						.idShort(model.getIdShort())
						.id(model.getId())
						.administration(model.getAdministration())
						.category(model.getCategory())
						.build();
				return storage.persist(submodel);
			}
			else {
				return sub.get();
			}
		}
		return null;
	}
	

}

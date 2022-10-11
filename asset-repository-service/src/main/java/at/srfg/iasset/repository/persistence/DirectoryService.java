package at.srfg.iasset.repository.persistence;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelDescriptor;
import org.eclipse.aas4j.v3.model.impl.DefaultAssetAdministrationShell;
import org.eclipse.aas4j.v3.model.impl.DefaultAssetInformation;
import org.eclipse.aas4j.v3.model.impl.DefaultSubmodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellDescriptorRepository;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellRepository;
import at.srfg.iasset.repository.persistence.service.SubmodelRepository;

@Component
public class DirectoryService {

	@Autowired
	private AssetAdministrationShellDescriptorRepository assetAdministrationShellDescriptorRepository;
	
	@Autowired
	private AssetAdministrationShellRepository assetAdministrationShellRepository;
	
	@Autowired
	private SubmodelRepository submodelRepository;

	
	public void removeShellDescriptor(String aasIdentifier) {
		assetAdministrationShellDescriptorRepository.deleteById(aasIdentifier);
		
	}

	public void removeSubmodelDescriptors(String aasIdentifier, String submodelIdentifier) {
		Optional<AssetAdministrationShellDescriptor> descriptor = assetAdministrationShellDescriptorRepository.findById(aasIdentifier);
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
			assetAdministrationShellDescriptorRepository.save(theShellDescriptor);
		}
		
	}

	public Optional<AssetAdministrationShellDescriptor> getShellDescriptor(String aasIdentifier) {
		return assetAdministrationShellDescriptorRepository.findById(aasIdentifier);
	}

	public Optional<SubmodelDescriptor> getSubmodelDescriptor(String aasDescriptor,
			String submodelIdentifier) {
		Optional<AssetAdministrationShellDescriptor> desc = assetAdministrationShellDescriptorRepository.findById(aasDescriptor);
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

	public AssetAdministrationShell registerShellDescriptor(String aasIdentifier, AssetAdministrationShellDescriptor shell) {
		// 
		Optional<AssetAdministrationShellDescriptor> desc = assetAdministrationShellDescriptorRepository.findById(aasIdentifier);
		// DECIDE what to do when present
		AssetAdministrationShellDescriptor descriptor = assetAdministrationShellDescriptorRepository.save(shell);
		// TODO: check what to copy from descrptor
		AssetAdministrationShell theShell = new DefaultAssetAdministrationShell.Builder()
				.administration(descriptor.getAdministration())
				.id(descriptor.getId())
				.idShort(descriptor.getIdShort())
				.descriptions(descriptor.getDescriptions())
				.displayNames(descriptor.getDisplayNames())
				.assetInformation(new DefaultAssetInformation.Builder()
						.globalAssetId(descriptor.getGlobalAssetId())
						.specificAssetId(descriptor.getSpecificAssetId())
						.build())
				.build();
		
		
		return assetAdministrationShellRepository.save(theShell);
	}

	public Submodel registerSubmodelDescriptor(String aasIdentifier, String submodelIdentifier, SubmodelDescriptor model) {
		Optional<AssetAdministrationShellDescriptor> descriptor = assetAdministrationShellDescriptorRepository.findById(aasIdentifier);
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
			assetAdministrationShellDescriptorRepository.save(theShellDescriptor);
			
			Optional<Submodel> sub = submodelRepository.findById(model.getId());
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
				return submodelRepository.save(submodel);
			}
			else {
				return sub.get();
			}
		}
		return null;
	}
	

}

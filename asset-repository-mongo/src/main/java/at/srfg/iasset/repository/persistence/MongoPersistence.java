package at.srfg.iasset.repository.persistence;

import java.util.List;
import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.DataSpecification;
import org.eclipse.aas4j.v3.model.Submodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.srfg.iasset.repository.component.Persistence;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellDescriptorRepository;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellRepository;
import at.srfg.iasset.repository.persistence.service.ConceptDescriptionRepository;
import at.srfg.iasset.repository.persistence.service.SubmodelRepository;

@Component
public class MongoPersistence implements Persistence {
	@Autowired
	private AssetAdministrationShellRepository aasRepo;
	@Autowired
	private ConceptDescriptionRepository cdRepo;
	@Autowired
	private SubmodelRepository submodelRepo;
	@Autowired
	private AssetAdministrationShellDescriptorRepository descriptorRepo;
	

	@Override
	public AssetAdministrationShell persist(AssetAdministrationShell assetAdministrationShell) {
		return aasRepo.save(assetAdministrationShell);
	}

	@Override
	public ConceptDescription persist(ConceptDescription conceptDescription) {
		return cdRepo.save(conceptDescription);
	}

	@Override
	public Submodel persist(Submodel submodel) {
		return submodelRepo.save(submodel);
	}

	@Override
	public AssetAdministrationShellDescriptor persist(AssetAdministrationShellDescriptor descriptor) {
		return descriptorRepo.save(descriptor);
	}

	@Override
	public void deleteSubmodelById(String submodelIdentifier) {
		submodelRepo.deleteById(submodelIdentifier);

	}

	@Override
	public void deleteAssetAdministrationShellById(String aasIdentifier) {
		aasRepo.deleteById(aasIdentifier);

	}

	@Override
	public void deleteConceptDescriptionById(String cdIdentifier) {
		cdRepo.deleteById(cdIdentifier);

	}

	@Override
	public void deleteAssetAdministrationShellDescriptorById(String descriptorIdentifier) {
		descriptorRepo.deleteById(descriptorIdentifier);

	}

	@Override
	public Optional<ConceptDescription> findConceptDescriptionById(String cdIdentifier) {
		return cdRepo.findById(cdIdentifier);
	}

	@Override
	public Optional<AssetAdministrationShell> findAssetAdministrationShellById(String aasIdentifier) {
		return aasRepo.findById(aasIdentifier);
	}

	@Override
	public Optional<Submodel> findSubmodelById(String submodel) {
		return submodelRepo.findById(submodel);
	}

	@Override
	public Optional<AssetAdministrationShellDescriptor> findAssetAdministrationShellDescriptorById(String id) {
		return descriptorRepo.findById(id);
	}

	@Override
	public List<AssetAdministrationShell> getAssetAdministrationShells() {
		return aasRepo.findAll();
	}

	@Override
	public void setAssetAdministrationShells(List<AssetAdministrationShell> shells) {
		aasRepo.saveAll(shells);
		
	}

	@Override
	public void setSubmodels(List<Submodel> submodels) {
		submodelRepo.saveAll(submodels);
	}

	@Override
	public void setConceptDescriptions(List<ConceptDescription> conceptDescriptions) {
//		cdRepo.deleteAll();
		cdRepo.saveAll(conceptDescriptions);
		
	}

	@Override
	public List<ConceptDescription> getConceptDescriptions() {
		return cdRepo.findAll();
	}

	@Override
	public List<Submodel> getSubmodels() {
		return submodelRepo.findAll();
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

}

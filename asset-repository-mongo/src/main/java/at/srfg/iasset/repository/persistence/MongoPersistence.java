package at.srfg.iasset.repository.persistence;

import java.util.List;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
	
	@Autowired
	private MongoTemplate template;
	

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
//	public List<AssetAdministrationShellDescriptor> findDescriptorBySemanticId(String semanticId) {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("submodel").is("value"));
//		template.find(query,AssetAdministrationShellDescriptor.class );
//	}

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
	public Optional<AssetAdministrationShellDescriptor> findAssetAdministrationShellDescriptorBySupplementalSemanticId(
			String supplemental) {
		Query query = Query.query(Criteria.where("submodelDescriptors.supplementalSemanticIds.keys.value").is(supplemental));
		List<AssetAdministrationShellDescriptor> result = template.find(query, AssetAdministrationShellDescriptor.class);
		if ( result.isEmpty() ) {
			return Optional.empty();
		}
		else if ( result.size() == 1) {
			return Optional.of(result.get(0));
		}
		else {
			throw new IllegalStateException(String.format("Multiple descriptors for semantic ID \"%s\" present!", supplemental));
		}
		
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


}

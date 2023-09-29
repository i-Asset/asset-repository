package at.srfg.iasset.repository.persistence.startup;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.srfg.iasset.repository.model.AASFaultSubmodel;
import at.srfg.iasset.repository.persistence.model.AssetAdministrationShellData;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellDataRepository;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellRepository;
import at.srfg.iasset.repository.persistence.service.ConceptDescriptionRepository;
import at.srfg.iasset.repository.persistence.service.SubmodelRepository;
import jakarta.annotation.PostConstruct;

@Component
public class ConceptDescriptionLoader {
	@Autowired
	AssetAdministrationShellRepository assetAdministrationShellRepository;
	@Autowired
	AssetAdministrationShellDataRepository assetAdministrationShellDataRepository;

	@Autowired
	ConceptDescriptionRepository conceptDescriptionRepository;

	@Autowired
	SubmodelRepository submodelRepository;
	
	@PostConstruct
	public void loadData() {
		// Sample - Test data

    	// i-Twin specific
    	AssetAdministrationShell s5 = ApplicationTypes.createRootApplication();
    	assetAdministrationShellRepository.save(s5);
    	
    	Submodel sm8 = ApplicationTypes.createSubmodelForInfoModel();
    	submodelRepository.save(sm8);
    	
    	Submodel sm9 = ApplicationTypes.createSubmodelForRootEventConfiguration();
    	submodelRepository.save(sm9);
    	
    	ConceptDescription mBroker = ApplicationTypes.createConceptDescriptionForMessageBroker();
    	conceptDescriptionRepository.save(mBroker);
    	ConceptDescription mBrokerType = ApplicationTypes.createConceptDescriptionForMessageBrokerBrokerType();
    	conceptDescriptionRepository.save(mBrokerType);
    	ConceptDescription mBrokerHosts = ApplicationTypes.createConceptDescriptionForMessageBrokerHosts();
    	conceptDescriptionRepository.save(mBrokerHosts);
    	ConceptDescription sensors = ApplicationTypes.createConceptDescriptionForSensorEventType();
    	conceptDescriptionRepository.save(sensors);
    	
    	// belt definiton
    	assetAdministrationShellRepository.save(ApplicationTypes.createTypeAasForBelt());
    	submodelRepository.save(ApplicationTypes.createSubmodelForBeltInfo());
    	submodelRepository.save(ApplicationTypes.createSubmodelForBeltProperties());
    	conceptDescriptionRepository.save(ApplicationTypes.createConceptDescriptionForManufacturerName());
    	
    	
    	submodelRepository.save(AASFaultSubmodel.SUBMODEL_FAULT1);
    	
    	submodelRepository.save(AASFaultSubmodel.SUBMODEL_FAULT_OPERATIONS);
	}


}

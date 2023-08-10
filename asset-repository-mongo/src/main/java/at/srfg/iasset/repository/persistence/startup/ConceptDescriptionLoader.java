package at.srfg.iasset.repository.persistence.startup;

import jakarta.annotation.PostConstruct;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Submodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.srfg.iasset.repository.model.AASFaultSubmodel;
import at.srfg.iasset.repository.persistence.model.AssetAdministrationShellData;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellDataRepository;
import at.srfg.iasset.repository.persistence.service.AssetAdministrationShellRepository;
import at.srfg.iasset.repository.persistence.service.ConceptDescriptionRepository;
import at.srfg.iasset.repository.persistence.service.SubmodelRepository;

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
    	AssetAdministrationShell s1 = AASFull.createAAS1();
    	assetAdministrationShellRepository.save(s1);
    	assetAdministrationShellDataRepository.save(new AssetAdministrationShellData(s1));
    	
    	AssetAdministrationShell s2 = AASFull.createAAS2();
    	assetAdministrationShellRepository.save(s2);
    	AssetAdministrationShell s3 = AASFull.createAAS3();
    	assetAdministrationShellRepository.save(s3);
    	AssetAdministrationShell s4 = AASFull.createAAS4();
    	assetAdministrationShellRepository.save(s4);
    	
    	
    	Submodel sm1 = AASFull.createSubmodel1();
    	submodelRepository.save(sm1);
    	Submodel sm2 = AASFull.createSubmodel2();
    	submodelRepository.save(sm2);
    	Submodel sm3 = AASFull.createSubmodel3();
    	submodelRepository.save(sm3);
    	Submodel sm4 = AASFull.createSubmodel4();
    	submodelRepository.save(sm4);
    	Submodel sm5 = AASFull.createSubmodel5();
    	submodelRepository.save(sm5);
    	Submodel sm6 = AASFull.createSubmodel6();
    	submodelRepository.save(sm6);
    	Submodel sm7 = AASFull.createSubmodel7();
    	submodelRepository.save(sm7);
    	
    	
    	ConceptDescription cd1 = AASFull.createConceptDescription1();
    	conceptDescriptionRepository.save(cd1);
    	ConceptDescription cd2 = AASFull.createConceptDescription2();
    	conceptDescriptionRepository.save(cd2);
    	ConceptDescription cd3 = AASFull.createConceptDescription3();
    	conceptDescriptionRepository.save(cd3);
    	ConceptDescription cd4 = AASFull.createConceptDescription4();
    	conceptDescriptionRepository.save(cd4);
    	
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

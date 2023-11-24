package at.srfg.iasset.repository.persistence.service;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(value =  "AssetAdministrationShellDescriptor")
public interface AssetAdministrationShellDescriptorRepository extends MongoRepository<AssetAdministrationShellDescriptor, String> {
}

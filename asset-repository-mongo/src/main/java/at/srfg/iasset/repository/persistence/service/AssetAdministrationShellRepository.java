package at.srfg.iasset.repository.persistence.service;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(value =  "AssetAdministrationShell")
public interface AssetAdministrationShellRepository extends MongoRepository<AssetAdministrationShell, String> {
}

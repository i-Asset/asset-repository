package at.srfg.iasset.repository.persistence.service;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import at.srfg.iasset.repository.persistence.model.AssetAdministrationShellData;

@Document(value =  "AssetAdministrationShellData")
public interface AssetAdministrationShellDataRepository extends MongoRepository<AssetAdministrationShellData, String> {
}

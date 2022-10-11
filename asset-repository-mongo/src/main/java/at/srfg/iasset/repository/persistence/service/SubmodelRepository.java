package at.srfg.iasset.repository.persistence.service;

import org.eclipse.aas4j.v3.model.Submodel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(value =  "ConceptDescription")
public interface SubmodelRepository extends MongoRepository<Submodel, String> {
}

package at.srfg.iasset.semantic.lookup.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.model.ConceptBase;

public interface ConceptRepository<T extends ConceptBase> extends CrudRepository<T, Long> {
	boolean existsByConceptId(String conceptId);
	Optional<T> findByConceptId(String conceptId);
	Optional<T> findByNameSpaceAndLocalName(String nameSpace, String localName);
	Optional<T> findBySupplierAndLocalName(String supplier, String localName);
	long deleteByNameSpace(String nameSpace);

}

package at.srfg.iasset.semantic.lookup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptClassProperty;
import at.srfg.iasset.semantic.model.ConceptClassPropertyPK;
import at.srfg.iasset.semantic.model.ConceptProperty;

public interface ConceptClassPropertyRepository extends CrudRepository<ConceptClassProperty, ConceptClassPropertyPK>{
	@Query("SELECT c.property FROM ConceptClassProperty c WHERE c.conceptClass = ?1 ")
	List<ConceptProperty> getProperties(ConceptClass conceptClass);
	/**
	 * Find the class specific property assignment
	 * @param cClass
	 * @param property
	 * @return
	 */
	Optional<ConceptClassProperty> findByConceptClassAndProperty(ConceptClass cClass, ConceptProperty property);

}

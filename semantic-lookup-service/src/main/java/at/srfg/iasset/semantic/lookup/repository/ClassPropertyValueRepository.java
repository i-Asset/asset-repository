package at.srfg.iasset.semantic.lookup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptClassPropertyValue;
import at.srfg.iasset.semantic.model.ConceptClassPropertyValuePK;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

public interface ClassPropertyValueRepository extends CrudRepository<ConceptClassPropertyValue, ConceptClassPropertyValuePK>{
	@Query("SELECT c.propertyValue FROM ConceptClassPropertyValue c WHERE c.classProperty.conceptClass = ?1 AND c.classProperty.property = ?2 ")
	List<ConceptPropertyValue> findByConceptClassAndProperty(ConceptClass classId, ConceptProperty propertyId);
}

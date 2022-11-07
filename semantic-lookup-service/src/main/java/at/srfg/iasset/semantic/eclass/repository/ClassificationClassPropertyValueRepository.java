package at.srfg.iasset.semantic.eclass.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.eclass.model.ClassificationClass;
import at.srfg.iasset.semantic.eclass.model.ClassificationClassPropertyValue;
import at.srfg.iasset.semantic.eclass.model.ClassificationClassPropertyValuePK;
import at.srfg.iasset.semantic.eclass.model.PropertyDefinition;

public interface ClassificationClassPropertyValueRepository extends CrudRepository<ClassificationClassPropertyValue, ClassificationClassPropertyValuePK> {
	@Query("SELECT c FROM ClassificationClassPropertyValue c WHERE c.property =?1 and c.classificationClass.codedName LIKE ?2 ")
	List<ClassificationClassPropertyValue> findByPropertyDefinitionAndCodedName(PropertyDefinition pr, String coded);
	@Query("SELECT c.valueConstraint as valueConstraint, c.id.irdiVA as value, COUNT(c) as classCount FROM ClassificationClassPropertyValue c WHERE c.property = ?1 and c.classificationClass.codedName LIKE ?2 GROUP BY c.valueConstraint, c.id.irdiVA ")
	List<Object[]> getPropertyValueAndCodedNameLike(PropertyDefinition pr, String coded);

	List<ClassificationClassPropertyValue> findByClassificationClassAndProperty(ClassificationClass cc, PropertyDefinition pr);
	
	@Query("SELECT c.id.irdiVA, count(c) FROM ClassificationClassPropertyValue c WHERE c.id.irdiPR = ?1 AND c.classificationClass.codedName LIKE ?2 AND c.valueConstraint = ?3 GROUP BY c.id.irdiVA, c.valueConstraint HAVING COUNT(c.id.irdiVA) = ?4 ")
	List<Object[]> findValuesForPropertyAndCodedName(String irdiPr, String codedNameLike, String constraint, long usage);
	
	@Query(value=
			  "SELECT  irdiva as irdiva, min(usageCount) as min, max(usageCount) as max "
			+ "FROM ( SELECT irdiva, irdipr, count(irdicc) as usageCount FROM eclass_classification_class_property_value "
			+ "       WHERE irdipr = ?1 AND value_constraint =?2 "
			+ "       GROUP BY irdiva, irdipr"
			+ "     ) AS s "
			+ "GROUP BY irdiva ", nativeQuery=true)
	List<Object[]> findValuesForProperty(String irdiPr, String constraint); 
	
}
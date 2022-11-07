package at.srfg.iasset.semantic.eclass.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.eclass.model.ClassificationClass;
import at.srfg.iasset.semantic.eclass.model.ClassificationClassProperty;
import at.srfg.iasset.semantic.eclass.model.ClassificationClassPropertyPK;
import at.srfg.iasset.semantic.eclass.model.PropertyDefinition;

public interface ClassificationClassPropertyRepository extends CrudRepository<ClassificationClassProperty, ClassificationClassPropertyPK> {
	int countByPropertyAndClassCodedNameLike(PropertyDefinition property, String classCodedName);
	List<ClassificationClassProperty> findByPropertyAndClassCodedNameLike(PropertyDefinition property, String classCodedName);
	
	List<ClassificationClassProperty> findByClassificationClassAndProperty(ClassificationClass cc, PropertyDefinition pr);
	@Query("SELECT DISTINCT(c.property) FROM ClassificationClassProperty c WHERE c.classCodedName LIKE ?1")
	List<PropertyDefinition> findDistinctPropertyByClassCodedNameLike(String classCodedNamePrefix);
	@Query("SELECT c.id.irdiPR AS irdiPR, COUNT(c) as usageCount FROM ClassificationClassProperty c WHERE c.classCodedName LIKE ?1 GROUP BY c.id.irdiPR HAVING COUNT(c) = ?2 ")
	List<Object[]> findPropertyWithUsageCount(String classCodedNamePrefix, long usageCount);
}
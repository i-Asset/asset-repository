package at.srfg.iasset.semantic.eclass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.eclass.model.ClassificationClass;
import at.srfg.iasset.semantic.eclass.model.PropertyDefinition;
/**
 * JPA Repository for working with {@link PropertyDefinition} entities.
 * @author dglachs
 *
 */
public interface PropertyRepository extends CrudRepository<PropertyDefinition, String>{
	Optional<PropertyDefinition> findByIrdiPR(String irdiPR);
	List<PropertyDefinition> findByIdPR(String idPR);
	List<PropertyDefinition> findByIdentifier(String identifier);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.idCC = ?1")
	List<PropertyDefinition> findByIdCC(String idCC);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.classCodedName = ?1")
	List<PropertyDefinition> findByClassCodedName(String classCodedName);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.classificationClass = ?1")
	List<PropertyDefinition> findByClassificationClass(ClassificationClass classificationClass);
	@Query("SELECT ccpr.property FROM ClassificationClassProperty ccpr WHERE ccpr.id.irdiCC = ?1")
	List<PropertyDefinition> findByIrdiCC(String irdiCC);
	/**
	 * Retrieve all properties by category
	 * @param category
	 * @return
	 */
	List<PropertyDefinition> findByCategory(String category);
	
}

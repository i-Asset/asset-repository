package at.srfg.iasset.semantic.eclass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.eclass.model.ClassificationClass;
import at.srfg.iasset.semantic.eclass.model.PropertyDefinition;
import at.srfg.iasset.semantic.eclass.model.PropertyValue;

public interface ValueRepository extends CrudRepository<PropertyValue, String>{
	PropertyValue findByIrdiVA(String irdiVA);
	List<PropertyValue> findByIdentifier(String eceCode);
	/**
	 * Retrieve the available/allowed values for the combination of {@link ClassificationClass} and
	 * {@link PropertyDefinition}.
	 * @param irdiCC The IRDI pointing to the {@link ClassificationClass}
	 * @param irdiPR The IRDI pointing to the {@link PropertyDefinition}
	 * @return
	 */
	@Query("SELECT ccprva.value FROM ClassificationClassPropertyValue ccprva WHERE ccprva.id.irdiCC = ?1 and ccprva.id.irdiPR =?2 ")
	List<PropertyValue> getValues(String irdiCC, String irdiPR);
	/**
	 * Retrieve the available/allowed values for the combination of {@link ClassificationClass} and
	 * {@link PropertyDefinition}.
	 * @param irdiCC The {@link ClassificationClass}
	 * @param irdiPR The {@link PropertyDefinition}
	 * @return
	 */
	@Query("SELECT ccprva.value FROM ClassificationClassPropertyValue ccprva WHERE ccprva.classificationClass = ?1 and ccprva.property =?2 ")
	List<PropertyValue> getValues(ClassificationClass classificationClass, PropertyDefinition property);
}

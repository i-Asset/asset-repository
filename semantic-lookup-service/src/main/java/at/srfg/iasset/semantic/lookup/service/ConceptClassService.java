package at.srfg.iasset.semantic.lookup.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

public interface ConceptClassService extends ConceptService<ConceptClass> {
	/**
	 * Retrieve all properties for a class
	 * @param identifier
	 * @return
	 */
	List<ConceptProperty> getProperties(String identifier);
	List<ConceptProperty> getProperties(String identifier, boolean complete);
	
	Optional<ConceptClass> addConcept(String parentConceptIdentifier, ConceptClass newConcept);

	Collection<ConceptProperty> setPropertiesById(String identifier, List<String> properties);
	Collection<ConceptProperty> setProperties(String identifier, List<ConceptProperty> properties);

	Collection<ConceptPropertyValue> setPropertyValuesForConceptClassById(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<String> propertyValueIds);

	Collection<ConceptPropertyValue> setPropertyValuesForConceptClass(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<ConceptPropertyValue> conceptPropertyList);
}

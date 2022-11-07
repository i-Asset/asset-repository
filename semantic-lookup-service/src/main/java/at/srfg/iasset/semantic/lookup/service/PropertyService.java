package at.srfg.iasset.semantic.lookup.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptClassProperty;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

public interface PropertyService extends ConceptService<ConceptProperty> {
	/**
	 * Retrieve all property-values for a given property and (optionally) for
	 * a provided class
	 * @param classIdentifier The identifier of the class - may be <code>null</code>
	 * @param propertyIdentifier The identifier of th property
	 * @return
	 */
	Set<ConceptPropertyValue> getValues(String classIdentifier, String propertyIdentifier);
	/**
	 * Retrieve all property-values for a given property and (optionally) for
	 * a provided class
	 * @param identifier
	 * @param classIdentifier
	 * @return
	 */
	Set<ConceptPropertyValue> getValues(ConceptProperty property, Optional<ConceptClass> classIdentifier);
	Set<ConceptPropertyValue> setValues(ConceptProperty property, List<ConceptPropertyValue> valueList);
	Set<ConceptPropertyValue> setValues(ConceptProperty property, Optional<ConceptClass> cClass, List<ConceptPropertyValue> valueList);
	Set<ConceptPropertyValue> setPropertyValuesById(String identifier, String classIdentifier, List<String> valueIdentifier);
	Set<ConceptPropertyValue> setPropertyValues(String identifier, String classIdentifier, List<ConceptPropertyValue> valueIdentifier);
	Collection<ConceptPropertyValue> getPropertyValues(String conceptPropertyIdentifier, String conceptClassIdentifier);
	/**
	 * Delete the provided valueIds from the property, when the <code>conceptClassIdentifier</code> is provided, the class specific value list is deleted
	 * @param conceptPropertyIdentifier The identifier of the property, whose value list is to be deleted!
	 * @param conceptClassIdentifier Optionally, a class identifier may point to a {@link ConceptClassProperty} holding the class specific values
	 * @param propertyValueList The identifiers of the values to delete
	 * @return The remaining value list
	 */
	Collection<ConceptPropertyValue> deletePropertyValues(String conceptPropertyIdentifier,
			String conceptClassIdentifier, List<String> propertyValueList);
}

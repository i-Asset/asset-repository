package at.srfg.iasset.repository.api;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.repository.api.dependency.SemanticLookup;
import at.srfg.iasset.semantic.api.SemanticLookupService;
import at.srfg.iasset.semantic.model.ConceptBase;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

@RestController
public class LookupDependencyController implements SemanticLookupService {
	@Autowired
	SemanticLookup lookup;

	@Override
	public Optional<ConceptBase> getConcept(String identifier) {

		return lookup.getConcept(identifier);
	}

	@Override
	public Optional<ConceptBase> addConcept(ConceptBase concept) {
		return lookup.addConcept(concept);
	}

	@Override
	public Optional<ConceptBase> setConcept(ConceptBase concept) {
		return lookup.setConcept(concept);
	}

	@Override
	public boolean deleteConcept(String identifier) {
		return lookup.deleteConcept(identifier);
	}

	@Override
	public Optional<ConceptClass> getConceptClass(String identifier) {
		return lookup.getConceptClass(identifier);
	}

	@Override
	public Optional<ConceptClass> addConceptClass(String parentIdentifier, ConceptClass conceptClass) {
		return lookup.addConceptClass(parentIdentifier, conceptClass);
	}

	@Override
	public Optional<ConceptClass> setConceptClass(ConceptClass conceptClass) {
		return lookup.setConceptClass(conceptClass);
	}

	@Override
	public boolean deleteConceptClass(List<String> identifier) {
		return lookup.deleteConceptClass(identifier);
	}

	@Override
	public Collection<ConceptProperty> getPropertiesForConceptClass(String identifier, boolean complete) {
		return lookup.getPropertiesForConceptClass(identifier, complete);
	}

	@Override
	public Collection<ConceptProperty> setPropertiesForConceptClass(String identifier,
			List<ConceptProperty> conceptPropertyList) {
		return lookup.setPropertiesForConceptClass(identifier, conceptPropertyList);
	}

	@Override
	public Collection<ConceptProperty> setPropertiesByIdForConceptClass(String identifier, List<String> propertyList) {
		return lookup.setPropertiesByIdForConceptClass(identifier, propertyList);
	}

	@Override
	public Collection<ConceptPropertyValue> getPropertyValues(String conceptClassIdentifier,
			String conceptPropertyIdentifier) {
		return lookup.getPropertyValues(conceptClassIdentifier, conceptPropertyIdentifier);
	}

	@Override
	public Collection<ConceptPropertyValue> setPropertyValuesForConceptClass(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<ConceptPropertyValue> conceptPropertyList) {
		return lookup.setPropertyValuesForConceptClass(conceptClassIdentifier, conceptPropertyIdentifier, conceptPropertyList);
	}

	@Override
	public Collection<ConceptPropertyValue> setPropertyValuesByIdForConceptClass(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<String> propertyValueList) {
		return lookup.setPropertyValuesByIdForConceptClass(conceptClassIdentifier, conceptPropertyIdentifier, propertyValueList);
	}

	@Override
	public Collection<ConceptPropertyValue> setPropertyValues(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<String> propertyValueList) {
		return lookup.setPropertyValues(conceptClassIdentifier, conceptPropertyIdentifier, propertyValueList);
	}

	@Override
	public Optional<ConceptProperty> getProperty(String identifier) {
		return lookup.getProperty(identifier);
	}

	@Override
	public Optional<ConceptProperty> addProperty(ConceptProperty property) {
		return lookup.addProperty(property);
	}

	@Override
	public Optional<ConceptProperty> setProperty(ConceptProperty property) {
		return lookup.setProperty(property);
	}

	@Override
	public boolean deleteProperty(List<String> identifier) {
		return lookup.deleteProperty(identifier);
	}

	@Override
	public Collection<ConceptPropertyValue> getPropertyValues(String identifier) {
		return lookup.getPropertyValues(identifier);
	}

	@Override
	public Optional<ConceptPropertyUnit> getPropertyUnit(String identifier) {
		return lookup.getPropertyUnit(identifier);
	}

	@Override
	public Optional<ConceptPropertyUnit> addPropertyUnit(ConceptPropertyUnit property) {
		return lookup.addPropertyUnit(property);
	}

	@Override
	public Optional<ConceptPropertyUnit> setPropertyUnit(ConceptPropertyUnit property) {
		return lookup.setPropertyUnit(property);
	}

	@Override
	public boolean deletePropertyUnit(List<String> identifier) {
		return lookup.deletePropertyUnit(identifier);
	}

	@Override
	public Optional<ConceptPropertyValue> getPropertyValue(String identifier) {
		return lookup.getPropertyValue(identifier);
	}

	@Override
	public Optional<ConceptPropertyValue> addPropertyValue(ConceptPropertyValue property) {
		return lookup.addPropertyValue(property);
	}

	@Override
	public Optional<ConceptPropertyValue> setPropertyValue(ConceptPropertyValue property) {
		return lookup.setPropertyValue(property);
	}

	@Override
	public boolean deletePropertyValue(List<String> id) {
		return lookup.deletePropertyValue(id);
	}


}

package at.srfg.iasset.repository.api;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.repository.api.dependency.SemanticLookup;
import at.srfg.iasset.repository.api.dependency.SemanticLookupMapper;
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
	public ConceptBase getConcept(String identifier) {
		Optional<ConceptBase> res = lookup.getConcept(identifier);
		return lookup.getConcept(identifier).orElse(null);
	}

	@Override
	public ConceptBase addConcept(ConceptBase concept) {
		return lookup.addConcept(concept).orElse(null);
	}

	@Override
	public ConceptBase setConcept(ConceptBase concept) {
		return lookup.setConcept(concept).orElse(null);
	}

	@Override
	public boolean deleteConcept(String identifier) {
		return lookup.deleteConcept(identifier);
	}

	@Override
	public ConceptClass getConceptClass(String identifier) {
		return lookup.getConceptClass(identifier).orElse(null);
	}

	@Override
	public ConceptClass addConceptClass(String parentIdentifier, ConceptClass conceptClass) {
		return lookup.addConceptClass(parentIdentifier, conceptClass).orElse(null);
	}

	@Override
	public ConceptClass setConceptClass(ConceptClass conceptClass) {
		return lookup.setConceptClass(conceptClass).orElse(null);
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
	public ConceptProperty getProperty(String identifier) {
		return lookup.getProperty(identifier).orElse(null);
	}

	@Override
	public ConceptProperty addProperty(ConceptProperty property) {
		return lookup.addProperty(property).orElse(null);
	}

	@Override
	public ConceptProperty setProperty(ConceptProperty property) {
		return lookup.setProperty(property).orElse(property);
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
	public ConceptPropertyUnit getPropertyUnit(String identifier) {
		return lookup.getPropertyUnit(identifier).orElse(null);
	}

	@Override
	public ConceptPropertyUnit addPropertyUnit(ConceptPropertyUnit property) {
		return lookup.addPropertyUnit(property).orElse(null);
	}

	@Override
	public ConceptPropertyUnit setPropertyUnit(ConceptPropertyUnit property) {
		return lookup.setPropertyUnit(property).orElse(property);
	}

	@Override
	public boolean deletePropertyUnit(List<String> identifier) {
		return lookup.deletePropertyUnit(identifier);
	}

	@Override
	public ConceptPropertyValue getPropertyValue(String identifier) {
		return lookup.getPropertyValue(identifier).orElse(null);
	}

	@Override
	public ConceptPropertyValue addPropertyValue(ConceptPropertyValue property) {
		return lookup.addPropertyValue(property).orElse(null);
	}

	@Override
	public ConceptPropertyValue setPropertyValue(ConceptPropertyValue property) {
		return lookup.setPropertyValue(property).orElse(property);
	}

	@Override
	public boolean deletePropertyValue(List<String> id) {
		return lookup.deletePropertyValue(id);
	}


}

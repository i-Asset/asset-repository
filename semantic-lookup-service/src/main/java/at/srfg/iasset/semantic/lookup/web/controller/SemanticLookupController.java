package at.srfg.iasset.semantic.lookup.web.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.semantic.api.SemanticLookupService;
import at.srfg.iasset.semantic.lookup.service.ConceptClassService;
import at.srfg.iasset.semantic.lookup.service.ConceptService;
import at.srfg.iasset.semantic.lookup.service.PropertyService;
import at.srfg.iasset.semantic.lookup.service.PropertyUnitService;
import at.srfg.iasset.semantic.lookup.service.PropertyValueService;
import at.srfg.iasset.semantic.model.ConceptBase;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

@RestController
public class SemanticLookupController implements SemanticLookupService {
//	@Autowired
//	private ClassService classService;
	@Autowired
	private ConceptClassService conceptService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private PropertyUnitService propertyUnitService;
	@Autowired
	private PropertyValueService propertyValueService;
	@Autowired
	private ConceptService<ConceptBase> conceptBase;
//	@Autowired
//	private ConceptService<ConceptClass> conceptClass;
//	@Autowired
//	private ConceptService<Property> property;
	

	@Override
	public Optional<ConceptClass> getConceptClass(String identifier) {
		return conceptService.getConcept(identifier);
	}

	@Override
	public Optional<ConceptBase> getConcept(String identifier) {
		// read the concept
		System.out.println("Search for "+identifier);
		return conceptBase.getConcept(identifier);
	}
	@Override
	public Optional<ConceptBase> addConcept(ConceptBase concept) {
		return conceptBase.addConcept(concept);
	}
	@Override
	public Optional<ConceptBase> setConcept(ConceptBase concept) {
		return conceptBase.setConcept(concept);
	}
	@Override
	public boolean deleteConcept(String identifier) {
		return conceptBase.deleteConcept(identifier);
	}
	@Override
	public Optional<ConceptClass> addConceptClass(String parentIdentifier, ConceptClass conceptClass) {
		return conceptService.addConcept(parentIdentifier, conceptClass);
	}
	@Override
	public Optional<ConceptClass> setConceptClass(ConceptClass conceptClass) {
		return conceptService.setConcept(conceptClass);
	}
	
	@Override
	public boolean deleteConceptClass(List<String> identifiers) {
		for (String id : identifiers) {
			conceptService.deleteConcept(id);
		}
		return true;
	}

	@Override
	public Collection<ConceptProperty> getPropertiesForConceptClass(String identifier, boolean complete) {
		return conceptService.getProperties(identifier, complete);
	}

	@Override
	public Optional<ConceptProperty> getProperty(String identifier) {
		return propertyService.getConcept(identifier);
	}

	@Override
	public Optional<ConceptProperty> addProperty(ConceptProperty property) {
		return propertyService.addConcept(property);
	}

	@Override
	public Optional<ConceptProperty> setProperty(ConceptProperty property) {
		return propertyService.setConcept(property);
	}

	@Override
	public boolean deleteProperty(List<String> identifiers) {
		
		for (String id : identifiers) {
			propertyService.deleteConcept(id);
		}
		return true;
	}
	
	@Override
	public Optional<ConceptPropertyUnit> getPropertyUnit(String identifier) {
		return propertyUnitService.getConcept(identifier);
	}

	@Override
	public Optional<ConceptPropertyUnit> addPropertyUnit(ConceptPropertyUnit propertyUnit) {
		return propertyUnitService.addConcept(propertyUnit);
	}

	@Override
	public Optional<ConceptPropertyUnit> setPropertyUnit(ConceptPropertyUnit PropertyUnit) {
		return propertyUnitService.setConcept(PropertyUnit);
	}

	@Override
	public boolean deletePropertyUnit(List<String> identifiers) {
		for (String id : identifiers ) {
			propertyUnitService.deleteConcept(id);
		}
		return true;
	}

	@Override
	public Optional<ConceptPropertyValue> getPropertyValue(String identifier) {
		return propertyValueService.getConcept(identifier);
	}

	@Override
	public Optional<ConceptPropertyValue> addPropertyValue(ConceptPropertyValue propertyUnit) {
		return propertyValueService.addConcept(propertyUnit);
	}

	@Override
	public Optional<ConceptPropertyValue> setPropertyValue(ConceptPropertyValue propertyValue) {
		return propertyValueService.setConcept(propertyValue);
	}

	@Override
	public boolean deletePropertyValue(List<String> identifiers) {
		for (String id : identifiers ) {
			propertyValueService.deleteConcept(id);
		}
		return true;
	}

	@Override
	public Collection<ConceptPropertyValue> setPropertyValuesForConceptClass(String conceptClassIdentifier,
			String conceptPropertyIdentifier, 
			List<ConceptPropertyValue> conceptPropertyList) {
		if ( conceptPropertyList != null && !conceptPropertyList.isEmpty()) {
			return propertyService.setPropertyValues(conceptPropertyIdentifier, conceptClassIdentifier, conceptPropertyList);
		}
		else {
			throw new IllegalArgumentException("Invalid usage: Provide either propertyId's or the full descriptions");
		}
	}

	@Override
	public Collection<ConceptProperty> setPropertiesForConceptClass(String identifier,
			List<ConceptProperty> conceptPropertyList) {
		if ( conceptPropertyList != null && !conceptPropertyList.isEmpty()) {
			return conceptService.setProperties(identifier, conceptPropertyList);
		}
		else {
			throw new IllegalArgumentException("Invalid usage: Provide either propertyId's or the full descriptions");
		}
	}

	@Override
	public Collection<ConceptProperty> setPropertiesByIdForConceptClass(String identifier, List<String> propertyList) {
		if ( propertyList != null && !propertyList.isEmpty()) {
			return conceptService.setPropertiesById(identifier, propertyList);
		}
		else {
			throw new IllegalArgumentException("Invalid usage: Provide either propertyId's or the full descriptions");
		}
	}

	@Override
	public Collection<ConceptPropertyValue> setPropertyValuesByIdForConceptClass(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<String> propertyValueList) {
		if ( propertyValueList != null && !propertyValueList.isEmpty()) {
			return propertyService.setPropertyValuesById(conceptPropertyIdentifier, conceptClassIdentifier, propertyValueList);
		}
		else {
			throw new IllegalArgumentException("Invalid usage: Provide either propertyId's or the full descriptions");
		}
	}

	@Override
	public Collection<ConceptPropertyValue> getPropertyValues(String classIdentifier, String propertyIdentifier) {
		return propertyService.getValues(classIdentifier, propertyIdentifier);
	}

	@Override
	public Collection<ConceptPropertyValue> getPropertyValues(String conceptPropertyIdentifier) {
		// get the property values without checking the class
		return propertyService.getPropertyValues(conceptPropertyIdentifier, null);
	}

	@Override
	public Collection<ConceptPropertyValue> setPropertyValues(String conceptClassIdentifier,
			String conceptPropertyIdentifier, List<String> propertyValueList) {
		// TODO Auto-generated method stub
		return propertyService.deletePropertyValues(conceptPropertyIdentifier, conceptClassIdentifier, propertyValueList);
	}
	
}

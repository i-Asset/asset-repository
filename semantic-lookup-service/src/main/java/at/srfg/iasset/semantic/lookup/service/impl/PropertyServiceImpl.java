package at.srfg.iasset.semantic.lookup.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.semantic.eclass.service.DataDuplicationService;
import at.srfg.iasset.semantic.lookup.repository.ClassPropertyValueRepository;
import at.srfg.iasset.semantic.lookup.repository.ConceptClassPropertyRepository;
import at.srfg.iasset.semantic.lookup.service.PropertyService;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptClassProperty;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

@Service
public class PropertyServiceImpl extends ConceptServiceImpl<ConceptProperty> implements PropertyService {
//	@Autowired
//	private SemanticIndexer indexer;
	
	@Autowired
	private ClassPropertyValueRepository classPropertyValueRepository;

	@Autowired
	private ConceptClassPropertyRepository classPropertyRepository;

	@Autowired
	private PropertyUnitServiceImpl propertyUnitService;

	@Autowired
	private PropertyValueServiceImpl propertyValueService;

	@Autowired
	private DataDuplicationService duplexer;
	
	public Optional<ConceptProperty> getConcept(String identifier) {
		Optional<ConceptProperty> ccOpt = typeRepository.findByConceptId(identifier);
		if (!ccOpt.isPresent()) {
			return duplexer.copyProperty(identifier);
			
		}
		else {
			return ccOpt;
		}
	}

	@Override
	public Optional<ConceptProperty> addConcept(ConceptProperty newConcept) {
		ConceptProperty toStore = new ConceptProperty(newConcept.getConceptId());
		toStore.setCategory(newConcept.getCategory());
		toStore.setCoded(newConcept.isCoded());
		toStore.setDataType(newConcept.getDataType());
		toStore.setRevisionNumber(newConcept.getRevisionNumber());
		toStore.setPreferredLabel(newConcept.getPreferredLabel());
		toStore.setAlternateLabel(newConcept.getAlternateLabel());
		toStore.setHiddenLabel(newConcept.getHiddenLabel());
		toStore.setNote(newConcept.getNote());
		toStore.setRemark(newConcept.getRemark());
		toStore.setShortName(newConcept.getShortName());
		toStore.setSourceOfDefinition(newConcept.getSourceOfDefinition());
		// 
		checkPropertyUnit(toStore, newConcept.getUnit());
		// 
		if ( newConcept.getValues() != null ) {
			checkPropertyValues(toStore, newConcept.getValues());
		}
		ConceptProperty stored = typeRepository.save(toStore);
//		indexer.store(toStore);
		return Optional.of(stored);
	}
	/**
	 * Helper method for assigning a {@link ConceptPropertyUnit} - will be created when not present
	 * @param property
	 * @param unit
	 */
	private void checkPropertyUnit(ConceptProperty property, ConceptPropertyUnit unit) {
		if (unit != null && ! isNullOrEmpty(unit.getConceptId())) {
			// check the provided unit (the identifier is important to find the stuff)
			Optional<ConceptPropertyUnit> stored = propertyUnitService.getConcept(unit.getConceptId());
			if ( stored.isPresent()) {
				property.setUnit(stored.get());
			}
			else {
				stored = propertyUnitService.addConcept(unit);
				if ( stored.isPresent()) {
					property.setUnit(stored.get());
				}
			}
		}
	}
	/**
	 * Helper method for assigning a {@link ConceptPropertyValue} - will be created when not preset
	 * @param property
	 * @param value
	 * @return
	 */
	private Optional<ConceptPropertyValue> checkPropertyValue(ConceptProperty property, ConceptPropertyValue value) {
		if (value != null && ! isNullOrEmpty(value.getConceptId())) {
			// check the provided unit (the identifier is important to find the stuff)
			Optional<ConceptPropertyValue> stored = propertyValueService.getConcept(value.getConceptId());
			if ( stored.isPresent()) {
				return stored;
			}
			else {
				return propertyValueService.addConcept(value);
			}
		}
		return Optional.empty();
	}
	/**
	 * Helper for processing the list of provided {@link ConceptPropertyValue} elements
 	 * @param property
	 * @param values
	 */
	private void checkPropertyValues(ConceptProperty property, Set<ConceptPropertyValue> values) {
		Set<ConceptPropertyValue> propValues = new HashSet<>();
		if ( values != null && ! values.isEmpty()) {
			for (ConceptPropertyValue value : values ) {
				Optional<ConceptPropertyValue> stored = checkPropertyValue(property, value);
				if ( stored.isPresent()) {
					propValues.add(stored.get());
				}
			}
			property.setValues(propValues);
		}
	}
	
	@Override
	public ConceptProperty setConcept(ConceptProperty property, ConceptProperty updated) {
		// description
		property.setPreferredLabel(updated.getPreferredLabel());
		property.setAlternateLabel(updated.getAlternateLabel());
		property.setHiddenLabel(updated.getHiddenLabel());
		// note
		if (! isNullOrEmpty(updated.getNote())) {
			property.setNote(updated.getNote());
		}
		// remark
		if (! isNullOrEmpty(updated.getRemark())) {
			property.setRemark(updated.getRemark());
		}
		// shortName
		if (! isNullOrEmpty(updated.getShortName())) {
			property.setShortName(updated.getShortName());
		}
		// category
		if (! isNullOrEmpty(updated.getCategory())) {
			property.setCategory(updated.getCategory());
		}
		// unit
		checkPropertyUnit(property, updated.getUnit());
		// values
		checkPropertyValues(property, updated.getValues());
		// store in database
		// also store in index
//		indexer.store(property);
		//
		return typeRepository.save(property);
	}

	@Override
	public Optional<ConceptProperty> setConcept(ConceptProperty updated) {
		Optional<ConceptProperty> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			return Optional.of(setConcept(stored.get(), updated));
		}
		else {
			return addConcept(updated);
		}
//		return Optional.empty();
	}

	@Override
	public Set<ConceptPropertyValue> getValues(String classIdentifier, String propertyIdentifier) {
		Optional<ConceptProperty> property = getConcept(propertyIdentifier);
		if (property.isPresent()) {
			Optional<ConceptClass> cClass = Optional.empty();
			if (! isNullOrEmpty(classIdentifier)) {
				 cClass = getConcept(classIdentifier, ConceptClass.class);
			}
			return getValues(property.get(), cClass);
		}		
		return new HashSet<>();
	}
	@Override
	public Set<ConceptPropertyValue> getValues(ConceptProperty property, Optional<ConceptClass> classIdentifier) {
		if ( classIdentifier.isPresent()) {
			
			List<ConceptPropertyValue> cClassPropertyValues 
				= classPropertyValueRepository.findByConceptClassAndProperty(classIdentifier.get(), property);
			if (! cClassPropertyValues.isEmpty()) {
				Set<ConceptPropertyValue> values = new HashSet<>();
				values.addAll(cClassPropertyValues);
				return values;
			}
		}
		return property.getValues();
		
	}
	@Override
	public Set<ConceptPropertyValue> setValues(ConceptProperty property, Optional<ConceptClass> cClass, List<ConceptPropertyValue> valueList) {
		Optional<ConceptClassProperty> cClassProperty = Optional.empty();
		
		if ( cClass.isPresent()) {
			cClassProperty = classPropertyRepository.findByConceptClassAndProperty(cClass.get(), property);
			ConceptClassProperty conceptClassProperty = cClassProperty.orElseGet(new Supplier<ConceptClassProperty>() {
				@Override
				public ConceptClassProperty get() {
					return new ConceptClassProperty(cClass.get(), property);
				}
			});
			return setValues(conceptClassProperty, valueList);
			
		}
		return setValues(property, valueList);
	}
	private Set<ConceptPropertyValue> setValues(ConceptClassProperty cClassProperty, List<ConceptPropertyValue> valueList) {
		for(ConceptPropertyValue value : valueList) {
			if (value.getId() != null) {
				// value is stored in the repo & no update is required!
				cClassProperty.addPropertyValue(value);
			}
			else {
				// need to read the stored 
				Optional<ConceptPropertyValue> stored = propertyValueService.getConcept(value.getConceptId());
				if (stored.isPresent()) {
					cClassProperty.addPropertyValue(propertyValueService.setConcept(stored.get(), value));
				}
			}
		}
		ConceptClassProperty saved = classPropertyRepository.save(cClassProperty);
		return saved.getPropertyValues();
				
	}
	@Override
	public Set<ConceptPropertyValue> setValues(ConceptProperty property, List<ConceptPropertyValue> valueList) {
		return setValues(property, Optional.empty(), valueList);
	}
	@Override
	public Set<ConceptPropertyValue> setPropertyValues(String identifier, String classIdentifier, List<ConceptPropertyValue> valueIdentifier) {
		Optional<ConceptProperty> property = getConcept(identifier);
		if (property.isPresent()) {
			Optional<ConceptClass> cClass  = Optional.empty();
			if (! isNullOrEmpty(classIdentifier)) {
				cClass = getConcept(classIdentifier, ConceptClass.class);
			}
			return setValues(property.get(), cClass, valueIdentifier);
		}
		throw new IllegalArgumentException(String.format("Concept Property with ID [%s] not found!", identifier));
		
	}
	@Override
	public Set<ConceptPropertyValue> setPropertyValuesById(String identifier, String classIdentifier, List<String> valueIdentifier) {
		List<ConceptPropertyValue> present = new ArrayList<ConceptPropertyValue>();
		Optional<ConceptProperty> property = getConcept(identifier);
		if (property.isPresent()) {
			Optional<ConceptClass> cClass  = Optional.empty();
			if (! isNullOrEmpty(classIdentifier)) {
				cClass = getConcept(classIdentifier, ConceptClass.class);
			}
			for (String valueId : valueIdentifier) {
				Optional<ConceptPropertyValue> value = propertyValueService.getConcept(valueId);
				if (value.isPresent()) {
					present.add(value.get());
				}
			}
			return setValues(property.get(),  cClass, present);
		}
		throw new IllegalArgumentException(String.format("Concept Property with ID [%s] not found!", identifier));
	}
	public boolean deleteConcept(String identifier) {
		Optional<ConceptProperty> property = typeRepository.findByConceptId(identifier);
		if (property.isPresent()) {
			typeRepository.delete(property.get());
//			indexer.remove(property.get());
			return true;
		}
		return false;
	}

	@Override
	public Collection<ConceptPropertyValue> getPropertyValues(String conceptPropertyIdentifier,
			String conceptClassIdentifier) {
		Optional<ConceptProperty> conceptProperty = getConcept(conceptPropertyIdentifier);
		if (conceptProperty.isPresent()) {
			Optional<ConceptClass> conceptClass = getConcept(conceptClassIdentifier, ConceptClass.class);
			return getValues(conceptProperty.get(), conceptClass);
		}
		throw new IllegalArgumentException(String.format("ConceptProperty with ID [%s] not found!", conceptPropertyIdentifier));
	}

	@Override
	public Collection<ConceptPropertyValue> deletePropertyValues(String conceptPropertyIdentifier,
			String conceptClassIdentifier, List<String> propertyValueList) {
		Optional<ConceptProperty> property = getConcept(conceptPropertyIdentifier);
		if (property.isPresent()) {
			Optional<ConceptClass> cClass  = getConcept(conceptClassIdentifier, ConceptClass.class);
			if ( cClass.isPresent() ) {
				// remove assignement
				Optional<ConceptClassProperty> ccp = classPropertyRepository.findByConceptClassAndProperty(cClass.get(), property.get());
				if ( ccp.isPresent() ) {
					ConceptClassProperty conceptClassProperty = ccp.get();
					for (String valueId : propertyValueList) {
						Optional<ConceptPropertyValue> value = getConcept(valueId, ConceptPropertyValue.class);
						if ( value.isPresent()) {
							conceptClassProperty.removePropertyValue(value.get());
						}
					}
					ConceptClassProperty changed = classPropertyRepository.save(conceptClassProperty);
					return changed.getPropertyValues();
				}
				
			}
			else {
				ConceptProperty prop = property.get();
				for (String valueId : propertyValueList) {
					Optional<ConceptPropertyValue> value = propertyValueService.getConcept(valueId);
					if (value.isPresent()) {
						prop.removePropertyValue(value.get());
					}
				}
				ConceptProperty changed = typeRepository.save(prop);
				return changed.getValues();
			}
		}
		throw new IllegalArgumentException(String.format("Concept Property with ID [%s] not found!", conceptPropertyIdentifier));

	}

}

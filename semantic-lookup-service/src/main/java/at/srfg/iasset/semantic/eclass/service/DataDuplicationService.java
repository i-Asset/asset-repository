package at.srfg.iasset.semantic.eclass.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.semantic.eclass.model.ClassificationClass;
import at.srfg.iasset.semantic.eclass.model.PropertyDefinition;
import at.srfg.iasset.semantic.eclass.repository.ClassificationClassPropertyRepository;
import at.srfg.iasset.semantic.eclass.repository.ClassificationClassPropertyValueRepository;
import at.srfg.iasset.semantic.eclass.repository.ClassificationClassRepository;
import at.srfg.iasset.semantic.eclass.repository.PropertyRepository;
import at.srfg.iasset.semantic.eclass.repository.UnitRepository;
import at.srfg.iasset.semantic.eclass.repository.ValueRepository;
import at.srfg.iasset.semantic.lookup.repository.ConceptClassPropertyRepository;
import at.srfg.iasset.semantic.lookup.repository.ConceptClassRepository;
import at.srfg.iasset.semantic.lookup.repository.ConceptRepository;
import at.srfg.iasset.semantic.lookup.service.indexing.SemanticIndexer;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptClassProperty;
import at.srfg.iasset.semantic.model.ConceptClassPropertyPK;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;
import at.srfg.iasset.semantic.model.DataTypeEnum;

@Service
public class DataDuplicationService {
	@Autowired
	private SemanticIndexer indexer;

	@Autowired
	private ClassificationClassRepository classificationClassRepository;
	@Autowired
	private ClassificationClassPropertyRepository classificationClassPropertyRepo;
	@Autowired
	private ClassificationClassPropertyValueRepository classificationClassPropertyValueRepo;
	@Autowired
	private PropertyRepository propertyRepo;
	@Autowired
	private ValueRepository valueRepo;
	@Autowired
	private UnitRepository unitRepo;
	// 
	@Autowired
	private ConceptRepository<ConceptProperty> conceptPropertyRepo;
	@Autowired
	private ConceptRepository<ConceptPropertyUnit> conceptPropertyUnitRepo;
	@Autowired
	private ConceptRepository<ConceptPropertyValue> conceptPropertyValueRepo;
	@Autowired 
	private ConceptClassRepository conceptRepository;
	@Autowired
	private ConceptClassPropertyRepository conceptClassPropertyRepository;
	
	@Autowired
	private ConceptClassPropertyRepository classPropertyRepo;
	
	public Optional<ConceptClass> copyClassificationClass(String irdi) {
		Optional<ClassificationClass> ccOpt = classificationClassRepository.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			ConceptClass parent = getParent(ccOpt.get());
			// now copy the classification class with the parent
			ConceptClass cc = fromClassificationClass(parent, ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.empty();
	}
	public Optional<ConceptProperty> copyProperty(String irdi) {
		Optional<PropertyDefinition> ccOpt = propertyRepo.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			
			// now copy the classification class with the parent
			ConceptProperty cc = fromPropertyDefinition(ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.ofNullable(null);
	}
	public Optional<ConceptPropertyUnit> copyUnit(String irdi) {
		Optional<at.srfg.iasset.semantic.eclass.model.PropertyUnit> ccOpt = unitRepo.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			
			// now copy the classification class with the parent
			ConceptPropertyUnit cc = fromPropertyUnit(ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.empty();
	}
	public Optional<ConceptPropertyValue> copyValue(String irdi) {
		Optional<at.srfg.iasset.semantic.eclass.model.PropertyValue> ccOpt = valueRepo.findById(irdi);
		if (ccOpt.isPresent()) {
			// obtain the parent for the classification class
			
			// now copy the classification class with the parent
			ConceptPropertyValue cc = fromPropertyValue(ccOpt.get());
			return Optional.of(cc);
		}
		return Optional.empty();
	}
	/**
	 * suche cc for classification
	 * 
	 * @param classificationClass
	 * @return
	 */
	private ConceptClass getParent(ClassificationClass classificationClass) {
		
		int level = classificationClass.getLevel()-1;
		String codedName = padEnd(classificationClass.getCodedName().substring(0, level*2), 8, '0');
		Optional<ClassificationClass> parent = classificationClassRepository.findParent(level, codedName);
		if ( parent.isPresent() ) {
			Optional<ConceptClass> ccParent = conceptRepository.findByConceptId(parent.get().getIrdiCC());
			if ( ccParent.isPresent() ) {
				return ccParent.get();
			}
			else {
				ConceptClass cc = getParent(parent.get());
				return fromClassificationClass(cc, parent.get());
			}
		}
		else {
			return null;

		}
	}
	protected String padEnd(String string, int length, char character) {
		StringBuffer buf = new StringBuffer(string);
		for ( int i = string.length(); i<length; i++ ) {
			buf.append(character);
		}
		return buf.toString();
	}



	private ConceptProperty duplicateProperty(PropertyDefinition eClass) {
		ConceptProperty prop = new ConceptProperty(eClass.getIrdiPR());
		prop.setVersionDate(eClass.getVersionDate());
		prop.setRevisionNumber(eClass.getRevisionNumber());
		
		prop.setCoded(eClass.getAttributeType()=="INDIRECT"?Boolean.TRUE: Boolean.FALSE);
		//
		prop.setCategory(eClass.getCategory());
		prop.setDataType(DataTypeEnum.fromString(eClass.getDataType()));
		prop.setDefinition(Locale.forLanguageTag(eClass.getIsoLanguageCode()), eClass.getDefinition());
		
		prop.setPreferredLabel(Locale.forLanguageTag(eClass.getIsoLanguageCode()), eClass.getPreferredName());
		prop.setNote(eClass.getNote());
		prop.setRemark(eClass.getRemark());
		prop.setShortName(eClass.getShortName());
		prop.setSourceOfDefinition(eClass.getSourceOfDefinition());
		// process the values
		for (at.srfg.iasset.semantic.eclass.model.PropertyValue v : eClass.getValues() ) {
			// process property assignment
			ConceptPropertyValue vt = fromPropertyValue(v);
			prop.addPropertyValue(vt);
			
		};
		if ( eClass.getUnit() != null ) {
			ConceptPropertyUnit unit = fromPropertyUnit(eClass.getUnit());
			prop.setUnit(unit);
		}
		// 
		duplicatePropertyValues(prop, eClass);
		//
		conceptPropertyRepo.save(prop);
		// store property in the index
		indexer.store(prop);

		return prop;
	}
	private void duplicatePropertyValues(ConceptProperty property, PropertyDefinition eClass) {
		List<Object[]> values = classificationClassPropertyValueRepo.findValuesForProperty(eClass.getIrdiPR(), "FALSE");
		for (Object[] value : values) {
			String irdiVA = value[0].toString();
			long min = new Long(value[1].toString()).longValue();
			long max = new Long(value[2].toString()).longValue();
			if ( min == max) {
				at.srfg.iasset.semantic.eclass.model.PropertyValue v = valueRepo.findByIrdiVA(irdiVA);
				ConceptPropertyValue pv = fromPropertyValue(v);
				property.addPropertyValue(pv);
			}
		}
	}
	private ConceptPropertyUnit duplicatePropertyUnit(at.srfg.iasset.semantic.eclass.model.PropertyUnit eClass) {
		ConceptPropertyUnit target = new ConceptPropertyUnit(eClass.getIrdiUN());
		//target.setVersionDate(eClass.getVersionDate());
		//target.setRevisionNumber(eClass.get);
		target.setPreferredLabel(Locale.ENGLISH, eClass.getStructuredNaming());
		target.setDefinition(Locale.ENGLISH, eClass.getDefinition());
		target.setComment(Locale.ENGLISH, eClass.getComment());
		target.setSiName(eClass.getSiName());
		target.setSiNotation(eClass.getSiNotation());
		target.setEceCode(eClass.getEceCode());
		target.setEceName(eClass.getEceName());
		target.setNistName(eClass.getNistName());
		target.setNameOfDedicatedQuantity(eClass.getNameOfDedicatedQuantity());
		target.setSource(eClass.getSource());
		target.setStructuredNaming(eClass.getStructuredNaming());
		target.setIecClassification(eClass.getIecClassification());
		conceptPropertyUnitRepo.save(target);
		return target;
	}
	private ConceptPropertyValue duplicatePropertyValue(at.srfg.iasset.semantic.eclass.model.PropertyValue source) {
		ConceptPropertyValue target = new ConceptPropertyValue(source.getIrdiVA());
		target.setVersionDate(source.getVersionDate());
		target.setRevisionNumber(source.getRevisionNumber());
		target.setDataType(DataTypeEnum.fromString(source.getDataType()));
		target.setReference(source.getReference());
		target.setShortName(source.getShortName());
		target.setValue(source.getPreferredName());
		target.setPreferredLabel(Locale.forLanguageTag(source.getIsoLanguage()), source.getPreferredName());
		target.setDefinition(Locale.forLanguageTag(source.getIsoLanguage()), source.getDefinition());
		conceptPropertyValueRepo.save(target);
		return target;
	}

	private ConceptClass fromClassificationClass(final ConceptClass parent, ClassificationClass eClass) {
		Optional<ConceptClass> opt = conceptRepository.findByConceptId(eClass.getIrdiCC());
		return opt.orElseGet(new Supplier<ConceptClass>() {
			public ConceptClass get() {
				return duplicateClass(parent, eClass);
			}
		});
	}
	private ConceptPropertyUnit fromPropertyUnit(at.srfg.iasset.semantic.eclass.model.PropertyUnit eClass) {
		Optional<ConceptPropertyUnit> opt = conceptPropertyUnitRepo.findByConceptId(eClass.getIrdiUN());
		return opt.orElseGet(new Supplier<ConceptPropertyUnit>() {
			public ConceptPropertyUnit get() {
				return duplicatePropertyUnit(eClass);
			}
		});
	}

	private ConceptPropertyValue fromPropertyValue(at.srfg.iasset.semantic.eclass.model.PropertyValue eClass) {
		Optional<ConceptPropertyValue> opt = conceptPropertyValueRepo.findByConceptId(eClass.getIrdiVA());
		return opt.orElseGet(new Supplier<ConceptPropertyValue>() {
			public ConceptPropertyValue get() {
				return duplicatePropertyValue(eClass);
			}
		});
	}

	private ConceptProperty fromPropertyDefinition(PropertyDefinition eClass) {
		Optional<ConceptProperty> opt = conceptPropertyRepo.findByConceptId(eClass.getIrdiPR());
		return opt.orElseGet(new Supplier<ConceptProperty>() {
			public ConceptProperty get() {
				return duplicateProperty(eClass);
			}
		});
	}
	private ConceptClassProperty fromClassificationClassProperty(final ConceptClass cClass, final ConceptProperty property, long usageCount) {
		Optional<ConceptClassProperty> opt = classPropertyRepo.findById(new ConceptClassPropertyPK(cClass, property));
		
		return opt.orElseGet(new Supplier<ConceptClassProperty>() {
			public ConceptClassProperty get() {
				ConceptClassProperty classProperty = new ConceptClassProperty(cClass, property);
				
				List<Object[]> values = classificationClassPropertyValueRepo.findValuesForPropertyAndCodedName(property.getConceptId(), cClass.getCodedName(), "TRUE", usageCount);
				for (Object[] pwc : values) {
					String irdiVA = pwc[0].toString();
					at.srfg.iasset.semantic.eclass.model.PropertyValue value = valueRepo.findByIrdiVA(irdiVA);
					ConceptPropertyValue conceptValue = fromPropertyValue(value);
					classProperty.addPropertyValue(conceptValue);
					classProperty.setValueConstraint(true);
				}
				classPropertyRepo.save(classProperty);
				
				return classProperty;
			}
		});
	}


	private ConceptClass duplicateClass(ConceptClass parent, ClassificationClass eClass) {
		ConceptClass cClass = new ConceptClass(parent, eClass.getIrdiCC());
		cClass.setVersionDate(eClass.getVersionDate());
		cClass.setRevisionNumber(eClass.getRevisionNumber());
		cClass.setPreferredLabel(Locale.forLanguageTag(eClass.getIsoLanguageCode()), eClass.getPreferredName());
		cClass.setDefinition(Locale.forLanguageTag(eClass.getIsoLanguageCode()), eClass.getDefinition());
		cClass.setNote(eClass.getNote());
		cClass.setRemark(eClass.getRemark());
		cClass.setShortName(eClass.getCodedName());
		cClass.setCodedName(eClass.getCodedName());
		cClass.setLevel(eClass.getLevel());
		conceptRepository.save(cClass);
		// search for property assignements
		duplicateClassProperties(cClass);
		//
		indexer.store(cClass);
		return cClass;
		
	}
	private boolean isParentProperty(ConceptClass parent, ConceptProperty conceptProperty) {
		// find all subclasses which have properties
		if ( parent == null ) {
			return false;
		}
		Optional<ConceptClassProperty> opt = conceptClassPropertyRepository.findByConceptClassAndProperty(parent, conceptProperty);
		if ( opt.isPresent() ) {
			return true;
		}
		else {
			return isParentProperty(parent.getParentElement(), conceptProperty);
		}
	}
	private void duplicateClassProperties(ConceptClass conceptClass) {
		int level = conceptClass.getLevel();
		String classCodedNamePrefix = conceptClass.getCodedName().substring(0, level*2) + "%";
		long subClassCount = classificationClassRepository.countBySubclassPresentAndCodedNameLike(false, classCodedNamePrefix);
		// check each property whether it is applied to all subclasses
		// find all property assignments
		List<Object[]> propertiesWithCount = classificationClassPropertyRepo.findPropertyWithUsageCount(classCodedNamePrefix, subClassCount);
		for (Object[] pwc : propertiesWithCount) {
			String irdiPR = pwc[0].toString();
			Long propertyCount = Long.valueOf(pwc[1].toString());
			if ( subClassCount == propertyCount ) {
				Optional<PropertyDefinition> eClassProperty = propertyRepo.findByIrdiPR(irdiPR);
				if ( eClassProperty.isPresent()) {
					ConceptProperty conceptProperty = fromPropertyDefinition(eClassProperty.get());
					if ( ! isParentProperty(conceptClass, conceptProperty)) {
						fromClassificationClassProperty(conceptClass, conceptProperty, subClassCount);
					}
				}
			}			
		}

	}
}

package at.srfg.iasset.semantic.lookup.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import at.srfg.iasset.semantic.lookup.service.PropertyUnitService;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;

@Service
public class PropertyUnitServiceImpl extends ConceptServiceImpl<ConceptPropertyUnit> implements PropertyUnitService {

	@Override
	public Optional<ConceptPropertyUnit> addConcept(ConceptPropertyUnit newConcept) {
		ConceptPropertyUnit toStore = new ConceptPropertyUnit(newConcept.getConceptId());
		toStore.setShortName(newConcept.getShortName());
		toStore.setPreferredLabel(newConcept.getPreferredLabel());
		toStore.setAlternateLabel(newConcept.getAlternateLabel());
		toStore.setHiddenLabel(newConcept.getHiddenLabel());
		toStore.setNote(newConcept.getNote());
		toStore.setRemark(newConcept.getRemark());
		toStore.setRevisionNumber(newConcept.getRevisionNumber());
		// 
		toStore.setDinNotation(newConcept.getDinNotation());
		toStore.setEceCode(newConcept.getEceCode());
		toStore.setEceName(newConcept.getEceName());
		toStore.setSiName(newConcept.getSiName());
		toStore.setSiNotation(newConcept.getSiNotation());
		toStore.setStructuredNaming(newConcept.getStructuredNaming());
		toStore.setNistName(newConcept.getNistName());
		// store the unit
		ConceptPropertyUnit stored = typeRepository.save(toStore);
		return Optional.of(stored);
	}

	@Override
	public ConceptPropertyUnit setConcept(ConceptPropertyUnit unit,
			ConceptPropertyUnit updated) {
		unit.setPreferredLabel(updated.getPreferredLabel());
		unit.setAlternateLabel(updated.getAlternateLabel());
		unit.setHiddenLabel(updated.getHiddenLabel());
		// note
		if (! isNullOrEmpty(updated.getNote())) {
			unit.setNote(updated.getNote());
		}
		// remark
		if (! isNullOrEmpty(updated.getRemark())) {
			unit.setRemark(updated.getRemark());
		}
		// shortName
		if (! isNullOrEmpty(updated.getShortName())) {
			unit.setShortName(updated.getShortName());
		}
		// dinNotation
		if (! isNullOrEmpty(updated.getDinNotation())) {
			unit.setDinNotation(updated.getDinNotation());
		}
		// eceCode
		if (! isNullOrEmpty(updated.getEceCode())) {
			unit.setEceCode(updated.getEceCode());
		}
		// eceName
		if (! isNullOrEmpty(updated.getEceName())) {
			unit.setEceName(updated.getEceName());
		}
		// siName
		if (! isNullOrEmpty(updated.getSiName())) {
			unit.setSiName(updated.getSiName());
		}
		// siNotation
		if (! isNullOrEmpty(updated.getSiNotation())) {
			unit.setSiNotation(updated.getSiNotation());
		}
		// structuredNaming
		if (! isNullOrEmpty(updated.getStructuredNaming())) {
			unit.setStructuredNaming(updated.getStructuredNaming());
		}
		// nistName
		if (! isNullOrEmpty(updated.getNistName())) {
			unit.setNistName(updated.getNistName());
		}
		// iecClassification
		if (! isNullOrEmpty(updated.getIecClassification())) {
			unit.setIecClassification(updated.getIecClassification());
		}
		
		//
		return typeRepository.save(unit);
	}

	@Override
	public Optional<ConceptPropertyUnit> setConcept(ConceptPropertyUnit updated) {
		Optional<ConceptPropertyUnit> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			return Optional.of(setConcept(stored.get(),updated));
		}
		return Optional.empty();
	}

}

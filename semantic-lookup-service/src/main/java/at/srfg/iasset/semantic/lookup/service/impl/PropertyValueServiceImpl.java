package at.srfg.iasset.semantic.lookup.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.semantic.eclass.service.DataDuplicationService;
import at.srfg.iasset.semantic.lookup.service.PropertyValueService;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

@Service
public class PropertyValueServiceImpl extends ConceptServiceImpl<ConceptPropertyValue> implements PropertyValueService {
	@Autowired
	private DataDuplicationService duplexer;

	public Optional<ConceptPropertyValue> getConcept(String identifier) {
		Optional<ConceptPropertyValue> ccOpt = typeRepository.findByConceptId(identifier);
		if (!ccOpt.isPresent()) {
			return duplexer.copyValue(identifier);
			
		}
		else {
			return ccOpt;
		}
	}
	@Override
	public Optional<ConceptPropertyValue> addConcept(ConceptPropertyValue newConcept) {
		ConceptPropertyValue toStore = new ConceptPropertyValue(newConcept.getConceptId());
		toStore.setShortName(newConcept.getShortName());
		toStore.setPreferredLabel(newConcept.getPreferredLabel());
		toStore.setAlternateLabel(newConcept.getAlternateLabel());
		toStore.setHiddenLabel(newConcept.getHiddenLabel());
		toStore.setDefinition(newConcept.getDefinition());
		toStore.setComment(newConcept.getComment());
		toStore.setNote(newConcept.getNote());
		toStore.setRemark(newConcept.getRemark());
		toStore.setRevisionNumber(newConcept.getRevisionNumber());
		// 
		toStore.setDataType(newConcept.getDataType());
		toStore.setReference(newConcept.getReference());
		toStore.setValue(newConcept.getValue());
		// store the unit
		ConceptPropertyValue stored = typeRepository.save(toStore);
		return Optional.of(stored);
	}

	@Override
	public ConceptPropertyValue setConcept(ConceptPropertyValue toStore,
			ConceptPropertyValue updated) {
		// labels
		toStore.setPreferredLabel(updated.getPreferredLabel());
		toStore.setAlternateLabel(updated.getAlternateLabel());
		toStore.setHiddenLabel(updated.getHiddenLabel());
		toStore.setDefinition(updated.getDefinition());
		toStore.setComment(updated.getComment());
		
		// note
		if (! isNullOrEmpty(updated.getNote())) {
			toStore.setNote(updated.getNote());
		}
		// remark
		if (! isNullOrEmpty(updated.getRemark())) {
			toStore.setRemark(updated.getRemark());
		}
		// shortName
		if (! isNullOrEmpty(updated.getShortName())) {
			toStore.setShortName(updated.getShortName());
		}
		// reference
		if (! isNullOrEmpty(updated.getReference())) {
			toStore.setReference(updated.getReference());
		}
		// value
		if (! isNullOrEmpty(updated.getValue())) {
			toStore.setValue(updated.getValue());
		}
		//
		return typeRepository.save(toStore);
	}

	@Override
	public Optional<ConceptPropertyValue> setConcept(ConceptPropertyValue updated) {
		Optional<ConceptPropertyValue> stored = getStoredConcept(updated);
		if ( stored.isPresent()) {
			return Optional.of(setConcept(stored.get(),updated));
		}
		return Optional.empty();
	}

}

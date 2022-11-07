package at.srfg.iasset.semantic.lookup.service.indexing;

import at.srfg.iasset.semantic.model.ConceptClass;

public class ConceptClassEvent extends IndexingEvent<ConceptClass> {

	public ConceptClassEvent(ConceptClass source) {
		super(source);
	}
	
	public ConceptClassEvent(ConceptClass source, boolean delete) {
		super(source, delete);
	}	
}

package at.srfg.iasset.semantic.lookup.service.indexing;

import at.srfg.iasset.semantic.model.ConceptProperty;

public class PropertyEvent extends IndexingEvent<ConceptProperty> {

	public PropertyEvent(ConceptProperty source) {
		super(source);
	}
	public PropertyEvent(ConceptProperty source, boolean delete) {
		super(source, delete);
	}

}

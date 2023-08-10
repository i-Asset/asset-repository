package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.ReferenceElement;

import at.srfg.iasset.repository.model.helper.value.ReferenceElementValue;

public class ReferenceElementValueMapper implements ValueMapper<ReferenceElement, ReferenceElementValue> {

	@Override
	public ReferenceElementValue mapToValue(ReferenceElement modelElement) {
		if ( modelElement.getValue() != null ) {
			return new ReferenceElementValue(modelElement.getValue().getKeys());
		}
		return null;
	}

}

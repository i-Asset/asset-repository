package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.ReferenceElement;

import at.srfg.iasset.repository.model.helper.value.ReferenceElementValue;

public class ReferenceElementValueMapper implements ValueMapper<ReferenceElement, ReferenceElementValue> {

	@Override
	public ReferenceElementValue getValueOnly(ReferenceElement modelElement) {
		return new ReferenceElementValue(modelElement.getValue().getKeys());
	}

}

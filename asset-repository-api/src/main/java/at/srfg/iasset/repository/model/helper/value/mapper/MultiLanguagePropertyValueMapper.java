package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.MultiLanguageProperty;

import at.srfg.iasset.repository.model.helper.value.MultiLanguagePropertyValue;

public class MultiLanguagePropertyValueMapper implements ValueMapper<MultiLanguageProperty, MultiLanguagePropertyValue> {

	@Override
	public MultiLanguagePropertyValue getValueOnly(MultiLanguageProperty modelElement) {
		return new MultiLanguagePropertyValue(modelElement.getValues());
	}

}

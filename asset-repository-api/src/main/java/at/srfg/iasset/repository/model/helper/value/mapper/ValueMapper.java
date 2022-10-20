package at.srfg.iasset.repository.model.helper.value.mapper;

import org.eclipse.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;

public interface ValueMapper<M extends SubmodelElement, V extends SubmodelElementValue> {
	/**
	 * Obtain the value-only representation of the {@link SubmodelElement}
	 * @param modelElement The SubmodelElement
	 * @return
	 */
	V getValueOnly(M modelElement);
	default M applyValue(M modelElement, JsonNode valueNode) {
		return modelElement;
	}
	default M applyValues(M modelElement, V valueObject) {
		return modelElement;
	}
}

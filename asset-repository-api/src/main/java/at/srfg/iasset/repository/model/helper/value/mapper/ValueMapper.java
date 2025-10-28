package at.srfg.iasset.repository.model.helper.value.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public interface ValueMapper<M extends SubmodelElement, V extends SubmodelElementValue> {
	/**
	 * Obtain the value-only representation of the {@link SubmodelElement}
	 * @param modelElement The SubmodelElement
	 * @return
	 */
	V mapToValue(M modelElement) throws ValueMappingException;
	/**
	 * Update the pre-existing model element with the provided valueNode
	 * <p>
	 * Values are only mapped to existing sub-elements. E.g.
	 * {@link SubmodelElementCollection}s will search for values with {@link SubmodelElement#getIdShort()}. 
	 * {@link SubmodelElementList} will process the existing list, and update it's value as long there are
	 * value in the provided value list, no extra list elements will be created!  
	 * </p> 
	 * @param modelElement The model element to modify with the values
	 * @param valueNode
	 * @return
	 * @throws ValueMappingException 
	 */
	default M mapValueToElement(M modelElement, JsonNode valueNode) throws ValueMappingException {
		return modelElement;
	}
	default M mapValueToTemplate(ServiceEnvironment serviceEnvironment, M modelElement, JsonNode valueNode) throws ValueMappingException {
		return mapValueToElement(modelElement, valueNode);
	}
	default M mapValueToTemplate(ServiceEnvironment serviceEnvironment, M modelElement, M templateElement, JsonNode valueNode) throws ValueMappingException {
		return mapValueToTemplate(serviceEnvironment, modelElement, valueNode);
	}
	default M mapValueToTemplate(ServiceEnvironment serviceEnvironment, M modelElement, Reference modelReference, JsonNode valueNode) throws ValueMappingException {
		return mapValueToTemplate(serviceEnvironment, modelElement, valueNode);
	}
	default M applyValues(M modelElement, V valueObject) throws ValueMappingException {
		return modelElement;
	}
}

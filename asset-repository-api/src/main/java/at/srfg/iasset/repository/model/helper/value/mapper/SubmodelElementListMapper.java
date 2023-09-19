package at.srfg.iasset.repository.model.helper.value.mapper;

import java.util.Optional;

import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.SubmodelElementList;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementListValue;

public class SubmodelElementListMapper implements ValueMapper<SubmodelElementList, SubmodelElementListValue>{

	@Override
	public SubmodelElementListValue mapToValue(SubmodelElementList modelElement) {
		SubmodelElementListValue value = new SubmodelElementListValue();

		for ( SubmodelElement element : modelElement.getValues()) {
			value.getValues().add(ValueHelper.toValue(element));
		}
		return value;
	}

	@Override
	public SubmodelElementList mapValueToElement(SubmodelElementList modelElement, JsonNode valueNode) {
		modelElement.getValues().clear();
		if ( modelElement.getValueTypeListElement() != null ) {
			if ( valueNode.isArray() ) {
				for ( int i = 0; i < valueNode.size(); i++) {
					JsonNode value = valueNode.get(i);
					if ( value!= null) {
						if ( value.isValueNode()) {
							Property listElement = AASModelHelper.newElementInstance(Property.class);
							modelElement.getValues().add(listElement);
							listElement.setValueType(modelElement.getValueTypeListElement());
							listElement.setIdShort(String.format("%s(%s)", modelElement.getIdShort(), i+1));
							ValueHelper.applyValue(listElement, value);
							
						}
					}
				}
			}
		}

		return modelElement;
	}

	@Override
	public SubmodelElementList mapValueToTemplate(ServiceEnvironment environment, SubmodelElementList modelElement, JsonNode valueNode) {
		// is the list element a template?
		modelElement.getValues().clear();
		Optional<SubmodelElement> listElementTemplate = environment.resolve(modelElement.getSemanticIdListElement(), SubmodelElement.class);

		if ( valueNode.isArray() ) {
			for ( int i = 0; i < valueNode.size(); i++) {
				
				JsonNode value = valueNode.get(i);
				if (value != null ) {
					if ( value.isValueNode() ) {
						Property listElement = AASModelHelper.newElementInstance(Property.class);
						modelElement.getValues().add(listElement);
						listElement.setValueType(modelElement.getValueTypeListElement());
						listElement.setIdShort(String.format("%s(%s)", modelElement.getIdShort(), i+1));
						ValueHelper.applyValue(environment, listElement, value);
						
					}
					else if ( value.isObject()) {
						SubmodelElementCollection collection = AASModelHelper.newElementInstance(SubmodelElementCollection.class);
						modelElement.getValues().add(collection);
						collection.setIdShort(String.format("%s(%s)", modelElement.getIdShort(), i+1));
						ValueHelper.applyValue(environment, collection, listElementTemplate.get(), value);
					}
					else if ( value.isArray()) {
						SubmodelElementList collection = AASModelHelper.newElementInstance(SubmodelElementList.class);
						modelElement.getValues().add(collection);
						collection.setIdShort(String.format("%s(%s)", modelElement.getIdShort(), i+1));
						ValueHelper.applyValue(environment, collection, value);
					}
				}
			}
		}
		return modelElement;
	}

}

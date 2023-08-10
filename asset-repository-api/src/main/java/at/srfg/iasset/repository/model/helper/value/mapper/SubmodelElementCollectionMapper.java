package at.srfg.iasset.repository.model.helper.value.mapper;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.ModelingKind;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.SubmodelElementList;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementCollectionValue;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;

public class SubmodelElementCollectionMapper implements ValueMapper<SubmodelElementCollection, SubmodelElementCollectionValue>{

	@Override
	public SubmodelElementCollectionValue mapToValue(SubmodelElementCollection modelElement) {
		SubmodelElementCollectionValue value = new SubmodelElementCollectionValue();
		for ( SubmodelElement element : modelElement.getValues()) {
			value.getValues().put(element.getIdShort(), ValueHelper.toValue(element));
		}
		return value;
	}

	@Override
	public SubmodelElementCollection mapValueToElement(SubmodelElementCollection modelElement, JsonNode valueNode) {
		modelElement.getValues().stream().forEach(new Consumer<SubmodelElement>() {

			@Override
			public void accept(SubmodelElement t) {
				JsonNode elementValue = valueNode.get(t.getIdShort());
				if ( elementValue != null) {
					ValueHelper.applyValue(t, elementValue);
				}
				
			}
		});
		return modelElement;
	}

	@Override
	public SubmodelElementCollection mapValueToTemplate(ServiceEnvironment serviceEnvironment,
			SubmodelElementCollection modelElement, JsonNode valueNode) {
		// TODO Auto-generated method stub
		if ( valueNode.isObject()) {
			Iterator<Entry<String,JsonNode>> fieldIterator = valueNode.fields();
			while( fieldIterator.hasNext()) {
				Entry<String, JsonNode> fieldNode = fieldIterator.next();
				SubmodelElement element = modelElement.getValues().stream().filter(new Predicate<SubmodelElement>() {

					@Override
					public boolean test(SubmodelElement t) {
						return fieldNode.getKey().equals(t.getIdShort());
					}})
					.findFirst()
					.orElseGet(new Supplier<SubmodelElement>() {

						@Override
						public SubmodelElement get() {
							SubmodelElement newElement = cloneElement(fieldNode.getKey(), fieldNode.getValue());
							modelElement.getValues().add(newElement);
							return newElement;
						}});
				
				
				ValueHelper.applyValue(serviceEnvironment, element, fieldNode.getValue());
			}
		}
		return modelElement;
//		return ValueMapper.super.mapValueToTemplate(serviceEnvironment, modelElement, valueNode);
	}
	private SubmodelElement cloneElement(String idShort, JsonNode valueNode) {
		if ( valueNode.isArray() && valueNode.has(0) && valueNode.get(0).isValueNode()) {
			SubmodelElement listElement = AASModelHelper.newElementInstance(SubmodelElementList.class);
			listElement.setIdShort(idShort);
			listElement.setKind(ModelingKind.INSTANCE);
			return listElement;
		}
		else if ( valueNode.isObject()) {
			SubmodelElement listElement = AASModelHelper.newElementInstance(SubmodelElementCollection.class);
			listElement.setIdShort(idShort);
			listElement.setKind(ModelingKind.INSTANCE);
			return listElement;
		}
		else {
			Property listElement = AASModelHelper.newElementInstance(Property.class);
			listElement.setIdShort(idShort);
			listElement.setKind(ModelingKind.INSTANCE);
			return listElement;
		}
	}


}

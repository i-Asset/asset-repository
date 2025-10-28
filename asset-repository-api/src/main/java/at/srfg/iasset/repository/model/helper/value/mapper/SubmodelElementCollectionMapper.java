package at.srfg.iasset.repository.model.helper.value.mapper;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;

import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;

import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementCollectionValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class SubmodelElementCollectionMapper implements ValueMapper<SubmodelElementCollection, SubmodelElementCollectionValue>{

	@Override
	public SubmodelElementCollectionValue mapToValue(SubmodelElementCollection modelElement) throws ValueMappingException {
		SubmodelElementCollectionValue value = new SubmodelElementCollectionValue();
		for ( SubmodelElement element : modelElement.getValue()) {
			value.getValues().put(element.getIdShort(), ValueHelper.toValue(element));
		}
		return value;
	}

	@Override
	public SubmodelElementCollection mapValueToElement(SubmodelElementCollection modelElement, JsonNode valueNode) throws ValueMappingException {
		for (SubmodelElement element : modelElement.getValue()) {
			JsonNode elementValue = valueNode.get(element.getIdShort());
			if ( elementValue != null) {
				ValueHelper.applyValue(element, elementValue);
			}
			
		}
//		}
//		modelElement.getValues().stream().forEach(new Consumer<SubmodelElement>() {
//
//			@Override
//			public void accept(SubmodelElement t) {
//				JsonNode elementValue = valueNode.get(t.getIdShort());
//				if ( elementValue != null) {
//					ValueHelper.applyValue(t, elementValue);
//				}
//				
//			}
//		});
		return modelElement;
	}

	@Override
	public SubmodelElementCollection mapValueToTemplate(ServiceEnvironment serviceEnvironment,
			SubmodelElementCollection modelElement, JsonNode valueNode) throws ValueMappingException {
		if ( modelElement.getSemanticId()!= null ) {
			Optional<SubmodelElementCollection> template = serviceEnvironment.getSubmodelElement(modelElement.getSemanticId(), SubmodelElementCollection.class);
			if ( template.isPresent()) {
				return mapValueToTemplate(serviceEnvironment, modelElement, template.get(), valueNode);
			}
			
		}

		if ( valueNode.isObject()) {
			Iterator<Entry<String,JsonNode>> fieldIterator = valueNode.fields();
			while( fieldIterator.hasNext()) {
				Entry<String, JsonNode> fieldNode = fieldIterator.next();
				SubmodelElement element = modelElement.getValue().stream().filter(new Predicate<SubmodelElement>() {

					@Override
					public boolean test(SubmodelElement t) {
						return fieldNode.getKey().equals(t.getIdShort());
					}})
					.findFirst()
					.orElseGet(new Supplier<SubmodelElement>() {
						// 
						@Override
						public SubmodelElement get() {
							SubmodelElement newElement = cloneElement(fieldNode.getKey(), fieldNode.getValue());
							modelElement.getValue().add(newElement);
							return newElement;
						}});
				
				
				ValueHelper.applyValue(serviceEnvironment, element, fieldNode.getValue());
			}
		}
		return modelElement;
//		return ValueMapper.super.mapValueToTemplate(serviceEnvironment, modelElement, valueNode);
	}
	
	@Override
	public SubmodelElementCollection mapValueToTemplate(ServiceEnvironment serviceEnvironment,
			SubmodelElementCollection modelElement, SubmodelElementCollection templateElement, JsonNode valueNode) throws ValueMappingException {
		modelElement.getValue().clear();
		if ( valueNode.isObject()) {
			Iterator<Entry<String,JsonNode>> fieldIterator = valueNode.fields();
			while( fieldIterator.hasNext()) {
				Entry<String, JsonNode> fieldNode = fieldIterator.next();
				Optional<SubmodelElement> element = templateElement.getValue().stream().filter(new Predicate<SubmodelElement>() {

					@Override
					public boolean test(SubmodelElement t) {
						return fieldNode.getKey().equals(t.getIdShort());
					}})
					.findFirst();
				
				if ( element.isPresent()) {
					SubmodelElement instance = instantiate(element.get(), fieldNode.getValue());
					modelElement.getValue().add(instance);
					ValueHelper.applyValue(serviceEnvironment, instance, element.get(), fieldNode.getValue());
				}
			}
		}
		return modelElement;
	}
	@Override
	public SubmodelElementCollection mapValueToTemplate(ServiceEnvironment serviceEnvironment,
			SubmodelElementCollection modelElement, Reference modelReference, JsonNode valueNode) throws ValueMappingException {
		Optional<SubmodelElementCollection> template = serviceEnvironment.resolve(modelReference, SubmodelElementCollection.class);
		if ( template.isPresent()) {
			SubmodelElementCollection templateElement = template.get(); 
			
			modelElement.getValue().clear();
			if ( valueNode.isObject()) {
				Iterator<Entry<String,JsonNode>> fieldIterator = valueNode.fields();
				while( fieldIterator.hasNext()) {
					Entry<String, JsonNode> fieldNode = fieldIterator.next();
					Optional<SubmodelElement> element = templateElement.getValue().stream().filter(new Predicate<SubmodelElement>() {
						
						@Override
						public boolean test(SubmodelElement t) {
							return fieldNode.getKey().equals(t.getIdShort());
						}})
							.findFirst();
					
					if ( element.isPresent()) {
						Reference refToTemplate = ReferenceUtils.toReference(modelReference, element.get());
						SubmodelElement instance = instantiate(element.get(), fieldNode.getValue());
						if ( instance.getSemanticId() == null ) {
							instance.setSemanticId(refToTemplate);
						}
						modelElement.getValue().add(instance);
						ValueHelper.applyValue(serviceEnvironment, instance, ReferenceUtils.toReference(modelReference, element.get()), fieldNode.getValue());
					}
				}
			}
		}
		else {
			mapValueToElement(modelElement, valueNode);
		}
		return modelElement;
	}
	private SubmodelElement instantiate(SubmodelElement template, JsonNode valueNode) {
		if ( valueNode.isArray() && valueNode.has(0) && valueNode.get(0).isValueNode() && template instanceof SubmodelElementList) {
			SubmodelElement listElement = AASModelHelper.newElementInstance(SubmodelElementList.class);
			listElement.setIdShort(template.getIdShort());
			Reference semanticId = template.getSemanticId();
			if ( semanticId != null && semanticId.getType()==ReferenceTypes.EXTERNAL_REFERENCE) {
				listElement.setSemanticId(semanticId);
			}
//			listElement.setKind(ModelingKind.INSTANCE);
			return listElement;
		}
		else if ( valueNode.isObject() && template instanceof SubmodelElementCollection) {
			SubmodelElement listElement = AASModelHelper.newElementInstance(SubmodelElementCollection.class);
			listElement.setIdShort(template.getIdShort());
			Reference semanticId = template.getSemanticId();
			if ( semanticId != null && semanticId.getType()==ReferenceTypes.EXTERNAL_REFERENCE) {
				listElement.setSemanticId(semanticId);
			}
//			listElement.setKind(ModelingKind.INSTANCE);
			return listElement;
		}
		else if (template instanceof Property){
			Property listElement = AASModelHelper.newElementInstance(Property.class);
			listElement.setIdShort(template.getIdShort());
			Reference semanticId = template.getSemanticId();
			if ( semanticId != null && semanticId.getType()==ReferenceTypes.EXTERNAL_REFERENCE) {
				listElement.setSemanticId(semanticId);
			}
			listElement.setValueType(((Property)template).getValueType());
			return listElement;
		}
		return null;
	}
	private SubmodelElement cloneElement(String idShort, JsonNode valueNode) {
		if ( valueNode.isArray() && valueNode.has(0) && valueNode.get(0).isValueNode()) {
			SubmodelElement listElement = AASModelHelper.newElementInstance(SubmodelElementList.class);
			listElement.setIdShort(idShort);
//			listElement.setKind(ModelingKind.INSTANCE);
			return listElement;
		}
		else if ( valueNode.isObject()) {
			SubmodelElement listElement = AASModelHelper.newElementInstance(SubmodelElementCollection.class);
			listElement.setIdShort(idShort);
//			listElement.setKind(ModellingKind.INSTANCE);
			return listElement;
		}
		else {
			Property listElement = AASModelHelper.newElementInstance(Property.class);
			listElement.setIdShort(idShort);
//			listElement.setKind(ModellingKind.INSTANCE);
			return listElement;
		}
	}


}

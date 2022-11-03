package at.srfg.iasset.repository.model.helper.value.mapper;

import java.util.function.Consumer;

import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.SubmodelElementCollectionValue;

public class SubmodelElementCollectionMapper implements ValueMapper<SubmodelElementCollection, SubmodelElementCollectionValue>{

	@Override
	public SubmodelElementCollectionValue mapToValue(SubmodelElementCollection modelElement) {
		SubmodelElementCollectionValue value = new SubmodelElementCollectionValue();
		// TODO Auto-generated method stub
		for ( SubmodelElement element : modelElement.getValues()) {
			value.getValues().put(element.getIdShort(), ValueHelper.toValue(element));
		}
		return value;
	}

	@Override
	public SubmodelElementCollection mapValueToElement(SubmodelElementCollection modelElement, JsonNode valueNode) {
		// TODO Auto-generated method stub
		modelElement.getValues().stream().forEach(new Consumer<SubmodelElement>() {

			@Override
			public void accept(SubmodelElement t) {
				JsonNode elementValue = valueNode.get(t.getIdShort());
				ValueHelper.applyValue(t, elementValue);
				
			}
		});
		return modelElement;
	}

}

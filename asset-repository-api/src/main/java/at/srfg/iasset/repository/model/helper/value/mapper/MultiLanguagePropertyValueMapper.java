package at.srfg.iasset.repository.model.helper.value.mapper;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.aas4j.v3.model.LangString;
import org.eclipse.aas4j.v3.model.MultiLanguageProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.srfg.iasset.repository.model.helper.value.MultiLanguagePropertyValue;

public class MultiLanguagePropertyValueMapper implements ValueMapper<MultiLanguageProperty, MultiLanguagePropertyValue> {

	@Override
	public MultiLanguagePropertyValue mapToValue(MultiLanguageProperty modelElement) {
		return new MultiLanguagePropertyValue(modelElement.getValues());
	}

	@Override
	public MultiLanguageProperty mapValueToElement(MultiLanguageProperty modelElement, JsonNode valueNode) {
		if (valueNode.isObject()) {
			ObjectNode objectNode = (ObjectNode)valueNode;
			
			objectNode.fields().forEachRemaining(new Consumer<Entry<String,JsonNode>>() {

				@Override
				public void accept(Entry<String, JsonNode> t) {
					Optional<LangString> langString  = modelElement.getValues().stream()
							.filter(new Predicate<LangString>() {

								@Override
								public boolean test(LangString lStr) {
									return lStr.getLanguage().equalsIgnoreCase(t.getKey());
								}
							})
							.findAny();
					if ( langString.isPresent()) {
						langString.get().setText(t.getValue().asText());
					}
					
				}});
		}
		return ValueMapper.super.mapValueToElement(modelElement, valueNode);
	}

}

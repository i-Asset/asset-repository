package at.srfg.iasset.repository.model.helper.value.mapper;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.LangString;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangString;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.srfg.iasset.repository.model.helper.value.MultiLanguagePropertyValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class MultiLanguagePropertyValueMapper implements ValueMapper<MultiLanguageProperty, MultiLanguagePropertyValue> {

	@Override
	public MultiLanguagePropertyValue mapToValue(MultiLanguageProperty modelElement) {
		return new MultiLanguagePropertyValue(modelElement.getValues());
	}

	@Override
	public MultiLanguageProperty mapValueToElement(MultiLanguageProperty modelElement, JsonNode valueNode) throws ValueMappingException {
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
					else {
						modelElement.getValues().add(new DefaultLangString(t.getKey(), t.getValue().asText()));
					}
					
				}});
		}
		else if (valueNode.isTextual()) {
			if ( valueNode.asText().contains("@")) {
				
			}
		}
		else {
			
			throw new ValueMappingException("ValueMapping not possible - No map provided!");
		}
		return modelElement;
	}

}

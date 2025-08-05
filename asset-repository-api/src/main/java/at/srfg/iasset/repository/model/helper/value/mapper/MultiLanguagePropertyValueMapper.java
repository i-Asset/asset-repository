package at.srfg.iasset.repository.model.helper.value.mapper;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.LangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.srfg.iasset.repository.model.helper.value.MultiLanguagePropertyValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class MultiLanguagePropertyValueMapper implements ValueMapper<MultiLanguageProperty, MultiLanguagePropertyValue> {

	@Override
	public MultiLanguagePropertyValue mapToValue(MultiLanguageProperty modelElement) {
		return new MultiLanguagePropertyValue(modelElement.getValue());
	}

	@Override
	public MultiLanguageProperty mapValueToElement(MultiLanguageProperty modelElement, JsonNode valueNode) throws ValueMappingException {
		if (valueNode.isObject()) {
			ObjectNode objectNode = (ObjectNode)valueNode;
			
			objectNode.fields().forEachRemaining(new Consumer<Entry<String,JsonNode>>() {

				@Override
				public void accept(Entry<String, JsonNode> t) {
					Optional<LangStringTextType> langString  = modelElement.getValue().stream()
							.filter(new Predicate<LangStringTextType>() {

								@Override
								public boolean test(LangStringTextType lStr) {
									return lStr.getLanguage().equalsIgnoreCase(t.getKey());
								}
							})
							.findAny();
					if ( langString.isPresent()) {
						langString.get().setText(t.getValue().asText());
					}
					else {
						modelElement.getValue().add(new DefaultLangStringTextType.Builder()
								.language(t.getKey())
								.text(t.getValue().asText())
								.build());
										
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

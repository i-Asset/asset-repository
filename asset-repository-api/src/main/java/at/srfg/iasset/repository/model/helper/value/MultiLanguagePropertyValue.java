package at.srfg.iasset.repository.model.helper.value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;

import com.fasterxml.jackson.annotation.JsonValue;

public class MultiLanguagePropertyValue extends DataElementValue {
	@JsonValue
	private Map<String, String> value;

	public MultiLanguagePropertyValue(List<LangStringTextType> langString) {
		value = new HashMap<>();
		langString.forEach(new Consumer<LangStringTextType>() {

			@Override
			public void accept(LangStringTextType t) {
				value.put(t.getLanguage(), t.getText());
				
			}
		});
		
	}
}

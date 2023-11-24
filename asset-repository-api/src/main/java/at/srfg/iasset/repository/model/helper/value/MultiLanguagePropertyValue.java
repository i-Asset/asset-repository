package at.srfg.iasset.repository.model.helper.value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.digitaltwin.aas4j.v3.model.LangString;

import com.fasterxml.jackson.annotation.JsonValue;

public class MultiLanguagePropertyValue extends DataElementValue {
	@JsonValue
	private Map<String, String> value;

	public MultiLanguagePropertyValue(List<LangString> langString) {
		value = new HashMap<>();
		langString.forEach(new Consumer<LangString>() {

			@Override
			public void accept(LangString t) {
				value.put(t.getLanguage(), t.getText());
				
			}
		});
		
	}
}

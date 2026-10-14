package at.srfg.iasset.repository.model.helper.rdf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import com.fasterxml.jackson.annotation.JsonValue;

public class MultiLanguagePropertyValue extends DataElementValue {
	@JsonValue
	private Map<String, String> value;

	public MultiLanguagePropertyValue(IRI predicate, List<LangStringTextType> langString) {
		super(predicate);
		
		value = new HashMap<>();
		langString.forEach(new Consumer<LangStringTextType>() {

			@Override
			public void accept(LangStringTextType t) {
				value.put(t.getLanguage(), t.getText());
			}
		});
		
	}
	
    @Override
    protected void addToRDF(Resource parent, Model model) {
		value.keySet().forEach((key) -> {
			Literal langString = SimpleValueFactory.getInstance().createLiteral(value.get(key), key);
			model.add(parent, predicate(), langString);
		});
    }
}

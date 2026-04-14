package at.srfg.iasset.repository.model.helper.rdf;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import com.fasterxml.jackson.annotation.JsonValue;

import at.srfg.iasset.repository.model.helper.value.type.Value;

public class PropertyValue extends DataElementValue {
	@JsonValue
	Value<?> typedValue; 
	
	public PropertyValue(IRI predicate, Value<?> typedValue) {
		super(predicate);
		this.typedValue = typedValue;
	}
	
	public Value<?> getValue() {
		return typedValue;
	}
	
	public void setValue(Value<?> value) {
		this.typedValue = value;
	}
	
    @Override
    protected void addToRDF(Resource parent, Model model) {
		model.setNamespace("xsd", XSD.NAMESPACE);
		Literal literal = SimpleValueFactory.getInstance().createLiteral(typedValue.getValue().toString(), typedValue.getValueType().toIRI());
		model.add(parent, predicate(), literal);
    }
	
}

package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.rdf.PropertyValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.helper.value.type.Value;
import at.srfg.iasset.repository.model.helper.value.type.ValueType;

public class PropertyValueMapper implements RDFMapper<Property, PropertyValue>{

	@Override
	public PropertyValue mapToValue(Property modelElement) throws ValueMappingException {
		return new PropertyValue(Value.getValue(modelElement.getValueType(), modelElement.getValue()));
	}

	

	@Override
	public Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, Property modelElement) throws ValueMappingException {
		Model model = new TreeModel();
		if (parent == null ) {
			parent = SimpleValueFactory.getInstance().createBNode();
			model.setNamespace("xs", XSD.NAMESPACE);
		}
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement.getSemanticId());
		if ( property.isPresent()) {
			PropertyValue value = new PropertyValue(Value.getValue(modelElement.getValueType(), modelElement.getValue()));
			
			Literal literal = SimpleValueFactory.getInstance().createLiteral(value.getValue().toString(), value.getValue().getValueType().toIRI());
			model.add(parent, property.get(), literal);
		}
		
		return model;
	}

	@Override
	public Property mapToElement(RDFEnvironment rdfMetaModel, Resource parent, Model model, Property modelElement)
			throws ValueMappingException {
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement.getSemanticId());
		if ( property.isPresent()) {
			Models.objectLiteral(model.filter(parent, property.get(), null)).ifPresent(new Consumer<Literal>() {

				@Override
				public void accept(Literal t) {
					// todo check type
					ValueType vt = ValueType.fromDataType(modelElement.getValueType());
					modelElement.setValue(t.getLabel());
					
				}});
			
		}
		return modelElement;
	}



}

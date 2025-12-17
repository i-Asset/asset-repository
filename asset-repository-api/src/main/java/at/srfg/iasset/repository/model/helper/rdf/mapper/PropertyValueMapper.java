package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.rdf.PropertyValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.model.helper.value.type.Value;

public class PropertyValueMapper implements RDFMapper<Property, PropertyValue>{

	@Override
	public Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, Property modelElement) throws ValueMappingException {
		Model model = new TreeModel();
		if (parent == null ) {
			throw new ValueMappingException("No parent resource provided!");
		}
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement.getSemanticId());
		if ( property.isPresent()) {
			PropertyValue value = new PropertyValue(Value.getValue(modelElement.getValueType(), modelElement.getValue()));
			
			Literal literal = SimpleValueFactory.getInstance().createLiteral(value.getValue().toString(), value.getValue().getValueType().toIRI());
			model.add(parent, property.get(), literal);
		}
		
		return model;
	}



}

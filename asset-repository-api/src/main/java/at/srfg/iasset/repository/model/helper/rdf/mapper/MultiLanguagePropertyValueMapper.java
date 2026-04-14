package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.MultiLanguageProperty;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import at.srfg.iasset.repository.component.RDFEnvironment;
import at.srfg.iasset.repository.model.helper.rdf.MultiLanguagePropertyValue;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;

public class MultiLanguagePropertyValueMapper implements RDFMapper<MultiLanguageProperty, MultiLanguagePropertyValue> {

	@Override
	public Optional<MultiLanguagePropertyValue> mapToValue(MultiLanguageProperty modelElement, RDFEnvironment rdfMetaModel)
			throws ValueMappingException {
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement);
		if ( property.isPresent()) {
			return Optional.of(new MultiLanguagePropertyValue(property.get(), modelElement.getValue()));
		}
		return Optional.empty();
	}
	@Override
	public Optional<MultiLanguagePropertyValue> mapToValueAndModel(MultiLanguageProperty modelElement, RDFEnvironment rdfMetaModel, Model model, Resource parent)
			throws ValueMappingException {
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement);
		if ( property.isPresent()) {
			// check parent - when not available, create a BNode
			final Resource subject = (parent != null ? parent : SimpleValueFactory.getInstance().createBNode());

			// check for namespaces
			addToNamespaces(model, property.get().getNamespace());
			// add the literals to the model
			modelElement.getValue().forEach((LangStringTextType t) -> {
							Literal langString = SimpleValueFactory.getInstance().createLiteral(t.getText(), t.getLanguage());
                            model.add(subject, property.get(), langString);
                        });
			// create and return the value object
			return Optional.of(new MultiLanguagePropertyValue(property.get(), modelElement.getValue()));
		}
		return Optional.empty();
	}	
	@Override
	public Model mapToRDF(RDFEnvironment rdfMetaModel, Resource parent, MultiLanguageProperty modelElement)
			throws ValueMappingException {
		Model model = new TreeModel();
		if (parent == null ) {
			parent = SimpleValueFactory.getInstance().createBNode();
			model.setNamespace("xs", XSD.NAMESPACE);
		}
		final Resource resource = parent;
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement);
		if ( property.isPresent()) {
			modelElement.getValue().forEach(new Consumer<LangStringTextType>() {

				@Override
				public void accept(LangStringTextType t) {
					Literal langText = SimpleValueFactory.getInstance().createLiteral(t.getText(), t.getLanguage());
					model.add(resource, property.get(), langText);
					
				}});

		}
		
		return model;
	}
	@Override
	public MultiLanguageProperty mapToElement(RDFEnvironment rdfMetaModel, Resource parent, Model model, final MultiLanguageProperty modelElement) {
		Optional<IRI> property = rdfMetaModel.getSemanticIdentifier(modelElement.getSemanticId());
		if ( property.isPresent()) {
			for (Literal languageText : Models.objectLiterals(model.filter(parent, property.get(), null))) {
				languageText.getLanguage().ifPresent(new Consumer<String>() {

					@Override
					public void accept(final String language) {
						modelElement.getValue().removeIf(new Predicate<LangStringTextType>() {
							@Override
							public boolean test(LangStringTextType t) {
								
								return language.equalsIgnoreCase(t.getLanguage());
							}
						});
						modelElement.getValue().add(new DefaultLangStringTextType.Builder()
								.language(language)
								.text(languageText.getLabel())
								.build());
					}
				});
			}
		}
		return modelElement;
	}

}

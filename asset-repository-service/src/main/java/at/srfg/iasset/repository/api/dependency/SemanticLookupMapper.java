package at.srfg.iasset.repository.api.dependency;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.aas4j.v3.model.DataSpecificationContent;
import org.eclipse.aas4j.v3.model.DataSpecificationIEC61360;
import org.eclipse.aas4j.v3.model.DataSpecificationPhysicalUnit;
import org.eclipse.aas4j.v3.model.DataTypeIEC61360;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.LangString;
import org.eclipse.aas4j.v3.model.ValueList;
import org.eclipse.aas4j.v3.model.ValueReferencePair;
import org.eclipse.aas4j.v3.model.impl.DefaultDataSpecificationIEC61360;
import org.eclipse.aas4j.v3.model.impl.DefaultDataSpecificationPhysicalUnit;
import org.eclipse.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.aas4j.v3.model.impl.DefaultValueList;
import org.eclipse.aas4j.v3.model.impl.DefaultValueReferencePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.repository.utils.ReferenceUtils;
import at.srfg.iasset.semantic.model.ConceptBase;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

@Service
public class SemanticLookupMapper {
	@Autowired
	SemanticLookup lookup;
	/**
	 * Obtain a concept (Class, Property, PropertyValue or Physical Unit) from the Semantic Lookup!
	 * @param identifier
	 * @return
	 */
	public <T extends DataSpecificationContent> Optional<T> getDataSpecification(String identifier) {
		Optional<ConceptBase> concept = lookup.getConcept(identifier);
		if ( concept.isPresent()) {
			if ( ConceptClass.class.isInstance(concept.get())) {
				
			}
			else if ( ConceptProperty.class.isInstance(concept.get())) {
				
			}
			else if ( ConceptPropertyValue.class.isInstance(concept.get())) {
				
			}
			else if ( ConceptPropertyUnit.class.isInstance(concept.get())) {
				
			}

		}
		return Optional.empty();
	}
	private DataSpecificationIEC61360 fromConceptClass(ConceptClass conceptClass) {
		return new DefaultDataSpecificationIEC61360.Builder()
			// shortName
			.shortName(new DefaultLangString.Builder()
					.text(conceptClass.getShortName())
					.language(Locale.ENGLISH.getLanguage())
					.build())
			// preferredName
			.preferredNames(mapLabel(conceptClass.getPreferredLabel()))
			// definition
			.definitions(mapLabel(conceptClass.getDefinition()))
			.build();
	}
	private DataSpecificationIEC61360 fromConceptProperty(ConceptProperty conceptClass) {
		return new DefaultDataSpecificationIEC61360.Builder()
			// shortName
			.shortName(new DefaultLangString.Builder()
					.text(conceptClass.getShortName())
					.language(Locale.ENGLISH.getLanguage())
					.build())
			// preferredName
			.preferredNames(mapLabel(conceptClass.getPreferredLabel()))
			// definition
			.definitions(mapLabel(conceptClass.getDefinition()))
			.dataType(DataTypeIEC61360.fromValue(conceptClass.getDataType().name()))
			.valueList(valueList(conceptClass.getConceptId()))
			.build();
	}
	private DataSpecificationPhysicalUnit fromConceptPropertyUnit(ConceptPropertyUnit unit) {
		return new DefaultDataSpecificationPhysicalUnit.Builder()
				.eceCode(unit.getEceCode())
				.eceName(unit.getEceName())
				.definitions(mapLabel(unit.getDefinition()))
				.siName(unit.getSiName())
				.siNotation(unit.getSiNotation())
				.dinNotation(unit.getDinNotation())
				.build();
				
	}
	private ValueList valueList(String property) {
		return new DefaultValueList.Builder().valueReferencePairs(mapValues(property)).build();
	}
	private List<ValueReferencePair> mapValues(String propertyIdentifier) {
		// obtain the values ...
		Collection<ConceptPropertyValue> values = lookup.getPropertyValues(propertyIdentifier);
		return values.stream().map(new Function<ConceptPropertyValue, ValueReferencePair>() {
		
					@Override
					public ValueReferencePair apply(ConceptPropertyValue t) {
						return new DefaultValueReferencePair.Builder()
								.value(t.getValue())
								.valueId(ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, t.getConceptId()))
								.build();
					}
				})
				.collect(Collectors.toList());
		
	}
	private List<LangString> mapLabel(final Map<Locale, String> map) {
		return map.keySet().stream().map(new Function<Locale, LangString>() {
	
				@Override
				public LangString apply(Locale t) {
					// TODO Auto-generated method stub
					return new DefaultLangString.Builder().text(map.get(t)).language(t.getISO3Language()).build();
				}
			})
			.collect(Collectors.toList());
	}
}

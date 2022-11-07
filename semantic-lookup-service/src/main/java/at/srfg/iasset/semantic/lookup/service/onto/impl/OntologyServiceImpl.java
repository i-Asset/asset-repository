package at.srfg.iasset.semantic.lookup.service.onto.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import at.srfg.iasset.semantic.lookup.service.ConceptClassService;
import at.srfg.iasset.semantic.lookup.service.PropertyService;
import at.srfg.iasset.semantic.lookup.service.onto.OntologyService;
import at.srfg.iasset.semantic.model.ConceptBase;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.DataTypeEnum;


@Service
public class OntologyServiceImpl implements OntologyService {
	@Value("${iAsset.ns.replace:}")
	private Boolean replaceNamespace;
	@Value("${iAsset.ns.replaceWith:}")
	private String nameSpace;
//	@Autowired
//	SemanticIndexing indexer;
	
	@Autowired
	PropertyService propertyService;
	@Autowired
	ConceptClassService conceptClassService;
	
	protected static final Logger logger = LoggerFactory.getLogger(OntologyServiceImpl.class);


	@Override
	public boolean delete(List<String> namespace) {
		if ( namespace != null && !namespace.isEmpty()) {
			for (String ns : namespace) {
				try {
					long classes = conceptClassService.deleteNameSpace(ns);
					
					long properties = propertyService.deleteNameSpace(ns);
				
				// 
//					indexer.deleteConcepts(ns);
				} catch (Exception e) {
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public void upload(String mimeType, String onto, List<String> nameSpaces) {
		//
		//List<String> nameSpaces = Arrays.asList(includedNamespaces);
	
		Lang l = Lang.RDFNULL;
		switch (mimeType) {
		case "application/rdf+xml":
			l = Lang.RDFXML;
			break;
		case "application/turtle":
			l = Lang.TURTLE;
			break;
		default:
		    // 
			return;
		}
		/*
		 * Create a Model with RDFS inferencing
		 */
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
		try {
			//
			StringReader reader = new StringReader(onto);
			/*
			 * Read the input string into the Ontology Model
			 */
			RDFParser.create()
				.source(reader)
				.errorHandler(ErrorHandlerFactory.errorHandlerStrict)
				.lang(l)
				.base("http://www.salzburgresearch.at/hmdp/")
				.parse(ontModel);
			
			/*
			 * Keep a list of indexed properties, use this list for
			 * mapping with classes 
			 */
			List<ConceptProperty> indexedProp = new ArrayList<>();
			Map<String, ConceptClass> indexedClass = new HashMap<>();
			/*
			 * Process all ontology properties, index them and fill
			 * the list of indexedProp
			 */
			Iterator<OntProperty> properties = ontModel.listAllOntProperties();
			while ( properties.hasNext()) {
				OntProperty p = properties.next();
				// restrict import to namespace list provided
				if (nameSpaces.isEmpty() || nameSpaces.contains(p.getNameSpace())) {
					if ( !p.isOntLanguageTerm()) {
						//
						ConceptProperty prop = processProperty(ontModel, p);
						if ( prop != null) {
							// store the property
							indexedProp.add(prop);
						}
					}
				}
			}
			Iterator<OntClass> rootIterator = ontModel.listHierarchyRootClasses();
			while (rootIterator.hasNext()) {
				OntClass c = rootIterator.next();
				
				if ( nameSpaces.isEmpty() || nameSpaces.contains(c.getNameSpace())) {
					
					if ( !c.isOntLanguageTerm()) {
						Optional<ConceptClass> cc = processClass(null, c);
						if (cc.isPresent()) {
							processProperties(cc.get(), c, indexedProp);
							processSubClasses(cc.get(), c, indexedProp);
						}
					}
				}
				
			}

		} finally {
			ontModel.close();
		}

	}
	private Optional<ConceptClass> processClass(final ConceptClass parent, final OntClass ontClass) {
		System.out.println("Processing " + ontClass.getURI() + " - " + ontClass.getLocalName());
		String localName = replaceNamespace ? localNameFromPrefLabel(ontClass) : ontClass.getLocalName();
		// 
		final String fullUri = replaceNamespace ?
				// true: replace the given namespace
				String.format("%s%s", nameSpace, localName) :
				// false: use the original namespace provided 
				ontClass.getURI();
		ConceptClass subCC = conceptClassService.getConcept(fullUri)
				.orElseGet(new Supplier<ConceptClass>() {
					public ConceptClass get() {
						return new ConceptClass(parent, fullUri);
					}
				});
		subCC.setShortName(localName);
		subCC.setCategory(ontClass.getRDFType().getLocalName());
		processLabels(subCC, ontClass);
		return conceptClassService.setConcept(subCC);
		
	}
	private void processSubClasses(final ConceptClass parentClass, final OntClass root, List<ConceptProperty> availableProps) {
		Iterator<OntClass> subIter = root.listSubClasses(true);
		while ( subIter.hasNext() ) {
			OntClass sub = subIter.next();
			Optional<ConceptClass> cc =  processClass(parentClass, sub);
			if ( cc.isPresent()) {
				processProperties(cc.get(), sub, availableProps);
				//
				processSubClasses(cc.get(), sub, availableProps);
			}
		}
	}
	
	private void processProperties(ConceptClass cc, OntClass ontClass, List<ConceptProperty> availableProperties) {
		List<ConceptProperty> assignedProps = new ArrayList<ConceptProperty>();
		// detect only directly assigned properties
		ExtendedIterator<OntProperty> prop = ontClass.listDeclaredProperties(true);
		while (prop.hasNext()) {
			OntProperty assignedProp = prop.next();
			Optional<ConceptProperty> propFound = availableProperties.stream().filter(new Predicate<ConceptProperty>() {
						@Override
						public boolean test(ConceptProperty t) {
							// TODO Auto-generated method stub
							return t.getConceptId().equals(assignedProp.getURI());
						}
					})
					.findFirst();
			if ( propFound.isPresent()) {
				assignedProps.add(propFound.get());
			}
		}
		conceptClassService.setProperties(cc.getConceptId(), assignedProps);
		
	}
	private DataTypeEnum fromRange(OntResource range) {
		return DataTypeEnum.STRING;
	}
	private String localNameFromPrefLabel(OntResource resource) {
    	// 
    	String localName = resource.getLocalName();
    	Map<Locale, String> pref = obtainMultilingualValues(resource, RDFS.label, DC.title, SKOS.prefLabel);
    	if (pref.containsKey(Locale.ENGLISH)) {
//    		localName = DynamicName.getDynamicFieldPart(pref.get(Locale.ENGLISH));
    	}
		return localName;
	}
    private ConceptProperty processProperty(OntModel model, OntProperty prop) {
    	String localName = localNameFromPrefLabel(prop);
		final String fullUri = replaceNamespace ?
				// true: replace the given namespace
				String.format("%s%s", nameSpace, localName) :
				// false: use the original namespace provided 
				prop.getURI();

        ConceptProperty index = propertyService.getConcept(fullUri)
        		.orElse(new ConceptProperty(fullUri));
        // process the labels
        processLabels(index, prop);
        //
        
        index.setDataType(fromRange(prop.getRange()));
        index.setShortName(localName);
        
        // deal with declaring classes
        propertyService.setConcept(index);
        return index;
    }
    /**
     * helper method processing all the labels (preferred, alternate, hidden) including description & comments
     * @param concept
     * @param resource
     */
    private void processLabels(ConceptBase concept, OntResource resource) {
    	concept.setPreferredLabel(obtainMultilingualValues(resource, RDFS.label, DC.title, SKOS.prefLabel));
    	concept.setAlternateLabel(obtainMultilingualLabels(resource, SKOS.altLabel));
    	concept.setHiddenLabel(obtainMultilingualLabels(resource, SKOS.hiddenLabel));
    	concept.setDefinition(obtainMultilingualValues(resource, DC.description, SKOS.definition));
    	concept.setComment(obtainMultilingualValues(resource, RDFS.comment, SKOS.note));
    }
	/**
	 * Helper method to extract multilingual labels
	 * @param prop
	 * @param properties
	 * @return
	 */
	private Map<Locale, String> obtainMultilingualValues(OntResource prop, Property ... properties ) {
		Map<Locale,String> languageMap = new HashMap<>();
		for (Property property : properties) {
			NodeIterator nIter = prop.listPropertyValues(property);
			while ( nIter.hasNext()) {
				RDFNode node = nIter.next();
				if ( node.isLiteral()) {
					Locale lang = Locale.ENGLISH;
					String nodeLang = node.asLiteral().getLanguage();
					if (nodeLang != null && !nodeLang.isEmpty()) {
						lang = Locale.forLanguageTag(nodeLang);
					}
					//String lang = node.asLiteral().getLanguage();
					
					if (! languageMap.containsKey(lang)) {
						languageMap.put(lang, node.asLiteral().getString());
					}
				}
			}
		}
		return languageMap;
		
	}

	/**
	 * Helper method to extract multilingual hidden and alternate labels
	 * @param prop
	 * @param properties
	 * @return
	 */
	private Map<Locale, Set<String>> obtainMultilingualLabels(OntResource prop, org.apache.jena.rdf.model.Property... properties) {

		Map<Locale, Set<String>> languageMap = new HashMap<Locale, Set<String>>();
		for (Property property : properties) {
			NodeIterator nIter = prop.listPropertyValues(property);
			while (nIter.hasNext()) {
				RDFNode node = nIter.next();
				if (node.isLiteral()) {
					Locale lang = Locale.forLanguageTag(node.asLiteral().getLanguage());
					if (languageMap.get(lang) != null) {
						Set<String> labelValues = languageMap.get(lang);
						labelValues.add(node.asLiteral().getString());
						languageMap.put(lang, labelValues);
					} else {
						Set<String> labelValues = new HashSet<String>();
						labelValues.add(node.asLiteral().getString());
						languageMap.put(lang, labelValues);
					}
				}
			}
		}
		return languageMap;

	}
}

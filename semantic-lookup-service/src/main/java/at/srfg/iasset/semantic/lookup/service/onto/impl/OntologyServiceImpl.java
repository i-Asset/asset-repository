package at.srfg.iasset.semantic.lookup.service.onto.impl;

import at.srfg.iasset.semantic.lookup.service.ConceptClassService;
import at.srfg.iasset.semantic.lookup.service.PropertyService;
import at.srfg.iasset.semantic.lookup.service.onto.OntologyService;
import at.srfg.iasset.semantic.model.ConceptBase;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.DataTypeEnum;
import org.apache.jena.ontapi.OntModelFactory;
import org.apache.jena.ontapi.OntSpecification;
import org.apache.jena.ontapi.model.OntClass;
import org.apache.jena.ontapi.model.OntModel;
import org.apache.jena.ontapi.model.OntProperty;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


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
		OntModel ontModel = OntModelFactory.createModel(OntSpecification.OWL1_FULL_MEM_RDFS_INF);
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
			try (Stream<OntProperty> propertyStream = ontModel.properties()) {
				Iterator<OntProperty> properties = propertyStream.iterator();
				while ( properties.hasNext()) {
					OntProperty p = properties.next();
					// restrict import to namespace list provided
					if (p.isURIResource() && (nameSpaces.isEmpty() || nameSpaces.contains(p.getNameSpace()))) {
						if ( !isOntLanguageTerm(p)) {
							//
							ConceptProperty prop = processProperty(ontModel, p);
							if ( prop != null) {
								// store the property
								indexedProp.add(prop);
							}
						}
					}
				}
			}
			try (Stream<OntClass> rootStream = ontModel.hierarchyRoots()) {
				Iterator<OntClass> rootIterator = rootStream.iterator();
				while (rootIterator.hasNext()) {
					OntClass c = rootIterator.next();

					if ( c.isURIResource() && (nameSpaces.isEmpty() || nameSpaces.contains(c.getNameSpace()))) {

						if ( !isOntLanguageTerm(c)) {
							Optional<ConceptClass> cc = processClass(null, c);
							if (cc.isPresent()) {
								processProperties(cc.get(), c, indexedProp);
								processSubClasses(cc.get(), c, indexedProp);
							}
						}
					}
				}
				
			}

		} finally {
			ontModel.close();
		}

	}
	private boolean isOntLanguageTerm(Resource resource) {
		if (!resource.isURIResource()) {
			return false;
		}
		String namespace = resource.getNameSpace();
		return RDF.getURI().equals(namespace)
				|| RDFS.getURI().equals(namespace)
				|| OWL.getURI().equals(namespace);
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
		try (Stream<Resource> types = ontClass.types()) {
			subCC.setCategory(types.findFirst().map(Resource::getLocalName).orElse(null));
		}
		processLabels(subCC, ontClass);
		return conceptClassService.setConcept(subCC);
		
	}
	private void processSubClasses(final ConceptClass parentClass, final OntClass root, List<ConceptProperty> availableProps) {
		try (Stream<OntClass> subStream = root.subClasses(true)) {
			Iterator<OntClass> subIter = subStream.iterator();
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
	}
	
	private void processProperties(ConceptClass cc, OntClass ontClass, List<ConceptProperty> availableProperties) {
		List<ConceptProperty> assignedProps = new ArrayList<ConceptProperty>();
		// detect only directly assigned properties
		try (Stream<OntProperty> propStream = ontClass.declaredProperties(true)) {
			Iterator<OntProperty> prop = propStream.iterator();
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
		}
		conceptClassService.setProperties(cc.getConceptId(), assignedProps);
		
	}
	private DataTypeEnum fromRange(Resource range) {
		return DataTypeEnum.STRING;
	}
	private String localNameFromPrefLabel(Resource resource) {
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
        
		try (Stream<? extends Resource> ranges = prop.ranges()) {
			index.setDataType(fromRange(ranges.findFirst().orElse(null)));
		}
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
    private void processLabels(ConceptBase concept, Resource resource) {
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
	private Map<Locale, String> obtainMultilingualValues(Resource prop, Property ... properties ) {
		Map<Locale,String> languageMap = new HashMap<>();
		for (Property property : properties) {
			NodeIterator nIter = prop.getModel().listObjectsOfProperty(prop, property);
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
	private Map<Locale, Set<String>> obtainMultilingualLabels(Resource prop, org.apache.jena.rdf.model.Property... properties) {

		Map<Locale, Set<String>> languageMap = new HashMap<Locale, Set<String>>();
		for (Property property : properties) {
			NodeIterator nIter = prop.getModel().listObjectsOfProperty(prop, property);
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

//package at.srfg.iasset.semantic.lookup.service.indexing;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//import at.srfg.iasset.semantic.lookup.dependency.SemanticIndexing;
//import at.srfg.iasset.semantic.lookup.repository.ConceptClassPropertyRepository;
//import at.srfg.iasset.semantic.lookup.repository.ConceptClassRepository;
//import at.srfg.iasset.semantic.model.ConceptBase;
//import at.srfg.iasset.semantic.model.ConceptClass;
//import at.srfg.iasset.semantic.model.ConceptProperty;
//import at.srfg.iot.common.solr.model.model.common.ClassType;
//import at.srfg.iot.common.solr.model.model.common.Concept;
//import at.srfg.iot.common.solr.model.model.common.DynamicName;
//import at.srfg.iot.common.solr.model.model.common.PropertyType;
//
//@Transactional
//@Component
//public class ConceptIndexingHandler {
////	@Autowired
////	SemanticIndexing indexer;
//	@Autowired
//	ConceptClassRepository ccRepo;
//	@Autowired
//	ConceptClassPropertyRepository conceptClassPropertyRepository;
//
//	/**
//	 * Event handler, taking care of indexing the concept class Event
//	 * @param event
//	 */
//	@Async
//	@TransactionalEventListener
//	public void onConceptClassEvent(ConceptClassEvent event) {
//		ConceptClass cc = event.getConcept();
//		try {
//			if ( event.isDelete() ) {
//				indexer.deleteClassType(Collections.singletonList(cc.getConceptId()));
//			}
//			else {
////				if ( cc.getParentElement() != null ) {
////					// 
////					ClassType parentType = asClassType(cc.getParentElement());
////					parentType.addChild(null);
////					indexer.setClassType(parentType);
////				}
//				ClassType cType = asClassType(cc);
//				indexer.setClassType(cType);
//			}
//		} catch (Exception e) {
//			
//		}
//	}
//	/**
//	 * Event handler, taking care of indexing the concept class Event
//	 * @param event
//	 */
//	@TransactionalEventListener @Async
//	public void onPropertyEvent(PropertyEvent event) {
//		ConceptProperty cc = event.getConcept();
//		try {
//			if ( event.isDelete() ) {
//				indexer.deletePropertyType(Collections.singletonList(cc.getConceptId()));
//			}
//			else {
//				PropertyType cType = asPropertyType(cc);
//				indexer.setPropertyType(cType);
//			}
//		} catch (Exception e) {
//			
//		}
//	}
//	/**
//	 * Helper method for mapping {@link ConceptClass } data to the indexed {@link ClassType}
//	 * @param cc The {@link ConceptClass}
//	 * @return The {@link ClassType} finally stored
//	 */
//
//	private ClassType asClassType(ConceptClass cc) {
//		ClassType cType = new ClassType();
//		cType.setUri(cc.getConceptId());
//		cType.setNameSpace(cc.getNameSpace());
//		cType.setLocalName(cc.getLocalName());
//		cType.setCode(cc.getCodedName());
//		cType.setType(cc.getCategory());
//		// 
//		cType.setParents(getParentIdentifier(cc.getParentElement(), false));
//		cType.setAllParents(getParentIdentifier(cc.getParentElement(), true));
//		cType.setChildren(getChildrenIdentifier(cc, false));
//		cType.setAllChildren(getChildrenIdentifier(cc, true));
//		cType.setLevel(cc.getLevel());
//		// handle concept properties
//		handleConceptProperties(cc, cType);
//		
//		// map the description
//		handleConceptDescription(cc, cType);
//		
//		// (re)use the preferred labels from all parents as hidden labels
//		handleParentLabelsAsHidden(cc.getParentElement(), cType);
//		return cType;		
//	}
//	private void handleParentLabelsAsHidden(ConceptClass conceptClass, ClassType concept) {
//		Map<Locale, Set<String>> hidden = getParentLabels(conceptClass, true);
//		for ( Locale locale : hidden.keySet()) {
//			for (String hiddenLabel : hidden.get(locale)) {
//				concept.addHiddenLabel(locale, hiddenLabel);
//			}
//		}
//		
//	}
//	private Map<Locale, Set<String>> getParentLabels(ConceptClass conceptClass, boolean all) {
//		Map<Locale,Set<String>> collectedLabels = new HashMap<>();
//		if ( conceptClass != null) {
//			// add the element's concept identifier
//			for (Locale locale : conceptClass.getPreferredLabel().keySet() ) {
//				if (!collectedLabels.containsKey(locale)) {
//					collectedLabels.put(locale, new HashSet<>());
//				}
//				Optional<String> languagePref = conceptClass.getPreferredLabel(locale);
//				if ( languagePref.isPresent()) {
//					collectedLabels.get(locale).add(languagePref.get());
//				}
//			}
//			if ( all && conceptClass.getParentElement()!= null ) {
//				Map<Locale,Set<String>> collectedParent = getParentLabels(conceptClass.getParentElement(), all);
//				for ( Locale locale : collectedParent.keySet()) {
//					if (!collectedLabels.containsKey(locale)) {
//						collectedLabels.put(locale, collectedParent.get(locale));
//					}
//					else {
//						collectedLabels.get(locale).addAll(collectedParent.get(locale));
//					}
//				}
//			}
//		}
//		return collectedLabels;
//		
//	}
//	/**
//	 * Map all description elements 
//	 * @param base
//	 * @param concept
//	 */
//	private void handleConceptDescription(ConceptBase base, Concept concept) {
//		Map<Locale, String> prefLabel = base.getPreferredLabel();
//		for ( Locale locale : prefLabel.keySet()) {
//			concept.setLabel(locale, prefLabel.get(locale));
//		}
//		Map<Locale, Set<String>> altLabel = base.getAlternateLabel();
//		for ( Locale locale : altLabel.keySet()) {
//			for (String alt : altLabel.get(locale)) {
//				concept.addAlternateLabel(locale, alt);
//			}
//		}
//		Map<Locale, Set<String>> hidden = base.getHiddenLabel();
//		for ( Locale locale : hidden.keySet()) {
//			for (String hiddenLabel : hidden.get(locale)) {
//				concept.addHiddenLabel(locale, hiddenLabel);
//			}
//		}
//		
//		Map<Locale, String> comments = base.getComment();
//		for ( Locale locale : comments.keySet()) {
//			concept.addComment(locale.getLanguage(), comments.get(locale));
//		}	
//		Map<Locale, String> definitions = base.getDefinition();
//		for ( Locale locale : definitions.keySet()) {
//			concept.addDescription(locale, definitions.get(locale));
//		}
//	}
//	/**
//	 * Map all properties of the {@link ConceptClass} and add them to the {@link ClassType}
//	 * @param cc The {@link ConceptClass}
//	 * @param cType {@link ClassType}
//	 */
//	private void handleConceptProperties(ConceptClass cc, ClassType cType) {
//		Set<ConceptProperty> properties = getProperties(cc);
//		for (ConceptProperty property : properties) {
//			PropertyType pType = asPropertyType(property);
//			cType.addProperty(pType);
//		}
//	}
//	/**
//	 * Helper method for mapping {@link ConceptProperty} data to the indexed {@link PropertyType}
//	 * @param property the {@link ConceptProperty} to store in the index
//	 * @return The {@link PropertyType} finally stored
//	 */
//	private PropertyType asPropertyType(ConceptProperty property) {
//		PropertyType pType = new PropertyType();
//		pType.setUri(property.getConceptId());
//		pType.setNameSpace(property.getNameSpace());
//		pType.setLocalName(property.getLocalName());
//		pType.setCode(property.getShortName());
//		// add as item fieldNames: 
//		// - the (preferred labels)
//		// - the short name
//		// - the localName
//		Map<Locale, String> prefLabel = property.getPreferredLabel();
//		for ( Locale locale : prefLabel.keySet()) {
//			pType.addItemFieldName(DynamicName.getDynamicFieldPart(prefLabel.get(locale)));
//		}
//		// add short name when set 
//		if ( property.getShortName()!= null) {
//			pType.addItemFieldName(DynamicName.getDynamicFieldPart(property.getShortName()));
//		}
//		pType.addItemFieldName(DynamicName.getDynamicFieldPart(property.getLocalName()));
//		// map the description
//		handleConceptDescription(property, pType);
//		//
//		return pType;
//	}
//	/**
//	 * Helper method to extract the {@link ConceptBase#getConceptId()} of the elements' children
//	 * @param cc The {@link ConceptClass} 
//	 * @param all <code>true</code> to include all children, <code>false</code> for the direct children only
//	 * @return
//	 */
//	private Set<String> getChildrenIdentifier(ConceptClass cc, boolean all) {
//		Set<String> children = new HashSet<>();
//		List<ConceptClass> subClasses = ccRepo.findByParentElement(cc);
//		for (ConceptClass child : cc.getChildElements()) {
//			children.add(child.getConceptId());
//			if ( all ) {
//				children.addAll(getChildrenIdentifier(child, all));
//			}
//		}
//		return children;
//	}
//	/**
//	 * Helper method to extract the {@link ConceptBase#getConceptId()} of the elements' parents
//	 * @param conceptClass The {@link ConceptClass}
//	 * @param all <code>true</code> to include all parents, <code>false</code> for the direct parent only
//	 * @return
//	 */
//	private Set<String> getParentIdentifier(ConceptClass conceptClass, boolean all) {
//		Set<String> parent = new HashSet<>();
//		if ( conceptClass != null) {
//			// add the element's concept identifier
//			parent.add(conceptClass.getConceptId());
//			if ( all && conceptClass.getParentElement()!= null ) {
//				parent.addAll(getParentIdentifier(conceptClass.getParentElement(),all));
//			}
//			
//		}
//		return parent;
//
//	}
//	/**
//	 * Helper method to retrieve all properties (including the properties of parent elements)
//	 * @param cc The {@link ConceptClass}
//	 * @return
//	 */
//	private Set<ConceptProperty> getProperties(ConceptClass cc) {
//		Set<ConceptProperty> properties = new HashSet<>();
//		if ( cc.getParentElement() != null) {
//			properties.addAll(getProperties(cc.getParentElement()));
//		}
//		properties.addAll(conceptClassPropertyRepository.getProperties(cc));
//		return properties;
//
//	}
//}

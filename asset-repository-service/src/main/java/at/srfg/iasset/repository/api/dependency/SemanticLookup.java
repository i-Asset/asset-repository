package at.srfg.iasset.repository.api.dependency;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import at.srfg.iasset.semantic.api.SemanticLookupService;
import at.srfg.iasset.semantic.model.ConceptBase;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;

@FeignClient(name = "semantic-lookup-service")
public interface SemanticLookup {

	/*
	 * Generic /concept methods
	 * - /concept (GET,PUT,POST,DELETE)
	 * - /concept/description (GET,POST)
	 */
	/**
	 * Read a concept element based on it's identifier. The returned object
	 * is either a
	 * <ul>
	 * <li>ConceptClass
	 * <li>Property
	 * <li>PropertyValue
	 * <li>PropertyUnit
	 * </ul>
	 * The respective type is stored in the <code>conceptType</code>-element.
	 * @param identifier
	 * @return
	 */
	@GetMapping("/concept")
	Optional<ConceptBase> getConcept(@RequestParam("id") String identifier);

	/**
	 * Add a new concept element to the repository. The stored object
	 * must be either a
	 * <ul>
	 * <li>ConceptClass
	 * <li>Property
	 * <li>PropertyValue
	 * <li>PropertyUnit
	 * </ul>
	 * The respective type is stored in the <code>conceptType</code>-element.
	 * @param identifier
	 * @return
	 */
	@PutMapping("/concept")
	Optional<ConceptBase> addConcept(ConceptBase concept);

	/**
	 * Change an existing concept element in the repository. The changed object
	 * must be either a
	 * <ul>
	 * <li>ConceptClass
	 * <li>Property
	 * <li>PropertyValue
	 * <li>PropertyUnit
	 * </ul>
	 * The respective type is stored in the <code>conceptType</code>-element.
	 * @return
	 */
	@PostMapping("/concept")
	Optional<ConceptBase> setConcept(ConceptBase concept);

	/**
	 * Delete an existing concept element from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@DeleteMapping("/concept")
	boolean deleteConcept(@RequestParam("id") String identifier);

	//	/**
	//	 * Add or update the multi-lingual description of the 
	//	 * concept. The concept must already exist and the provided 
	//	 * description must have the language set.
	//	 * @param identifier The URI or IRDI of the concept element
	//	 * @param description The description to add or update.
	//	 * @return
	//	 */
	//	@ApiOperation(
	//			value = "Add or update the multi-lingual description of a concept element",
	//			notes = "The resulting object is either a ConceptClass, Property, PropertyValue or PropertyUnit")
	//	@RequestMapping(
	//			method = RequestMethod.POST,
	//			path="/concept/description")
	//	public Optional<ConceptBase> setConceptDescription(
	//			@ApiParam("Concept Identifier (IRDI or URI)")
	//			@RequestParam("id") String identifier, 
	//			@RequestBody Object description) ;
	/*
	 * Concept Class Methods
	 * - /concept/class (GET,PUT,POST,DELETE)
	 * - /concept/class/properties (GET)
	 * 
	 */
	/**
	 * Read a {@link ConceptClass} from the semantic lookup service
	 * @param identifier The URI or IRDI  of the concept class
	 * @see SemanticLookupService#getConcept(String)
	 * @return
	 */
	@GetMapping("/concept/class")
	Optional<ConceptClass> getConceptClass(@RequestParam("id") String identifier);

	/**
	 * Add a new concept class to the Semantic Lookup Repository
	 * @param conceptClass The full description of the class to add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@PutMapping("/concept/class")
	Optional<ConceptClass> addConceptClass(@RequestParam("parentIdentifier") String parentIdentifier, ConceptClass conceptClass);

	/**
	 * Add a new concept class to the Semantic Lookup Repository
	 * @param conceptClass The full description of the class to add
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@PostMapping("/concept/class")
	Optional<ConceptClass> setConceptClass(ConceptClass conceptClass);

	/**
	 * Delete an existing concept class element from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@DeleteMapping("/concept/class")
	boolean deleteConceptClass(@RequestParam("id") List<String> identifier);

	/**
	 * Obtain the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@GetMapping("/concept/class/properties")
	Collection<ConceptProperty> getPropertiesForConceptClass(@RequestParam("id") String identifier, @RequestParam("complete") boolean complete);

	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@PostMapping("/concept/class/properties")
	Collection<ConceptProperty> setPropertiesForConceptClass(@RequestParam("id") String identifier,
			List<ConceptProperty> conceptPropertyList);

	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@PutMapping("/concept/class/properties")
	Collection<ConceptProperty> setPropertiesByIdForConceptClass(@RequestParam("id") String identifier, @RequestParam("property") List<String> propertyList);

	/**
	 * Get the list of values for a property, optionally related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@GetMapping("/concept/class/property/values")
	Collection<ConceptPropertyValue> getPropertyValues(@RequestParam("classId") String conceptClassIdentifier, @RequestParam("propertyId") String conceptPropertyIdentifier);

	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@PostMapping("/concept/class/property/values")
	Collection<ConceptPropertyValue> setPropertyValuesForConceptClass(@RequestParam("classId") String conceptClassIdentifier,
			@RequestParam("propertyId") String conceptPropertyIdentifier, List<ConceptPropertyValue> conceptPropertyList);

	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@PutMapping("/concept/class/property/values")
	Collection<ConceptPropertyValue> setPropertyValuesByIdForConceptClass(
			@RequestParam("classId") String conceptClassIdentifier,
			 @RequestParam("propertyId") String conceptPropertyIdentifier, 
			 @RequestParam("conceptPropertyValue") List<String> propertyValueList);

	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@DeleteMapping("/concept/class/property/values")
	Collection<ConceptPropertyValue> setPropertyValues(
			 @RequestParam("classId") String conceptClassIdentifier, 
			 @RequestParam("propertyId") String conceptPropertyIdentifier,
			 @RequestParam("conceptPropertyValue") List<String> propertyValueList);

	/*
	 * Property Methods
	 * - /concept/property (GET,PUT,POST,DELETE)
	 * - /concept/property/values (GET)
	 * 
	 */
	/**
	 * Read a {@link ConceptProperty} from the semantic lookup service
	 * @param identifier The URI or IRDI  of the property
	 * @see SemanticLookupService#getConcept(String)
	 * @return
	 */
	@GetMapping("/concept/property")
	Optional<ConceptProperty> getProperty( @RequestParam("id") String identifier);

	/**
	 * Add a new property to the Semantic Lookup Repository
	 * @param property The full description of the property to add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@PutMapping("/concept/property")
	Optional<ConceptProperty> addProperty(ConceptProperty property);

	/**
	 * Update an existing property in the Semantic Lookup Repository
	 * @param property The full description of the property to change
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@PostMapping("/concept/property")
	Optional<ConceptProperty> setProperty(ConceptProperty property);

	/**
	 * Delete an existing property from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@DeleteMapping("/concept/property")
	boolean deleteProperty(@RequestParam("id") List<String> identifier);

	/**
	 * Obtain the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@GetMapping("/concept/property/values")
	Collection<ConceptPropertyValue> getPropertyValues(@RequestParam("id") String identifier);

	/*
	 * PropertyUnit Methods
	 * - /concept/unit (GET,PUT,POST,DELETE)
	 * 
	 */
	/**
	 * Read a {@link ConceptPropertyUnit} from the semantic lookup service
	 * @param identifier The URI or IRDI  of the property
	 * @see SemanticLookupService#getConcept(String)
	 * @return
	 */
	@GetMapping("/concept/unit")
	Optional<ConceptPropertyUnit> getPropertyUnit(@RequestParam("id") String identifier);

	/**
	 * Add a new property to the Semantic Lookup Repository
	 * @param property The full description of the property to add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@PutMapping("/concept/unit")
	Optional<ConceptPropertyUnit> addPropertyUnit(ConceptPropertyUnit property);

	/**
	 * Update an existing property in the Semantic Lookup Repository
	 * @param property The full description of the property to change
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@PostMapping("/concept/unit")
	Optional<ConceptPropertyUnit> setPropertyUnit(ConceptPropertyUnit property);

	/**
	 * Delete an existing property from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@DeleteMapping("/concept/unit")
	boolean deletePropertyUnit(@RequestParam("id") List<String> identifier);

	/*
	 * PropertyValue Methods
	 * - /concept/value (GET,PUT,POST,DELETE)
	 * 
	 */
	/**
	 * Read a {@link ConceptPropertyUnit} from the semantic lookup service
	 * @param identifier The URI or IRDI  of the property
	 * @see SemanticLookupService#getConcept(String)
	 * @return
	 */
	@GetMapping("/concept/value")
	Optional<ConceptPropertyValue> getPropertyValue(@RequestParam("id") String identifier);

	/**
	 * Add a new property value to the Semantic Lookup Repository
	 * @param property The full description of the property valueto add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@PutMapping("/concept/value")
	Optional<ConceptPropertyValue> addPropertyValue(ConceptPropertyValue property);

	/**
	 * Update an existing property in the Semantic Lookup Repository
	 * @param property The full description of the property to change
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@PostMapping("/concept/value")
	Optional<ConceptPropertyValue> setPropertyValue(ConceptPropertyValue property);

	/**
	 * Delete an existing property value from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@DeleteMapping("/concept/value")
	boolean deletePropertyValue(@RequestParam("id") List<String> id);

}
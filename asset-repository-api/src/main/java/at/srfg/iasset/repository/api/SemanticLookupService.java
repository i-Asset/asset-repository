package at.srfg.iasset.repository.api;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.srfg.iasset.semantic.model.ConceptBase;
import at.srfg.iasset.semantic.model.ConceptClass;
import at.srfg.iasset.semantic.model.ConceptProperty;
import at.srfg.iasset.semantic.model.ConceptPropertyUnit;
import at.srfg.iasset.semantic.model.ConceptPropertyValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
/**
 * Service interface
 * @author dglachs
 *
 */
@Path("")
@RequestMapping(path = "")
public interface SemanticLookupService {
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
	@Operation(
			summary = "Read a concept element by its identifier",
			description = "The result is of type: ConceptClass, Property, PropertyValue or PropertyUnit")
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept")
	public ConceptBase getConcept(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "Concept Identifier (IRDI or URI)")
			@RequestParam("id") 
			String identifier) ;
	
	
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
	@Operation(
			summary = "Add a new concept element to the Semantic Lookup Repository",
			description = "The object must be of type: ConceptClass, Property, PropertyValue or PropertyUnit")
	@RequestMapping(
			method = RequestMethod.PUT,
			path="/concept")
	public ConceptBase addConcept(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "Concept Identifier (IRDI or URI)")
			@RequestBody ConceptBase concept) ;
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
	@Operation(
			summary = "Change an existing concept element in the Semantic Lookup Repository",
			description = "The object must be of type: ConceptClass, Property, PropertyValue or PropertyUnit")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/concept")
	public ConceptBase setConcept(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "Concept Identifier (IRDI or URI)")
			@RequestBody ConceptBase concept) ;
	/**
	 * Delete an existing concept element from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@Operation(
			summary = "Delete a concept from the Semantic Lookup Repository",
			description = "NOTE: Dependent data is also deleted")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/concept")
	public boolean deleteConcept(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "Concept Identifier (IRDI or URI)")
			@RequestParam("id") String identifier) ;
//	/**
//	 * Add or update the multi-lingual description of the 
//	 * concept. The concept must already exist and the provided 
//	 * description must have the language set.
//	 * @param identifier The URI or IRDI of the concept element
//	 * @param description The description to add or update.
//	 * @return
//	 */
//	@Operation(
//			value = "Add or update the multi-lingual description of a concept element",
//			notes = "The resulting object is either a ConceptClass, Property, PropertyValue or PropertyUnit")
//	@RequestMapping(
//			method = RequestMethod.POST,
//			path="/concept/description")
//	public Optional<ConceptBase> setConceptDescription(
//			@Parameter("Concept Identifier (IRDI or URI)")
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
	@Operation(
			summary = "Read a ConceptClass element based on it's ID",
			description = "Read the ConceptClass with it's full URI or IRDI")
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept/class")
	public ConceptClass getConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("id") String identifier) ;
	
	/**
	 * Add a new concept class to the Semantic Lookup Repository
	 * @param conceptClass The full description of the class to add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary = "Add a new ConceptClass element to the repository",
			description = "A new class is added, the parentElement may only provided as parameter!")
	@RequestMapping(
			method = RequestMethod.PUT,
			path="/concept/class")
	public ConceptClass addConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = false,
					description = "The ConceptClass Identifier (IRDI or URI) of the parent class - optional")
			@RequestParam(name = "parentIdentifier", required = false) 
			String parentIdentifier,
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the ConceptClass")
			@RequestBody 
			ConceptClass conceptClass) ;
	
	/**
	 * Add a new concept class to the Semantic Lookup Repository
	 * @param conceptClass The full description of the class to add
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary =  "Update an existing ConceptClass in the repository",
			description = "Note: the ConceptClass might contain additional data (parent class) this is not changed")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/concept/class")
	public ConceptClass setConceptClass(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the concept class")
			@RequestBody ConceptClass conceptClass) ;

	/**
	 * Delete an existing concept class element from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@Operation(
			summary = "Delete a ConceptClass from the Semantic Lookup Repository",
			description = "NOTE: Dependent data is also deleted")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/concept/class")
	public boolean deleteConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("id") List<String> identifier) ;
	
	/**
	 * Obtain the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Read all Property data relevant for the given class",
			description = "Obtain all relevant properties for given concept class"
			)
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept/class/properties")
	public Collection<ConceptProperty> getPropertiesForConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("id") String identifier,
			@RequestParam(name="complete", required=false, defaultValue="true" ) 
			boolean complete) ;

	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Add all Property data relevant for the given class",
			description = "Specify all relevant properties for given concept class"
			)
	@RequestMapping(
			method = RequestMethod.POST,
			path="/concept/class/properties")
	public Collection<ConceptProperty> setPropertiesForConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("id") 
			String identifier,
			@RequestBody
			List<ConceptProperty> conceptPropertyList) ;
	
	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Add all Property data relevant for the given class",
			description = "Specify all relevant properties for given concept class"
			)
	@RequestMapping(
			method = RequestMethod.PUT,
			path="/concept/class/properties")
	public Collection<ConceptProperty> setPropertiesByIdForConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("id") String identifier,
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "List of Property Identifier (IRDI or URI")
			@RequestParam(name = "property")
			List<String> propertyList) ;

	
	/**
	 * Get the list of values for a property, optionally related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Obtain the values allowed for the provided property, (optionally) when used with the given class",
			description = "Specify all relevant properties for given concept class"
			)
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept/class/property/values")
	public Collection<ConceptPropertyValue> getPropertyValues(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam(name="classid", required=false) String conceptClassIdentifier,
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptProperty Identifier (IRDI or URI)")
			@RequestParam("propertyId") String conceptPropertyIdentifier) ;
	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Specify the values allowed for the provided property, when used with the given class",
			description = "Specify all relevant properties for given concept class"
			)
	@RequestMapping(
			method = RequestMethod.POST,
			path="/concept/class/property/values")
	public Collection<ConceptPropertyValue> setPropertyValuesForConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("classid") 
			String conceptClassIdentifier,
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("propertyId") 
			String conceptPropertyIdentifier,
			@RequestBody
			List<ConceptPropertyValue> conceptPropertyList) ;

	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Specify the values allowed for the provided property, when used with the given class",
			description = "Specify all relevant properties for given concept class"
			)
	@RequestMapping(
			method = RequestMethod.PUT,
			path="/concept/class/property/values")
	public Collection<ConceptPropertyValue> setPropertyValuesByIdForConceptClass(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("classid") String conceptClassIdentifier,
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("propertyId") String conceptPropertyIdentifier,
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "List of Property Identifier (IRDI or URI")
			@RequestParam(name = "conceptPropertyValue")
			List<String> propertyValueList) ;
	/**
	 * Set the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Specify the values allowed for the provided property, when used with the given class",
			description = "Specify all relevant properties for given concept class"
			)
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/concept/class/property/values")
	public Collection<ConceptPropertyValue> setPropertyValues(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam(name="classid", required=false) String conceptClassIdentifier,
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "ConceptClass Identifier (IRDI or URI)")
			@RequestParam("propertyId") String conceptPropertyIdentifier,
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "List of Property Identifier (IRDI or URI")
			@RequestParam(name = "conceptPropertyValue")
			List<String> propertyValueList) ;
	
	
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
	@Operation(
			summary = "Obtain a Property based on it's ID",
			description = "Read the ClassificationClass with it's full IRDI")
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept/property")
	public ConceptProperty getProperty(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "Property Identifier (IRDI or URI)")
			@RequestParam("id") String identifier) ;
	
	/**
	 * Add a new property to the Semantic Lookup Repository
	 * @param property The full description of the property to add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary = "Add a new property to the repository",
			description = "Read the Property with it's full IRDI")
	@RequestMapping(
			method = RequestMethod.PUT,
			path="/concept/property")
	public ConceptProperty addProperty(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the Property")
			@RequestBody ConceptProperty property) ;
	
	/**
	 * Update an existing property in the Semantic Lookup Repository
	 * @param property The full description of the property to change
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary = "Update an existing Property in the repository",
			description = "Note: dependend data (PropertyUnit, PropertyValue) might be provided")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/concept/property")
	public ConceptProperty setProperty(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the Property")
			@RequestBody ConceptProperty property) ;

	/**
	 * Delete an existing property from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@Operation(
			summary = "Delete one or more from the Semantic Lookup Repository",
			description = "NOTE: Dependent data (e.g. Assignment to PropertyValues) is also deleted")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/concept/property")
	public boolean deleteProperty(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "Property Identifier (IRDI or URI)")
			@RequestParam("id") List<String> identifier) ;
	
	/**
	 * Obtain the list of properties related to a provided concept class
	 * @param identifier
	 * @return
	 */
	@Operation(
			summary = "Read all values relevant for the given property, optionally aligned to a given ConceptClass",
			description = "When the ConceptClass Identifier is provided, the value list is checked for the particular ConceptClass"
			)
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept/property/values")
	public Collection<ConceptPropertyValue> getPropertyValues(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "Property Identifier (IRDI or URI)")
			@RequestParam("id") String identifier);


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
	@Operation(
			summary = "Obtain a PropertyUnit based on it's ID",
			description = "Read the PropertyUnit with it's full IRDI")
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept/unit")
	public ConceptPropertyUnit getPropertyUnit(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "PropertyUnit Identifier (IRDI or URI)")
			@RequestParam("id") String identifier) ;
	
	/**
	 * Add a new property to the Semantic Lookup Repository
	 * @param property The full description of the property to add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary = "Add a new property to the repository",
			description = "Read the PropertyUnit with it's full IRDI")
	@RequestMapping(
			method = RequestMethod.PUT,
			path="/concept/unit")
	public ConceptPropertyUnit addPropertyUnit(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the PropertyUnit")
			@RequestBody ConceptPropertyUnit property) ;
	
	/**
	 * Update an existing property in the Semantic Lookup Repository
	 * @param property The full description of the property to change
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary = "Update an existing property unit in the repository",
			description = "Read the PropertyUnit with it's full URI or IRDI")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/concept/unit")
	public ConceptPropertyUnit setPropertyUnit(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the PropertyUnit")
			@RequestBody ConceptPropertyUnit property) ;

	/**
	 * Delete an existing property from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@Operation(
			summary = "Delete a property unit from the Semantic Lookup Repository",
			description = "NOTE: Dependent data is also deleted")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/concept/unit")
	public boolean deletePropertyUnit(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "PropertyUnit Identifier (IRDI or URI)")
			@RequestParam("id") List<String> identifier) ;

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
	@Operation(
			summary = "Obtain a PropertyValue based on it's ID",
			description = "Read the PropertyValue with it's full IRDI")
	@RequestMapping(
			method = RequestMethod.GET,
			path="/concept/value")
	public ConceptPropertyValue getPropertyValue(
			@Parameter(
					in = ParameterIn.QUERY, 
					required = true,
					description ="PropertyValue Identifier (IRDI or URI)")
			@RequestParam("id") String identifier) ;
	
	/**
	 * Add a new property value to the Semantic Lookup Repository
	 * @param property The full description of the property valueto add
	 * @see SemanticLookupService#addConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary = "Add a new property value to the repository",
			description = "Read the value with it's full IRDI")
	@RequestMapping(
			method = RequestMethod.PUT,
			path="/concept/value")
	public ConceptPropertyValue addPropertyValue(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the PropertyValue")
			@RequestBody 
			ConceptPropertyValue property) ;
	
	/**
	 * Update an existing property in the Semantic Lookup Repository
	 * @param property The full description of the property to change
	 * @see SemanticLookupService#setConcept(ConceptBase)
	 * @return
	 */
	@Operation(
			summary = "Update an existing property value in the repository",
			description = "Read the Property Value with it's full IRDI")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/concept/value")
	public ConceptPropertyValue setPropertyValue(
			@Parameter(
					in=ParameterIn.DEFAULT,
					required = true,
					description = "The full description for the property value")
			@RequestBody ConceptPropertyValue property) ;

	/**
	 * Delete an existing property value from the repository. 
	 * @param identifier The URI or IRDI
	 * @return
	 */
	@Operation(
			summary = "Delete a PropertyValue from the Semantic Lookup Repository",
			description = "NOTE: Dependent data is also deleted")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/concept/value")
	public boolean deletePropertyValue(
			@Parameter(
					in=ParameterIn.QUERY,
					required = true,
					description = "PropertyValue Identifier (IRDI or URI)")
			@RequestParam("id") List<String> id);



	
}

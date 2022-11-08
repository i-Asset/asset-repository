package at.srfg.iasset.repository.api;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Identifiable;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.srfg.iasset.repository.api.annotation.Base64Encoded;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST-API for accessing a single {@link AssetAdministrationShell}.
 * 
 * 
 * 
 * The repository endpoint requires the identification of each {@link AssetAdministrationShell}. 
 * The defined API methods therefore are relative to the {@link Identifiable} element.
 * @author dglachs
 *
 */
@Path("")
@RequestMapping(path = "")
public interface IAssetAdministrationShellRepositoryInterface {
	final String PATH_SHELLS 					= "/shells";
	final String AAS_IDENTIFIER					= "/{aasIdentifier}";
	final String PATH_AAS_SUBMODELS				= "/aas/submodels";
	final String SUBMODEL_IDENTIFIER			= "/{submodelIdentifier}";
	final String PATH_SUBMODEL_ELEMENTS			= "/submodel/submodel-elements";
	final String IDSHORT_PATH 					= "/{path}";
	final String IDSHORT_PATH_WILDCARD			= "/{path:.+}";
	final String PATH_CONCEPT_DESCRIPTION		= "/concept-description";
	final String CD_IDENTIFIER					= "/{cdIdentifier}";
	
	/**
	 * Obtain the {@link AssetAdministrationShell} 
	 * @return
	 */
	@Operation(summary =  "Get the AssetAdministrationShell",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path=PATH_SHELLS)
	public List<AssetAdministrationShell> getAssetAdministrationShells();
	
	/**
	 * Obtain the {@link AssetAdministrationShell} 
	 * @return
	 */
	@Operation(summary =  "Get the AssetAdministrationShell",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path=PATH_SHELLS + AAS_IDENTIFIER)
	public AssetAdministrationShell getAssetAdministrationShell(
			@Base64Encoded
			@PathParam("aasIdentifier")
			
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String identifier);
	/**
	 * Update the {@link AssetAdministrationShell} 
	 * @return
	 */
	@Operation(summary =  "Add a AssetAdministrationShell to the Repository",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.PUT,
			path=PATH_SHELLS + AAS_IDENTIFIER)
	public AssetAdministrationShell setAssetAdministrationShell(
			@Base64Encoded
			@PathParam("aasIdentifier")
			
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String identifier,
			
			@Parameter(
					in = ParameterIn.DEFAULT,
					description = "Asset Administration Shell object", 
					required = true, 
					schema = @Schema()) 
			@Valid 
			@RequestBody
			AssetAdministrationShell assetAdministrationShell);
	/**
	 * Update the {@link Submodel} 
	 * @return
	 */
	@Operation(summary =  "Add or update a Submodel to the Repository",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.PUT,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public Submodel setSubmodel(
			@Base64Encoded
			@PathParam("aasIdentifier")
			
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String aasIdentifier,
			
			@Base64Encoded		
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier") 
			String submodelIdentifier,
			
			@Parameter(
					in = ParameterIn.DEFAULT,
					description = "Submodel", 
					required = true, 
					schema = @Schema()) 
			@Valid 
			@RequestBody
			Submodel submodel);
	
	/**
	 *  Obtain a {@link Submodel} of the identified {@link AssetAdministrationShell} 
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @return
	 */
	@Operation(summary =  "Get the Submodel",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public Submodel getSubmodel(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Submodels’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier
			);
	/**
	 * Obtain the submodel {@link Reference} of the {@link AssetAdministrationShell}
	 * @return
	 */
	@Operation(summary =  "Obtain the submodel references from the AAS",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS)
	public List<Reference> getSubmodels(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String aasIdentifier);
	/**
	 * Update the submodel {@link Reference} of the {@link AssetAdministrationShell}
	 * @return
	 */
	@Operation(summary =  "Update the submodel references of the identified AAS",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS)
	public List<Reference> setSubmodels(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String aasIdentifier,
			@RequestBody
			List<Reference> submodels
			);

	/**
	 * Delete the submodel {@link Reference} of the {@link AssetAdministrationShell}
	 * @return
	 */
	@Operation(summary =  "Update the submodel references of the identified AAS",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public List<Reference> removeSubmodelReference(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier") 
			String submodelIdentifier
			);
	
//	/**
//	 * Obtain an model element based on the reference, may be a {@link Submodel} or a {@link SubmodelElement}
//	 * @param aasIdentifier
//	 * @param element Reference to the requested element
//	 * @return
//	 */
//	@Operation(summary =  "Obtain a referable element by it's reference",
//			tags = "Asset Administration Shell Repository Interface (for Connector)")
//	@RequestMapping(
//			method = RequestMethod.POST,
//			path="/shells/{aasIdentifier}/reference")
//	public Referable getSubmodelElement(
//			@Base64Encoded
//			@Parameter(
//					in = ParameterIn.PATH, 
//					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
//					required = true, 
//					schema = @Schema()) 
//			@PathVariable("aasIdentifier")
//			String aasIdentifier,			
//			@RequestBody
//			Reference element);
	
	/**
	 * Obtain an model element based on the reference, may be a {@link Submodel} or a {@link SubmodelElement}
	 * @param aasIdentifier
	 * @param element Reference to the requested element
	 * @return
	 */
	@Operation(summary =  "Obtain a referable element by it's reference",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH_WILDCARD)
	public Referable getSubmodelElement(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier")
			String aasIdentifier,			
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier,			
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The path to the requested element", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("path")
			String path	);

	/**
	 * Add a new model element to the {@link IAssetAdministrationShellRepositoryInterface}. The element must
	 * contain a proper parent element (see {@link Reference}) pointing to it's root container!
	 * @param element The element to add
	 * @return
	 */
	@Operation(summary =  "Update a submodel's submodel element",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS)
	public void setSubmodelElement(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier")
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Submodels’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier,
			@RequestBody
			SubmodelElement element);
	/**
	 * Add a new model element to the {@link IAssetAdministrationShellRepositoryInterface}. The element must
	 * contain a proper parent element (see {@link Reference}) pointing to it's root container!
	 * @param element The element to add
	 * @return
	 */
	@Operation(summary =  "Update a submodel element at the provided path",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH_WILDCARD)
	public void setSubmodelElement(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier")
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Submodels’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier,
			@Parameter(description = "The path to the container where the new element is to be placed", required = false)
			@PathVariable(name = "path", required = false) 
			String path,
			@RequestBody
			SubmodelElement element);
	/**
	 * Remove an element from the {@link IAssetAdministrationShellRepositoryInterface}. The element must 
	 * either hold the reference to it's parent container or 
	 * be a reference element directly!
	 * @param element 
	 * @return <code>true</code> when deletion successful, false otherwise
	 */
	@Operation(summary =  "Obtain the identifiable element",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH_WILDCARD)
	public boolean removeSubmodelElement(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier")
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier,
			@Parameter(description = "The path to the container")
			@PathVariable("path") 
			String path);

	/**
	 * Obtain the children of the element
	 * @param identifier
	 * @param path
	 * @return
	 */
	@Operation(summary = "Obtain the value of the (Data)Element",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE},
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/value")
	public Object getValue(	
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier")
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier,
			@Parameter(description = "The path to the container")
			@PathVariable("path") 
			String path);
	
	
	@Operation(summary =  "Obtain the identifiable element",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/value")
	public void setValue(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier")
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier,
			@Parameter(description = "The path to the container")
			@PathVariable("path") 
			String path,
			@RequestBody
			Object value);
	/**
	 * Invoke the operation named with the path
	 * @param path
	 * @param parameterMap
	 * @return
	 */
	@Operation(summary =  "Obtain the identifiable element",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			path=PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/invoke")
	public Object invokeOperation(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier")
			String aasIdentifier,
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier")
			String submodelIdentifier,
			@Parameter(description = "The path to the container")
			@PathVariable("path")
			String path,
			@RequestBody
			Object parameterMap);
	
	/**
	 * Obtain the {@link AssetAdministrationShell} 
	 * @return
	 */
	@Operation(summary =  "Get a ConceptDescription",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path=PATH_CONCEPT_DESCRIPTION + CD_IDENTIFIER)
	public ConceptDescription getConceptDescription(
			@Base64Encoded
			@PathParam("cdIdentifier")
			
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The ConceptDescriptions’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("cdIdentifier") 
			String identifier);
	/**
	 * Obtain the {@link AssetAdministrationShell} 
	 * @return
	 */
	@Operation(summary =  "Get a ConceptDescription",
			tags = "Asset Administration Shell Repository Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			path=PATH_CONCEPT_DESCRIPTION + CD_IDENTIFIER)
	public void setConceptDescription(
			@Base64Encoded
			@PathParam("cdIdentifier")
			
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The ConceptDescriptions’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("cdIdentifier") 
			String identifier,
			@RequestBody
			ConceptDescription conceptDescription);

}

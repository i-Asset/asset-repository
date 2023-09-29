package at.srfg.iasset.repository.api;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
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
import jakarta.ws.rs.Path;

@Path("")
@RequestMapping(path = "")
public interface SubmodelRepositoryInterface {
//	final String PATH_AAS_SUBMODELS				= "/aas/submodels";
	final String SUBMODEL_IDENTIFIER			= "/{submodelIdentifier}";
	final String PATH_SUBMODEL_ELEMENTS			= "/submodel/submodel-elements";
	final String IDSHORT_PATH 					= "/{path}";
	final String IDSHORT_PATH_WILDCARD			= "/{path:.+}";
	final String BY_REFERENCE					= "/reference";

	/**
	 * Obtain a {@link Submodel} of the identified {@link AssetAdministrationShell}
	 * 
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 * @return
	 */
	@Operation(summary =  "Obtain the submodel based on it's identifier from the repository",
			tags = "Submodel Repository Interface (Connector)")
	@RequestMapping(
			method = RequestMethod.GET, 
			path = SUBMODEL_IDENTIFIER)
	Submodel getSubmodel(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier") 
			String submodelIdentifier);

	/**
	 * Obtain the submodel {@link Reference} of the {@link AssetAdministrationShell}
	 * @return
	 */
	@Operation(summary =  "Obtain the submodel references from the repository",
			tags = "Submodel Repository Interface (Connector)")
	@RequestMapping(
			method = RequestMethod.GET)
	List<Reference> getSubmodels();

	/**
	 * Obtain an model element based on the reference, may be a {@link Submodel} or
	 * a {@link SubmodelElement}
	 * 
	 * @param element       Reference to the requested element
	 * @return The requested element or <code>null</code>
	 */
	@Operation(summary =  "Obtain a referable element by reference",
			tags = "Submodel Repository Interface (Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			path=BY_REFERENCE)
	Referable getReferableElement(
			@RequestBody
			Reference reference);
	/**
	 * Obtain an model element based on the reference, may be a {@link Submodel} or
	 * a {@link SubmodelElement}
	 * 
	 * @param submodelIdentifier Identifier of the submodel 
	 * @param path               Path pointing to the requested element
	 * @return The requested element or <code>null</code>
	 */
	@Operation(summary =  "Obtain a referable element by refernence",
			tags = "Submodel Repository Interface (Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path=SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH_WILDCARD)
	Referable getSubmodelElement(
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
	 * Obtain the value of a {@link SubmodelElement}
	 * 
	 * @param submodelIdentifier 	The identifier of the submodel 
	 * @param path 					The idShort-path pointing to the {@link SubmodelElement}
	 * @return
	 */
	@Operation(summary = "Obtain the value of the (Data)Element",
			tags = "Submodel Repository Interface (Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE},
			path= SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/value")
	Object getValue(	
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
	 * Obtain the value of a model element based on the reference, may be a {@link Submodel} or
	 * a {@link SubmodelElement}
	 * 
	 * @param element       Reference to the requested element
	 * @return The requested element or <code>null</code>
	 */
	@Operation(summary =  "Obtain the value of a referable element by reference",
			tags = "Submodel Repository Interface (Connector)")
	@RequestMapping(
			method = RequestMethod.POST,
			path=BY_REFERENCE + "/value")
	Object getValueByReference(
			@RequestBody
			Reference reference);	
}
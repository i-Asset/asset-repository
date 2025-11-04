package at.srfg.iasset.repository.api;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelDescriptor;
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
public interface DirectoryInterface {
	/**
	 * Retrieve the {@link AssetAdministrationShellDescriptor} by its identifier
	 * @param aasIdentifier The identifier
	 * @return
	 */
	@Operation(summary = "Obtain the descriptor of an Asset based on it's identifier",
			tags = "Asset Administration Shell Directory Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path="/aas/{aasIdentifier}")
	
	AssetAdministrationShellDescriptor lookup(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) @PathVariable("aasIdentifier") 
			String aasIdentifier);
	
//	@Operation(summary = "Obtain the descriptor of an Asset containing any of the requested semantic identifiers")
//	@RequestMapping(
//			method = RequestMethod.GET,
//			path="/lookup/{supplementalSemanticId}")
//	AssetAdministrationShellDescriptor lookupBySemanticId(
//			@Base64Encoded
//			@Parameter(
//					in = ParameterIn.PATH,
//					description = "A semantic identifer",
//					required = true,
//					schema = @Schema()) @PathVariable("supplementalSemanticId") 
//			String supplementalSemanticId);
	@Operation(summary = "Obtain the descriptor of an Asset containing any of the requested semantic identifiers")
	@RequestMapping(
			method = RequestMethod.POST,
			path="/lookup/{supplementalSemanticId}")
	AssetAdministrationShellDescriptor lookupBySemanticIds(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH,
					description = "A semantic identifer",
					required = true,
					schema = @Schema()) @PathVariable("supplementalSemanticId") 
			String supplementalSemanticId,
			@RequestBody
			List<String> additional);
	
	@Operation(summary = "Obtain the descriptor of an Asset based on it's identifier",
			tags = "Asset Administration Shell Directory Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.GET,
			path="/aas/{aasIdentifier}/submodel/{submodelIdentifier}")
	SubmodelDescriptor lookup(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) @PathVariable("aasIdentifier") 
			String aasIdentifier, 
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) @PathVariable("submodelIdentifier") 
			String submodelIdentifier);
	/**
	 * register new AAS with the directory,
	 * @param shell
	 */
	@Operation(summary = "Regester a new AssetAdministrationShell Descriptor",
			tags = "Asset Administration Shell Directory Interface (for Connector)")
	@RequestMapping(
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes =  MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST,
			path="/aas/{aasIdentifier}")
	AssetAdministrationShell register(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String aasIdentifier,
			
			@Parameter(
					in = ParameterIn.DEFAULT, 
					description = "The Asset Administration Shell Body", 
					required = true, 
					schema = @Schema(implementation = AssetAdministrationShell.class)) 
			@RequestBody 
			AssetAdministrationShellDescriptor shell);
	
	/**
	 * Add a new SubmodelDescriptor to the repository
	 * @param aasIdentifier
	 * @param model
	 * @return The {@link Submodel}
	 */
	@Operation(summary="Add a SubmodelDescriptor to an existing AssetAdministrationShell Descriptor",
			tags = "Asset Administration Shell Directory Interface (for Connector)")
	@RequestMapping(
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes =  MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST,
			path="/aas/{aasIdentifier}/submodel/{submodelIdentifier}")
	Submodel register(
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
					description = "The Submodel's unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("submodelIdentifier") 
			String submodelIdentifier,
			@Parameter(
					in = ParameterIn.DEFAULT, 
					description = "The SubmodelDescriptor to register with the AAS", 
					required = true, 
					schema = @Schema())
			@RequestBody
			SubmodelDescriptor model);
	/**
	 * Remove the registration for the AAS
	 * @param aasIdentifier
	 */
	@Operation(summary = "Remove an AssetAdministrationShell Descriptor",
			tags = "Asset Administration Shell Directory Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/aas/{aasIdentifier}")
	void unregister(
			@Base64Encoded
			@Parameter(
					in = ParameterIn.PATH, 
					description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", 
					required = true, 
					schema = @Schema()) 
			@PathVariable("aasIdentifier") 
			String aasIdentifier);
	/**
	 * Remove all provided submodel's from the {@link AssetAdministrationShell}
	 * @param aasIdentifier
	 * @param submodelIdentifier
	 */
	@Operation(summary="Delete a SubmodelDescriptor",
			tags = "Asset Administration Shell Directory Interface (for Connector)")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path="/aas/{aasIdentifier}/submodel/{submodelIdentifier}")
	void unregister(
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
					description = "The id's of the SubmodelDescriptor to unregister", 
					required = true, 
					schema = @Schema())

			@PathVariable("submodelIdentifier")
			String submodelIdentifier);

}

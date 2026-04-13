package at.srfg.iasset.connector.component.endpoint.controller;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.digitaltwin.aas4j.v3.model.*;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.*;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.eclipse.rdf4j.rio.helpers.JSONLDMode;
import org.eclipse.rdf4j.rio.helpers.JSONLDSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

@Path("")
public class AssetAdministrationRepositoryController implements IAssetAdministrationShellRepositoryInterface {
	private static final Logger log = LoggerFactory.getLogger(AssetAdministrationRepositoryController.class);
	@Context
	private SecurityContext securityContext;
	
	@Context
	private HttpServletRequest request;
	/**
	 * Injected Service Environment
	 */
	@Inject
	private ServiceEnvironment environment;

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER)
	public AssetAdministrationShell getAssetAdministrationShell(
			@PathParam("aasIdentifier") 
			String aasIdentifier) {
		return environment.getAssetAdministrationShell(
					ApiUtils.base64Decode(aasIdentifier)
				).orElse(null);
	}

	@Override
	@PUT
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER)
	public AssetAdministrationShell setAssetAdministrationShell(
			@PathParam("aasIdentifier")
			String aasIdentifier,
			@Valid 
			AssetAdministrationShell assetAdministrationShell) {
		return environment.setAssetAdministrationShell(
					ApiUtils.base64Decode(aasIdentifier),
					assetAdministrationShell)
				;
	}

	@Override
	@PUT
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public Submodel setSubmodel(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@Valid Submodel submodel) {
		return environment.setSubmodel(
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				submodel);
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public Submodel getSubmodel(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier
			) {
		return environment.getSubmodel(
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier)
				)
				.orElse(null);
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS )
	public List<Reference> getSubmodels(
			@PathParam("aasIdentifier")
			String aasIdentifier) {
		return environment.getSubmodelReferences(ApiUtils.base64Decode(aasIdentifier));
	}

	
	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH)
	public Referable getSubmodelElement(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path) {
		return environment.getSubmodelElement(
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier),
					path
				).orElse(null);
	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS)
	public void setSubmodelElement(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier,
			@Valid
			SubmodelElement element) {
		
		environment.setSubmodelElement(			
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				element);

	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH)
	public void setSubmodelElement(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path,
			@Valid
			SubmodelElement element) {
		
		environment.setSubmodelElement(					
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier),
				path, 
				element);

	}

	@Override
	@DELETE
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH)
	public boolean removeSubmodelElement(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path) {
		
		// TODO Auto-generated method stub
		return environment.deleteSubmodelElement(		
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier),
					path);
	}
	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + RDF_VALUE_MODIFIER)
	public Object getRDFValue(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path) {
		// obtain the model
		Model model = environment.getElementRDFValue(					
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path);
		// produce JSON-LD
        StringWriter sw = new StringWriter();
        RDFWriter writer = Rio.createWriter(RDFFormat.JSONLD, sw);
        
//        writer.handleNamespace("schema", "http://schema.org/");
//        writer.handleNamespace("ex", ex);
        // JSON-LD Modus: COMPACT
        writer.getWriterConfig().set(JSONLDSettings.JSONLD_MODE, JSONLDMode.COMPACT);

        // Optional: hübsche Ausgabe und kompakte Arrays
        writer.getWriterConfig().set(BasicWriterSettings.PRETTY_PRINT, true);
        writer.getWriterConfig().set(JSONLDSettings.COMPACT_ARRAYS, true);
    
        
        Rio.write(model, writer);
        return sw.toString();

	}

	@Override
	@PATCH
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + RDF_VALUE_MODIFIER)
	public void setRDFValue(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path,
			InputStream jsonLD) {
		// TODO Auto-generated method stub
		Model model;
		try {
//			Reader stringR = new StringReader(jsonLD);
			model = Rio.parse(jsonLD, RDFFormat.JSONLD);
			environment.setElementRDFValue(ApiUtils.base64Decode(aasIdentifier),
					ApiUtils.base64Decode(submodelIdentifier), path, model);
		} catch (RDFParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedRDFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		throw new Exception("Setting/Parsing JSON-LD failed");
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + VALUE_MODIFIER)
	public Object getValue(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path) {
		return environment.getElementValue(					
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path);
	}

	@Override
	@PATCH
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + VALUE_MODIFIER)
	public void setValue(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path, 
			Object value) {
		environment.setElementValue(					
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier), 
					path, 
					value);

	}
	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + INVOKE)
	public OperationResult invokeOperation(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path,
			OperationRequest parameterMap) {
		try {
            return environment.invokeOperation(
                    ApiUtils.base64Decode(aasIdentifier),
                    ApiUtils.base64Decode(submodelIdentifier),
                    path,
                    parameterMap);
		} catch (Exception e) {
			log.debug("invoke operation failed", e);
			throw new BadRequestException(e);
		}
	}
	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + INVOKE + VALUE_MODIFIER)
	public OperationResultValue invokeOperation(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path,
			OperationRequestValue parameterMap) {
		try {
			return environment.invokeOperationValue(
					ApiUtils.base64Decode(aasIdentifier),
					ApiUtils.base64Decode(submodelIdentifier),
					path,
					parameterMap);
		} catch (Exception e) {
			log.debug("invoke operation value failed", e);
			throw new BadRequestException(e);
		}
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_CONCEPT_DESCRIPTION + CD_IDENTIFIER)
	public ConceptDescription getConceptDescription(
			@PathParam("cdIdentifier")
			String identifier) {
		return environment.getConceptDescription(
					ApiUtils.base64Decode(identifier)
				)
				.orElse(null);
	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_CONCEPT_DESCRIPTION + CD_IDENTIFIER)
	public void setConceptDescription(
			@PathParam("cdIdentifier")
			String identifier, 
			ConceptDescription conceptDescription) {
		environment.setConceptDescription(ApiUtils.base64Decode(identifier), conceptDescription);
	}
	
	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS)
	public List<AssetAdministrationShell> getAssetAdministrationShells() {
		return environment.getAllAssetAdministrationShells();
	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS)
	public List<Reference> setSubmodels(@PathParam("aasIdentifier") String aasIdentifier, List<Reference> submodels) {
		return environment.setSubmodelReferences(ApiUtils.base64Decode(aasIdentifier), submodels);
	}

	@Override
	@DELETE
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public List<Reference> removeSubmodelReference(@PathParam("aasIdentifier") String aasIdentifier, String submodelIdentifier) {
		return environment.deleteSubmodelReference(	
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier)
				);
	}

}

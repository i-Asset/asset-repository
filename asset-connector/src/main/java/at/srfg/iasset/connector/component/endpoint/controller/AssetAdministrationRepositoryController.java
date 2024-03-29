package at.srfg.iasset.connector.component.endpoint.controller;

import java.util.List;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("")
public class AssetAdministrationRepositoryController implements IAssetAdministrationShellRepositoryInterface {
	@Context
	private SecurityContext securityContext;
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
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/value")
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
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/value")
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
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/invoke")
	public OperationResult invokeOperation(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path,
			OperationRequest parameterMap) {
		return environment.invokeOperation(					
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier), 
					path, 
					parameterMap);
	}
	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/invoke/$value")
	public OperationResultValue invokeOperation(
			@PathParam("aasIdentifier")
			String aasIdentifier, 
			@PathParam("submodelIdentifier")
			String submodelIdentifier, 
			@PathParam("path")
			String path,
			OperationRequestValue parameterMap) {
		return environment.invokeOperationValue(					
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier), 
					path, 
					parameterMap);
	}
//	@POST
//	@Produces(value = MediaType.APPLICATION_JSON)
//	@Consumes(value = MediaType.APPLICATION_JSON)
//	@Path(PATH_SHELLS + AAS_IDENTIFIER + PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH + "/invoke")
//	public OperationResult invoke(
//			@PathParam("aasIdentifier")
//			String aasIdentifier, 
//			@PathParam("submodelIdentifier")
//			String submodelIdentifier, 
//			@PathParam("path")
//			String path,
//	OperationRequest parameterMap) {
//		return environment.invokeOperation(aasIdentifier, submodelIdentifier, path, parameterMap);
//	}

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

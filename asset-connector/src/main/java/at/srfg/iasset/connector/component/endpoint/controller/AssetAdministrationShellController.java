package at.srfg.iasset.connector.component.endpoint.controller;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.ModelReference;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.IAssetAdministrationShellInterface;
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
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Providers;

/**
 * Controller for a single Asset Administration Shell
 * @author dglachs
 *
 */
@Path("")
public class AssetAdministrationShellController implements IAssetAdministrationShellInterface {
	@Context
	private SecurityContext securityContext;
	
	@Context
	private Providers providers;
	
	@Inject
	private ServiceEnvironment environment;

	@Inject
	private AssetAdministrationShell theShell;
	
	private ObjectMapper getObjectMapper() {
		ContextResolver<ObjectMapper> resolver = 
		        providers.getContextResolver(ObjectMapper.class, MediaType.WILDCARD_TYPE);
		return resolver.getContext(ObjectMapper.class);

	}
	
	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	public AssetAdministrationShell getAssetAdministrationShell() {
		return theShell;
	}

	@Override
	@PUT
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	public AssetAdministrationShell setAssetAdministrationShell(
			@Valid AssetAdministrationShell body) {
		return environment.setAssetAdministrationShell(theShell.getId(), body);
	}

	@Override
	@PUT
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public Submodel setSubmodel(@PathParam("submodelIdentifier") String submodelIdentifier, @Valid Submodel submodel) {
		return environment.setSubmodel(
				theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				submodel);
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public Submodel getSubmodel(@PathParam("submodelIdentifier") String submodelIdentifier) {
		// TODO Auto-generated method stub
		return environment.getSubmodel(
					theShell.getId(), 
					ApiUtils.base64Decode(submodelIdentifier)
				)
				.orElse(null);
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS)
	public List<ModelReference> getSubmodels() {
		return environment.getSubmodelReferences(theShell.getId());
	}
	
	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS)
	public List<ModelReference> setSubmodels(List<ModelReference> submodels) {
		return environment.setSubmodelReferences(theShell.getId(), submodels);
	}

	@Override
	@DELETE
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public List<ModelReference> removeSubmodelReference(@PathParam("submodelIdentifier") String submodelIdentifier) {
		return environment.deleteSubmodelReference(theShell.getId(),
				ApiUtils.base64Decode(submodelIdentifier));
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH)
	public Referable getSubmodelElement(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path) {
		return environment.getSubmodelElement(theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), path).orElse(null);
	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS)
	public void setSubmodelElement(@PathParam("submodelIdentifier") String submodelIdentifier, SubmodelElement element) {
		environment.setSubmodelElement(
				theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				element);
	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH)
	public void setSubmodelElement(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path, SubmodelElement element) {
		environment.setSubmodelElement(
				theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path,
				element);

	}

	@Override
	@DELETE
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH)
	public boolean removeSubmodelElement(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path) {
		return environment.deleteSubmodelElement(
				theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path);
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH +"/value")
	public Object getValue(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path) {
		return environment.getElementValue(				
				theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path);
	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH +"/value")
	public void setValue(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path, Object value) {
		environment.setElementValue(				
				theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path, 
				value);

	}

	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH +"/invoke")
	public OperationResult invokeOperation(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path,
			OperationRequest parameterMap) {
		return environment.invokeOperation(theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path,
				parameterMap );
	}
	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER + PATH_SUBMODEL_ELEMENTS + IDSHORT_PATH +"/invoke/$value")
	public OperationResultValue invokeOperation(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path,
			OperationRequestValue parameterMap) {
		return environment.invokeOperationValue(theShell.getId(), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path,
				parameterMap );
	}

	@Override
	@GET
	@Produces(value = MediaType.APPLICATION_JSON)
	@Path(PATH_CONCEPT_DESCRIPTION + CD_IDENTIFIER)
	public ConceptDescription getConceptDescription(@PathParam("cdIdentifier") String identifier) {
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
	public void setConceptDescription(@PathParam("cdIdentifier") String identifier, ConceptDescription conceptDescription) {
		environment.setConceptDescription(ApiUtils.base64Decode(identifier), conceptDescription);

	}

}

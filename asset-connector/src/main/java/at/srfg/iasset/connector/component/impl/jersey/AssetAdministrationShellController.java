package at.srfg.iasset.connector.component.impl.jersey;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.IAssetAdministrationShellInterface;
import at.srfg.iasset.repository.component.ServiceEnvironment;

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
	public List<Reference> getSubmodels() {
		return environment.getSubmodelReferences(theShell.getId());
	}
	
	@Override
	@POST
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS)
	public List<Reference> setSubmodels(List<Reference> submodels) {
		return environment.setSubmodelReferences(theShell.getId(), submodels);
	}

	@Override
	@DELETE
	@Produces(value = MediaType.APPLICATION_JSON)
	@Consumes(value = MediaType.APPLICATION_JSON)
	@Path(PATH_AAS_SUBMODELS + SUBMODEL_IDENTIFIER)
	public List<Reference> removeSubmodelReference(@PathParam("submodelIdentifier") String submodelIdentifier) {
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
	public Object invokeOperation(@PathParam("submodelIdentifier") String submodelIdentifier, @PathParam("path") String path,
			Object parameterMap) {
		return environment.invokeOperation(theShell.getId(), 
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

package at.srfg.iasset.repository.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.dataformat.core.util.AasUtils;
import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetInformation;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.SpecificAssetId;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.ShellsApi;
import at.srfg.iasset.repository.api.exception.RepositoryException;
import at.srfg.iasset.repository.api.exception.RepositoryNotFoundException;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.OperationRequest;
import at.srfg.iasset.repository.model.OperationResult;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShellsApiController implements ShellsApi {

	@Autowired
	ServiceEnvironment service;

	private static final Logger log = LoggerFactory.getLogger(ShellsApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	/**
	 * Constructor ...
	 * ObjectMapper must be named "aasMapper" to get the proper configured ObjectMapper injected 
	 * @param aasMapper
	 * @param request
	 */
	@org.springframework.beans.factory.annotation.Autowired
	public ShellsApiController(ObjectMapper aasMapper, HttpServletRequest request) {
		this.objectMapper = aasMapper;
		this.request = request;
	}

	public ResponseEntity<Void> deleteAssetAdministrationShellById(String aasIdentifier) throws RepositoryException {
		// check for the AAS!
		AssetAdministrationShell theShell = getAssetAdministrationShell(aasIdentifier);
		// shell is present, so delete
		service.deleteAssetAdministrationShellById(theShell.getId());
		// return 204 - sucess
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<Void> deleteSubmodelElementByPath(String aasIdentifier, String submodelIdentifier,
			String idShortPath) throws RepositoryException {
		boolean deleted = service.deleteSubmodelElement(
				ApiUtils.base64Decode(aasIdentifier),
				ApiUtils.base64Decode(submodelIdentifier), 
				idShortPath);
		// check the submodel is contained in the shell
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<Void> deleteSubmodelReferenceById(String aasIdentifier, String submodelIdentifier)
			throws RepositoryException {
		Reference ref = new DefaultReference.Builder()
				.key(new DefaultKey.Builder()
						.type(KeyTypes.SUBMODEL)
						.value(ApiUtils.base64Decode(submodelIdentifier)).build())
				.type(ReferenceTypes.GLOBAL_REFERENCE).build();
		// delete
		Reference ref2 = AasUtils.parseReference(submodelIdentifier, DefaultReference.class, DefaultKey.class);
		if ( AasUtils.sameAs(ref, ref2)) {
			// 
			System.out.println("cool");
		}
		service.deleteSubmodelReference(ApiUtils.base64Decode(aasIdentifier), ref);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	public List<AssetAdministrationShell> getAllAssetAdministrationShells(List<SpecificAssetId> assetIds,
			String idShort) {
		return service.getAllAssetAdministrationShells();
	}

	public List<SubmodelElement> getAllSubmodelElements(String aasIdentifier, String submodelIdentifier, String level,
			String content, String extent) throws RepositoryException {
		// decode id's
		String submodelId = ApiUtils.base64Decode(submodelIdentifier);
		// obtain the shell
		AssetAdministrationShell theShell = getAssetAdministrationShell(aasIdentifier);
		Reference ref = new DefaultReference.Builder()
				.key(new DefaultKey.Builder().type(KeyTypes.SUBMODEL).value(submodelId).build())
				.type(ReferenceTypes.GLOBAL_REFERENCE).build();
		// check the shell is contained in AAS
		if (theShell.getSubmodels().contains(ref)) {
			// obtain the shell

		}
		Optional<Submodel> submodel = service.getSubmodel(theShell.getId(), submodelId);
		// obtain the element
		if (submodel.isPresent()) {
			return submodel.get().getSubmodelElements();
		}
		return new ArrayList<SubmodelElement>();
	}

	public List<Reference> getAllSubmodelReferences(String aasIdentifier) throws RepositoryException {
		String id = ApiUtils.base64Decode(aasIdentifier);
		Optional<AssetAdministrationShell> aas = service.getAssetAdministrationShell(id);
		if (!aas.isPresent()) {
			return new ArrayList<Reference>();
		} else {
			return aas.get().getSubmodels();
		}
	}

	public ResponseEntity<AssetAdministrationShell> getAssetAdministrationShellById(
			@Parameter(in = ParameterIn.PATH, description = "The Asset Administration Shell’s unique id (UTF8-BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("aasIdentifier") String aasIdentifier)
			throws RepositoryException {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
//            try {
			byte[] id = Base64Utils.decodeFromString(aasIdentifier);
			String idString = new String(id);
			Optional<AssetAdministrationShell> theShell = service.getAssetAdministrationShell(idString);
			if (theShell.isPresent()) {
				return new ResponseEntity<AssetAdministrationShell>(theShell.get(), HttpStatus.OK);
			} else {
				throw new RepositoryException(HttpStatus.NOT_FOUND, String.format("AAS not found %s", idString));
//            		return new ResponseEntity<AssetAdministrationShell>(HttpStatus.NOT_FOUND);
			}
//            } catch (Exception e) {
//                log.error("Couldn't serialize response for content type application/json", e);
//                return new ResponseEntity<AssetAdministrationShell>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
		}

		return new ResponseEntity<AssetAdministrationShell>(HttpStatus.NOT_IMPLEMENTED);
	}

	public AssetAdministrationShell getAssetAdministrationShell(String identifier) throws RepositoryException {
		//
		String id = ApiUtils.base64Decode(identifier);

		return service.getAssetAdministrationShell(id).orElseThrow(new Supplier<RepositoryException>() {

			@Override
			public RepositoryException get() {
				return new RepositoryNotFoundException(String.format("AAS with id [%s] not found!", id));
			}
		});

	}

	public AssetInformation getAssetInformation(String aasIdentifier) throws RepositoryException {
		String id = ApiUtils.base64Decode(aasIdentifier);
		Optional<AssetAdministrationShell> aas = service.getAssetAdministrationShell(id);
		if (aas.isPresent()) {
			return aas.get().getAssetInformation();
		} else {
			throw new RepositoryNotFoundException(String.format("AAS with id [%s] not found!", id));
		}
	}

	public ResponseEntity<Resource> getFileByPath(String aasIdentifier, String submodelIdentifier, String idShortPath)
			throws RepositoryException {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Resource>(objectMapper.readValue("\"\"", Resource.class),
						HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Resource>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<OperationResult> getOperationAsyncResult(
			String aasIdentifier,
			String submodelIdentifier,
			String idShortPath,
			String handleId,
			String content) throws RepositoryException {
		return new ResponseEntity<OperationResult>(HttpStatus.NOT_IMPLEMENTED);
	}

	public Submodel getSubmodel(String aasIdentifier, String submodelIdentifier, String level, String content, String extent) throws RepositoryException {
		String submodelId = ApiUtils.base64Decode(submodelIdentifier);
		// obtain the shell
		AssetAdministrationShell theShell = getAssetAdministrationShell(aasIdentifier);
		Reference ref = new DefaultReference.Builder()
				.key(new DefaultKey.Builder().type(KeyTypes.SUBMODEL).value(submodelId).build())
				.type(ReferenceTypes.GLOBAL_REFERENCE).build();
		// check the shell is contained in AAS
		if (theShell.getSubmodels().contains(ref)) {
			// obtain the shell

		}
		Optional<Submodel> submodel = service.getSubmodel(theShell.getId(), submodelId);
		// obtain the element
		if (submodel.isPresent()) {
			return submodel.get();
		}
		// TODO:
		return null;
	}

	public SubmodelElement getSubmodelElementByPath(String aasIdentifier, String submodelIdentifier, String idShortPath,
			String level, String content, String extent) throws RepositoryException {
		Optional<SubmodelElement> elem = service.getSubmodelElement(ApiUtils.base64Decode(aasIdentifier),ApiUtils.base64Decode(submodelIdentifier), idShortPath);
		//
		return elem.orElse(null);
	}

	public OperationResult invokeOperation(
			String aasIdentifier,
			String submodelIdentifier,
			String idShortPath,
			OperationRequest body,
			Boolean async,
			String content) throws RepositoryException {
		throw new RepositoryException(HttpStatus.NOT_IMPLEMENTED, "not implemented");
	}

	public AssetAdministrationShell postAssetAdministrationShell(AssetAdministrationShell body) throws RepositoryException {
//		try {
//			return objectMapper.readerFor(AssetAdministrationShell.class).readValue(body);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return AASFull.AAS_1;
		return service.setAssetAdministrationShell(body.getId(), body);
	}

	public ResponseEntity<SubmodelElement> postSubmodelElement(
			String aasIdentifier,
			String submodelIdentifier,
			SubmodelElement body,
			String level,
			String content,
			String extent) throws RepositoryException {
		
		return new ResponseEntity<SubmodelElement>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<SubmodelElement> postSubmodelElementByPath(
			String aasIdentifier,
			String submodelIdentifier,
			String idShortPath,
			SubmodelElement body,
			String level,
			String content,
			String extent) throws RepositoryException {

		return new ResponseEntity<SubmodelElement>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Reference> postSubmodelReference(
			String aasIdentifier,
			Reference body) throws RepositoryException {
		AssetAdministrationShell theShell = getAssetAdministrationShell(aasIdentifier);
		Optional<Submodel> submodel  = service.getSubmodel(theShell.getId(), body.getKeys().get(0).getValue());
		if ( submodel.isPresent()) {
			theShell.getSubmodels().add(body);
			// save
			service.setAssetAdministrationShell(theShell.getId(), theShell);
			return new ResponseEntity<Reference>(body, HttpStatus.CREATED);
		}
		return new ResponseEntity<Reference>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Void> putAssetAdministrationShellById(String aasIdentifier,	AssetAdministrationShell body) throws RepositoryException {
		AssetAdministrationShell theShell = getAssetAdministrationShell(ApiUtils.base64Decode(aasIdentifier));
		service.setAssetAdministrationShell(theShell.getId(), theShell);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<Void> putAssetInformation(
			String aasIdentifier,
			AssetInformation body) throws RepositoryException {
		AssetAdministrationShell theShell = getAssetAdministrationShell(ApiUtils.base64Decode(aasIdentifier));
		theShell.setAssetInformation(body);
		service.setAssetAdministrationShell(theShell.getId(), theShell);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<Void> putFileByPath(
			String aasIdentifier,
			String submodelIdentifier,
			String idShortPath,
			String fileName,
			MultipartFile file) {
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Void> putSubmodel(
			String aasIdentifier,
			String submodelIdentifier,
			Submodel body,
			String level,
			String content,
			String extent) throws RepositoryException {
		AssetAdministrationShell theShell = getAssetAdministrationShell(ApiUtils.base64Decode(aasIdentifier));
		
//		Optional<Submodel> theSubmodel = service.getSubmodel(ApiUtils.base64Decode(submodelIdentifier));
		Reference ref = AasUtils.toReference(body);
		if (! theShell.getSubmodels().contains(ref)) {
			theShell.getSubmodels().add(ref);
			service.setAssetAdministrationShell(theShell.getId(), theShell);
			service.setSubmodel(theShell.getId(),body.getId(),body );
		} else {
			service.setSubmodel(theShell.getId(), body.getId(), body);
		}

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<Void> putSubmodelElementByPath(
			String aasIdentifier,
			String submodelIdentifier,
			String idShortPath,
			SubmodelElement body,
			String level,
			String content,
			String extent) throws RepositoryException {
		service.setSubmodelElement(
				ApiUtils.base64Decode(aasIdentifier),
				ApiUtils.base64Decode(submodelIdentifier),
				idShortPath,
				body
				);


		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}

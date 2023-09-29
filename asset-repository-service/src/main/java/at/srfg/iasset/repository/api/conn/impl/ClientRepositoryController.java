package at.srfg.iasset.repository.api.conn.impl;

import java.util.List;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.Endpoint;
import org.eclipse.digitaltwin.aas4j.v3.model.ModelReference;
import org.eclipse.digitaltwin.aas4j.v3.model.Referable;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.component.DirectoryService;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResult;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "repository")
public class ClientRepositoryController implements IAssetAdministrationShellRepositoryInterface {
	@Autowired
	private ServiceEnvironment server;
	@Autowired
	private DirectoryService directory;
	
	@Override
	public AssetAdministrationShell getAssetAdministrationShell(String identifier) {
		return server.getAssetAdministrationShell(ApiUtils.base64Decode(identifier)).orElse(null);
	}
	
	@Override
	public AssetAdministrationShell setAssetAdministrationShell(String identifier,
			@Valid AssetAdministrationShell assetAdministrationShell) {
		return server.setAssetAdministrationShell(
				ApiUtils.base64Decode(identifier),
				assetAdministrationShell);
	}

	@Override
	public Submodel getSubmodel(String aasIdentifier, String submodelIdentifier) {
		return server.getSubmodel(
					ApiUtils.base64Decode(aasIdentifier), 
					ApiUtils.base64Decode(submodelIdentifier)
				)
				.orElse(null);
	}
	
	@Override
	public Submodel setSubmodel(String aasIdentifier, String submodelIdentifier, @Valid Submodel submodel) {
		
		return server.setSubmodel(
				ApiUtils.base64Decode(aasIdentifier),
				ApiUtils.base64Decode(submodelIdentifier),
				submodel);
	}

	@Override
	public List<ModelReference> getSubmodels(String aasIdentifier) {
		
		return server.getSubmodelReferences(ApiUtils.base64Decode(aasIdentifier));
	}
	
	@Override
	public Referable getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		return server.getSubmodelElement(
				ApiUtils.base64Decode(aasIdentifier),
				ApiUtils.base64Decode(submodelIdentifier),
				path)
				.orElse(null);
	}

	@Override
	public void setSubmodelElement(String aasIdentifier, String submodelIdentifier, 
			SubmodelElement element) {
		
		server.setSubmodelElement(
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				null, 
				element);
	}


	@Override
	public void setSubmodelElement(String aasIdentifier, String submodelIdentifier, String path,
			SubmodelElement element) {
		
		server.setSubmodelElement(
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path, 
				element);
	}

	@Override
	public boolean removeSubmodelElement(String aasIdentifier, String submodelIdentifier, String path) {
		return server.deleteSubmodelElement(
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path);
	}

	@Override
	public Object getValue(String aasIdentifier, String submodelIdentifier, String path) {
		return server.getElementValue(ApiUtils.base64Decode(aasIdentifier), ApiUtils.base64Decode(submodelIdentifier), path);
	}

	@Override
	public void setValue(String aasIdentifier, String submodelIdentifier, String path, Object value) {
		server.setElementValue(
				ApiUtils.base64Decode(aasIdentifier), 
				ApiUtils.base64Decode(submodelIdentifier), 
				path, 
				value);


	}

	@Override
	public OperationResult invokeOperation(String aasIdentifier, String submodelIdentifier, String path, 
			OperationRequest parameterMap) {
		// 
		Optional<AssetAdministrationShellDescriptor> descriptor = directory.getShellDescriptor(aasIdentifier);
		if (descriptor.isPresent()) {
			// 
			Optional<Endpoint> endpoint = descriptor.get().getEndpoints().stream().findFirst();
			if ( endpoint.isPresent() ) {
				ConnectionProvider conn = ConnectionProvider.getConnection(endpoint.get().getAddress());
				// execute the operation with the endpoint!
				return conn.getRepositoryInterface().invokeOperation(aasIdentifier, submodelIdentifier, path, parameterMap);
			}
		}
		// TODO: add error reporting
		return null;
	}
	@Override
	public OperationResultValue invokeOperation(String aasIdentifier, String submodelIdentifier, String path, 
			OperationRequestValue parameterMap) {
		// 
		Optional<AssetAdministrationShellDescriptor> descriptor = directory.getShellDescriptor(aasIdentifier);
		if (descriptor.isPresent()) {
			// 
			Optional<Endpoint> endpoint = descriptor.get().getEndpoints().stream().findFirst();
			if ( endpoint.isPresent() ) {
				ConnectionProvider conn = ConnectionProvider.getConnection(endpoint.get().getAddress());
				// execute the operation with the endpoint!
				return conn.getRepositoryInterface().invokeOperation(aasIdentifier, submodelIdentifier, path, parameterMap);
			}
		}
		// TODO: add error reporting
		return null;
	}
	@Override
	public ConceptDescription getConceptDescription(String identifier) {
		return server.getConceptDescription(ApiUtils.base64Decode(identifier)).orElse(null);
	}
	@Override
	public void setConceptDescription(String identifier, ConceptDescription conceptDescription) {
		server.setConceptDescription(ApiUtils.base64Decode(identifier), conceptDescription);
	}

	@Override
	public List<AssetAdministrationShell> getAssetAdministrationShells() {
		return server.getAllAssetAdministrationShells();
	}

	@Override
	public List<ModelReference> setSubmodels(String aasIdentifier, List<ModelReference> submodels) {
		return server.setSubmodelReferences(aasIdentifier, submodels);
	}

	@Override
	public List<ModelReference> removeSubmodelReference(String aasIdentifier, String submodelIdentifier) {
		return server.deleteSubmodelReference(aasIdentifier, submodelIdentifier);
	}



}

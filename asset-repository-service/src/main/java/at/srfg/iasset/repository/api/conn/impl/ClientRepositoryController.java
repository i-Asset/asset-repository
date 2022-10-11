package at.srfg.iasset.repository.api.conn.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.ConceptDescription;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.api.IAssetConnection;
import at.srfg.iasset.repository.component.ServiceEnvironment;

@RestController
@RequestMapping(path = "repository")
public class ClientRepositoryController implements IAssetAdministrationShellRepositoryInterface {
	@Autowired
	private ServiceEnvironment server;

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
	public List<Reference> getSubmodels(String aasIdentifier) {
		
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
	public Map<String, Object> invokeOperation(String aasIdentifier, String submodelIdentifier, String path, 
			Map<String, Object> parameterMap) {
		return new HashMap<String, Object>();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> setSubmodels(String aasIdentifier, List<Reference> submodels) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reference> removeSubmodelReference(String aasIdentifier, String submodelIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}



}

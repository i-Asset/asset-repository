package at.srfg.iasset.repository.api.conn.impl;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.IAssetDirectory;
import at.srfg.iasset.repository.component.DirectoryService;

@RestController
@RequestMapping(path = "directory")
public class DirectoryController implements IAssetDirectory {
	@Autowired
	private DirectoryService service;


	@Override
	public void unregister(String aasIdentifier) {
		service.removeShellDescriptor(ApiUtils.base64Decode(aasIdentifier));
	}

	@Override
	public void unregister(String aasIdentifier, String submodelIdentifier) {
		service.removeSubmodelDescriptors(
				ApiUtils.base64Decode(aasIdentifier),
				ApiUtils.base64Decode(submodelIdentifier));
				
	}

	@Override
	public AssetAdministrationShellDescriptor lookup(String aasIdentifier) {
		return service.getShellDescriptor(ApiUtils.base64Decode(aasIdentifier)).orElse(null);
	}

	@Override
	public SubmodelDescriptor lookup(String aasIdentifier, String submodelIdentifier) {
		return service.getSubmodelDescriptor(
				ApiUtils.base64Decode(aasIdentifier),
				ApiUtils.base64Decode(submodelIdentifier)).orElse(null);
	}

	@Override
	public AssetAdministrationShell register(String aasIdentifier, AssetAdministrationShellDescriptor shell) {
		return service.registerShellDescriptor(
				ApiUtils.base64Decode(aasIdentifier),
				shell);
	}

	@Override
	public Submodel register(String aasIdentifier, String submodelIdentifier,  SubmodelDescriptor model) {
		return service.registerSubmodelDescriptor(
				ApiUtils.base64Decode(aasIdentifier),
				ApiUtils.base64Decode(submodelIdentifier),
				model);
	}

}

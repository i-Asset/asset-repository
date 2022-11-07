package at.srfg.iasset.repository.api.conn.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.api.SubmodelRepositoryInterface;
import at.srfg.iasset.repository.component.ServiceEnvironment;

@RestController
@RequestMapping(path = "subrepo")
public class SubmodelRepositoryController implements SubmodelRepositoryInterface {
	@Autowired
	private ServiceEnvironment server;

	@Override
	public Submodel getSubmodel(String submodelIdentifier) {
		return server.getSubmodel(
				ApiUtils.base64Decode(submodelIdentifier))
				.orElse(null);
	}

	@Override
	public List<Reference> getSubmodels() {
		return new ArrayList<Reference>();
	}

	@Override
	public Referable getSubmodelElement(String submodelIdentifier, String path) {
		return server.getSubmodelElement(
				ApiUtils.base64Decode(submodelIdentifier),
				path)
				.orElse(null);
	}

	@Override
	public Object getValue(String submodelIdentifier, String path) {
		return server.getElementValue(
				ApiUtils.base64Decode(submodelIdentifier),
				path);
	}

}

package at.srfg.iasset.connector.component.impl;

import java.net.URI;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.connector.component.impl.conn.RepositoryRestConnector;

public interface RepositoryConnection {
	void register(AssetAdministrationShell shellToRegister, URI uri);
	void unregister(String aasIdentifier);
	
	void save(AssetAdministrationShell shell);
	
	AssetAdministrationShell getAssetAdministrationShell(String aasIdentifier);
	
	Submodel getSubmodel(String aasIdentifier, String submodelIdentifier);
	
	<T extends SubmodelElement> T getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path, Class<T> elementClass);
	
	static RepositoryConnection getConnector(URI uri) {
		return new RepositoryRestConnector(uri);
	}
}

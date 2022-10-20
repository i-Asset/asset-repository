package at.srfg.iasset.connector.component.impl;

import java.net.URI;
import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;

import at.srfg.iasset.connector.component.impl.conn.RepositoryRestConnector;

public interface RepositoryConnection {
	/**
	 * register the provided {@link AssetAdministrationShell} with the Asset Directory.
	 * @param shellToRegister The shell to register
	 * @param uri The service endpoint for this particular {@link AssetAdministrationShell}
	 * @return <code>true</code> when successfully registered, <code>false</code> otherwise
	 */
	boolean register(AssetAdministrationShell shellToRegister, URI uri);
	/**
	 * Unregister the {@link AssetAdministrationShell} from the Asset Directory.
	 * @param aasIdentifier
	 * @return <code>true</code> when successfully unregistered, <code>false</code> otherwise
	 */
	boolean unregister(String aasIdentifier);
	/**
	 * Store the provided {@link AssetAdministrationShell} with the Asset Repository
	 * @param shell
	 */
	AssetAdministrationShell save(AssetAdministrationShell shell);
	/**
	 * Obtain the {@link AssetAdministrationShell} based on it's identifier
	 * @param aasIdentifier the unique identifier of the {@link AssetAdministrationShell}
	 * @return The aas object or null when not found
	 */
	Optional<AssetAdministrationShell> getAssetAdministrationShell(String aasIdentifier);
	
	Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier);
	
	<T extends SubmodelElement> Optional<T> getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path, Class<T> elementClass);
	
	static RepositoryConnection getConnector(URI uri) {
		return new RepositoryRestConnector(uri);
	}
}

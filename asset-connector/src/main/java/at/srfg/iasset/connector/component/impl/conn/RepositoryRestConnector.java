package at.srfg.iasset.connector.component.impl.conn;

import java.net.URI;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.impl.DefaultAssetAdministrationShellDescriptor;
import org.eclipse.aas4j.v3.model.impl.DefaultEndpoint;

import at.srfg.iasset.connector.component.impl.RepositoryConnection;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.api.IAssetDirectory;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;

public class RepositoryRestConnector implements RepositoryConnection {
	
	private final ConnectionProvider connection;
	
	public RepositoryRestConnector(URI repositoryURI) {
		this.connection = ConnectionProvider.getConnection(repositoryURI);
	}
	
	private IAssetAdministrationShellRepositoryInterface getRepositoryService() {
		return connection.getRepositoryInterface();
	}
	private IAssetDirectory getDirectoryService() {
		return connection.getIAssetDirectory();
	}

	@Override
	public void save(AssetAdministrationShell shell) {
		getRepositoryService().setAssetAdministrationShell(shell.getId(), shell);

	}

	@Override
	public AssetAdministrationShell getAssetAdministrationShell(String aasIdentifier) {
		return getRepositoryService().getAssetAdministrationShell(aasIdentifier);
	}

	@Override
	public Submodel getSubmodel(String aasIdentifier, String submodelIdentifier) {
		return getRepositoryService().getSubmodel(aasIdentifier, submodelIdentifier);
	}

	@Override
	public <T extends SubmodelElement> T getSubmodelElement(String aasIdentifier, String submodelIdentifier, String path,
			Class<T> elementClass) {
		Referable result = getRepositoryService().getSubmodelElement(aasIdentifier, submodelIdentifier, path);
		if ( result != null && elementClass.isInstance(result)) {
			return elementClass.cast(result);
		}
		return null;
	}

	@Override
	public void register(AssetAdministrationShell theShell, URI uri) {

		AssetAdministrationShellDescriptor descriptor = new DefaultAssetAdministrationShellDescriptor.Builder()
				.id(theShell.getId())
				.displayNames(theShell.getDisplayNames())
				.descriptions(theShell.getDescriptions())
				.endpoint(new DefaultEndpoint.Builder()
						.address(uri.toString())
						.type("shell")
						.build())
				.build();
		//
		getDirectoryService().register(theShell.getId(), descriptor);
		
	}

	@Override
	public void unregister(String aasIdentifier) {
		getDirectoryService().unregister(aasIdentifier);
		
	}

}

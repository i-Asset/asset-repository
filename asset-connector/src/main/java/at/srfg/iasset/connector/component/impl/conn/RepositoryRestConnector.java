package at.srfg.iasset.connector.component.impl.conn;

import java.net.URI;
import java.util.Optional;

import javax.ws.rs.ServiceUnavailableException;

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
	public AssetAdministrationShell save(AssetAdministrationShell shell) {
		try {
			return getRepositoryService().setAssetAdministrationShell(shell.getId(), shell);
		} catch (ServiceUnavailableException e) {
			return null;
		}

	}

	@Override
	public Optional<AssetAdministrationShell> getAssetAdministrationShell(String aasIdentifier) {
		AssetAdministrationShell theShell  =getRepositoryService().getAssetAdministrationShell(aasIdentifier);
		return Optional.ofNullable(theShell);
	}

	@Override
	public Optional<Submodel> getSubmodel(String aasIdentifier, String submodelIdentifier) {
		Submodel theSubmodel = getRepositoryService().getSubmodel(aasIdentifier, submodelIdentifier);
		return Optional.ofNullable(theSubmodel);
	}

	@Override
	public <T extends SubmodelElement> Optional<T> getSubmodelElement(String aasIdentifier, String submodelIdentifier,
			String path, Class<T> elementClass) {
		try {
			Referable result = getRepositoryService().getSubmodelElement(aasIdentifier, submodelIdentifier, path);
			if (result != null && elementClass.isInstance(result)) {
				return Optional.of(elementClass.cast(result));
			}
			return Optional.empty();
				
		} catch (ServiceUnavailableException e) {
			// Improve Exception Handling
			return null;
		}
	}

	@Override
	public boolean register(AssetAdministrationShell theShell, URI uri) {

		AssetAdministrationShellDescriptor descriptor = new DefaultAssetAdministrationShellDescriptor.Builder()
				.id(theShell.getId()).displayNames(theShell.getDisplayNames()).descriptions(theShell.getDescriptions())
				.endpoint(new DefaultEndpoint.Builder().address(uri.toString()).type("shell").build()).build();
		//
		try {
			getDirectoryService().register(theShell.getId(), descriptor);
			return true;
		} catch (ServiceUnavailableException e) {
			return false;
		}

	}

	@Override
	public boolean unregister(String aasIdentifier) {
		try {
			getDirectoryService().unregister(aasIdentifier);
			return true;
		} catch (ServiceUnavailableException e) {
			return false;
		}

	}

}

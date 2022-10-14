package at.srfg.iasset.repository.connectivity;

import java.net.URI;
import java.net.URL;

import at.srfg.iasset.repository.api.IAssetAdministrationShellInterface;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.api.IAssetConnection;
import at.srfg.iasset.repository.api.IAssetDirectory;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.connectivity.rest.ConsumerFactory;

public interface ConnectionProvider {
	static ConnectionProvider getConnection(String host) {
		if (!host.endsWith("/")) {
			host += "/";
		}
		return new Connection(host);
	}
	static Connection getConnection(URI uri) {
		return new Connection(uri.toString());
	}
	static Connection getConnection(URL url) {
		return new Connection(url.toString());
	}
	
//	IAssetConnection getIAssetConnection();
	IAssetAdministrationShellRepositoryInterface getRepositoryInterface();
	IAssetAdministrationShellInterface getShellInterface();
	
	IAssetDirectory getIAssetDirectory();

	class Connection implements ConnectionProvider {
		private IAssetAdministrationShellRepositoryInterface repositoryInterface;
		private IAssetDirectory directoryInterface;
		final String host;
		private Connection(String host) {
			this.host = host;
		}
		@Override
		public IAssetAdministrationShellRepositoryInterface getRepositoryInterface() {
			if ( repositoryInterface == null) {
				repositoryInterface = ConsumerFactory.createConsumer(
						// construct the URL
						host + "repository",
						// the Client Factory creates a client configured with the AAS Model (default implementations & mixins)
						ClientFactory.getInstance().getClient(), 
						// the interface class
						IAssetAdministrationShellRepositoryInterface.class);	
			}
			return repositoryInterface;
		}
		@Override
		public IAssetAdministrationShellInterface getShellInterface() {
				return ConsumerFactory.createConsumer(
						// construct the URL
						host + "repository",
						// the Client Factory creates a client configured with the AAS Model (default implementations & mixins)
						ClientFactory.getInstance().getClient(), 
						// the interface class
						IAssetAdministrationShellInterface.class);	
		}
		@Override
		public IAssetDirectory getIAssetDirectory() {
			if ( directoryInterface == null) {
				directoryInterface = ConsumerFactory.createConsumer(
						// construct the URL
						host + "directory",
						// the Client Factory creates a client configured with the AAS Model (default implementations & mixins)
						ClientFactory.getInstance().getClient(), 
						// the interface class
						IAssetDirectory.class);	
			}
			return directoryInterface;
		}
		
	}
}


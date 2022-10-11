package at.srfg.iasset.repository.connectivity;

import java.net.URL;

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
	static Connection getConnection(URL url) {
		return new Connection(url.toString());
	}
	
	IAssetConnection getIAssetConnection();
	
	IAssetDirectory getIAssetDirectory();

	class Connection implements ConnectionProvider {
		
		final String host;
		private Connection(String host) {
			this.host = host;
		}
		@Override
		public IAssetConnection getIAssetConnection() {
				return ConsumerFactory.createConsumer(
						// construct the URL
						host + "repository",
						// the Client Factory creates a client configured with the AAS Model (default implementations & mixins)
						ClientFactory.getInstance().getClient(), 
						// the interface class
						IAssetConnection.class);	
		}
		@Override
		public IAssetDirectory getIAssetDirectory() {
			return ConsumerFactory.createConsumer(
					// construct the URL
					host + "directory",
					// the Client Factory creates a client configured with the AAS Model (default implementations & mixins)
					ClientFactory.getInstance().getClient(), 
					// the interface class
					IAssetDirectory.class);	
		}
		
	}
}


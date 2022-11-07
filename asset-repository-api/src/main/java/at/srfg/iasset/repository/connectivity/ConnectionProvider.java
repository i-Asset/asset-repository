package at.srfg.iasset.repository.connectivity;

import java.net.URI;
import java.net.URL;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.Submodel;

import at.srfg.iasset.repository.api.IAssetAdministrationShellInterface;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.api.IAssetDirectory;
import at.srfg.iasset.repository.api.SubmodelRepositoryInterface;
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
	
	/**
	 * Obtain a service interface connected to the Asset Administration Shell Repository Interface!
	 * Each of the methods in the interface requires the {@link AssetAdministrationShell}'s identifier as a 
	 * first parameter.
	 *  
	 * <p>
	 * The final service path is combine with the provided <code>host address</code> and the suffix <code>/repository</code>
	 * </p>
	 * 
	 * @return A proxy-object connected with the r
	 */
	IAssetAdministrationShellRepositoryInterface getRepositoryInterface();
	/**
	 * Obtain a service interface connected to a Asset Administration Shell Interface. 
	 * Opposite to the {@link ConnectionProvider#getRepositoryInterface()} service interface, the 
	 * endpoint already identifies the {@link AssetAdministrationShell} to use!
	 * Each of the methods in the interface requires the {@link AssetAdministrationShell}'s identifier as a 
	 * first parameter. 
	 * <p>
	 * The final service path is combine with the provided <code>host address</code>!
	 * </p>
	 * 
	 * @return A proxy-object connected with the r
	 */
	IAssetAdministrationShellInterface getShellInterface();
	/**
	 * Interface provided by the repository allowing direct access to {@link Submodel} data!
	 * @return
	 */
	SubmodelRepositoryInterface getSubmodelInterface();
	/**
	 * Obtain a service interface connected with the central repository service
	 * @return
	 */
	IAssetDirectory getIAssetDirectory();

	class Connection implements ConnectionProvider {
		private IAssetAdministrationShellRepositoryInterface repositoryInterface;
		private IAssetDirectory directoryInterface;
		private SubmodelRepositoryInterface submodelRepository;
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
						host,
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
		@Override
		public SubmodelRepositoryInterface getSubmodelInterface() {
			if ( submodelRepository == null) {
				submodelRepository = ConsumerFactory.createConsumer(
						// construct the URL
						host + "subrepo",
						// the Client Factory creates a client configured with the AAS Model (default implementations & mixins)
						ClientFactory.getInstance().getClient(), 
						// the interface class
						SubmodelRepositoryInterface.class);	
			}
			return submodelRepository;
		}
		
	}
}


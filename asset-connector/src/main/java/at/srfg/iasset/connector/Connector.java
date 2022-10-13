package at.srfg.iasset.connector;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import at.srfg.iasset.connector.component.impl.HttpComponent;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;

public class Connector {
	

	/**
	 * The URL to the Server
	 */
	private final URI repositoryURL; 
	/**
	 * 
	 */
	private final ConnectionProvider connectionProvider;
	/**
	 * The local service port where the endpoint is created
	 */
	private int localServicePort = 5050;
	
	private HttpComponent endpoint;
	
	private ServiceEnvironment serviceEnvironment = new LocalServiceEnvironment();
	
	public Connector(URI repositoryURL) {
		
		this.repositoryURL = repositoryURL;
		this.connectionProvider = ConnectionProvider.getConnection(this.repositoryURL);
	}
	/**
	 * Enable the communication endpoints
	 * @return
	 */
	public Connector start() {
		return start(localServicePort);
	}
	public Connector start(int port) {
		if ( this.endpoint == null ) {
			endpoint = new HttpComponent(repositoryURL, serviceEnvironment);
		}
		if ( ! endpoint.isStarted()) {
			endpoint.start();
		}
		return this;
		
	}
	
	public void stop() {
		if ( endpoint.isStarted()) {
			endpoint.stop();
		}
	}
	
	public static void main(String [] args) {
		try {
			
			Connector connector = new Connector( new URI("http://localhost:8080/"));
			connector.start();
			connector.aliasForShell("test", "https://acplt.org/Test_AssetAdministrationShell");
			connector.register("https://acplt.org/Test_AssetAdministrationShell");
			System.in.read();
			connector.stop();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void aliasForShell(String alias, String aasIdentifier) {
		
		endpoint.addShellHandler(alias, aasIdentifier);
	}
	public void register(String aasIdentifier) {
		endpoint.register(aasIdentifier);
	}
	public void unregister(String aasIdentifier) {
		endpoint.unregister(aasIdentifier);
	}

}

package at.srfg.iasset.connector;

import java.net.MalformedURLException;
import java.net.URL;

import at.srfg.iasset.connector.component.impl.HttpComponent;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.ConnectionProvider;

public class Connector {
	

	/**
	 * The URL to the Server
	 */
	private final URL repositoryURL; 
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
	
	public Connector(URL repositoryURL) {
		
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
		endpoint = new HttpComponent(port, serviceEnvironment);
		endpoint.start();
		return this;
		
	}
	
	public void stop() {
		endpoint.stop();
	}
	
	public static void main(String [] args) {
		try {
			
			Connector connector = new Connector( new URL("http", "localhost", 8080, "/"));
			connector.start();
			connector.aliasForShell("test", "https://acplt.org/Test_AssetAdministrationShell");
			connector.stop();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void aliasForShell(String alias, String aasIdentifier) {
		
		endpoint.addShellHandler(alias, aasIdentifier);
	}

}

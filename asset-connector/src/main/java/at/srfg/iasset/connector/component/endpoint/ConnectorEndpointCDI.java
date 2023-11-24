package at.srfg.iasset.connector.component.endpoint;

import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.digitaltwin.aas4j.v3.model.Endpoint;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEndpoint;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultProtocolInformation;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.endpoint.config.AliasConfig;
import at.srfg.iasset.connector.component.endpoint.config.ShellsConfig;
import at.srfg.iasset.repository.api.ApiUtils;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConnectorEndpointCDI implements ConnectorEndpoint {

	private HttpServer httpServer;
	private static final int defaultPort = 5050;
	private int port = defaultPort;
	@Inject
	ServiceEnvironment environment;
	private URI endpointAddress;
	@Inject
	private EndpointSettings settings;
	/**
	 * List of the dedicated HttpHandlers 
	 */
	private final Map<String, HttpHandler> httpHandler = new HashMap<String, HttpHandler>();

	@PreDestroy
	protected void shutdownEndpoint() {
		stop();
	}

	public void start(int port, String contextRoot) {
		this.port = port;
		ResourceConfig config = new ShellsConfig(environment);
		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, config);

		httpServer = GrizzlyHttpServerFactory.createHttpServer(
				// always create with localhost
				URI.create(String.format("http://%s:%s/%s", "0.0.0.0", this.port, contextRoot))
			);
		
//		serverConfiguration = httpServer.getServerConfiguration();
		httpServer.getServerConfiguration().addHttpHandler(handler, contextRoot);
		
		try {
			String host = InetAddress.getLocalHost().getHostAddress();
			endpointAddress = URI.create(String.format("http://%s:%s%s", host, this.port, contextRoot));
			httpServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public URI getServerAddress(String host, int port, String context) {
		httpServer.getServerConfiguration().getHttpServerName();
		return URI.create(String.format("http://%s:%s/%s", host, port, context));
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public void stop() {
		if (httpServer != null && httpServer.isStarted()) {
			try {
				httpServer.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
//				httpServer.destroy();
				httpServer = null;
			}
		}
		
	}

	@Override
	public boolean isStarted() {
		return httpServer != null && httpServer.isStarted();
	}

	@Override
	public void addHttpHandler(String alias, ResourceConfig config) {
		// TODO Auto-generated method stub
		
	}
	public void startAlias(String alias, AssetAdministrationShell shell) {
		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, new AliasConfig(environment, shell));
		httpServer.getServerConfiguration().addHttpHandler(handler, "/" + alias);
		httpHandler.put(alias, handler);
		
	}
	public boolean stopAlias(String alias) {
		HttpHandler handler = httpHandler.get(alias);
		if (handler != null) {
			return httpServer.getServerConfiguration().removeHttpHandler(handler);
		}
		return false;
	}
	@Override
	public boolean removeHttpHandler(String alias) {
		return stopAlias(alias);
	}
	@Override
	public void start() {
		start(settings.getPort(), settings.getContext());
		
	}
	@Override
	public void start(int port) {
		start(port, settings.getContext());
		
	}

	@Override
	public URI getServiceAddress() {
		if ( endpointAddress == null) {
			throw new IllegalStateException("Adress not available, please start endpoint first!");
		}
		return endpointAddress;
	}
	
    @Override
	public Endpoint getEndpoint() {
    	Endpoint ep = new DefaultEndpoint.Builder()
    			.endpointInterface("AAS-REPOSITORY-3.0_ITWIN")
    			.protocolInformation(new DefaultProtocolInformation.Builder()
    					.href(String.format("%s", 
    							endpointAddress.toString()))
    					.build())
    			.build()
    			;
    	return ep;
	}	
    @Override
	public Endpoint getEndpoint(String aasIdentifier) {
		Optional<AssetAdministrationShell> shell = environment.getAssetAdministrationShell(aasIdentifier);
		if ( shell.isPresent()) {
			Endpoint ep = new DefaultEndpoint.Builder()
					.endpointInterface("AAS-3.0_ITWIN")		
					.protocolInformation(new DefaultProtocolInformation.Builder()
							.href(String.format("%sshells/%s", 
									endpointAddress.toString(), 
									ApiUtils.base64Encode(aasIdentifier)))
							.build())
					.build()
					;
			return ep;
		}
		return null;
	}
    public Endpoint getEndpoint(String aasIdentifier, String submodelIdentifier) {
    	Optional<Submodel> sub = environment.getSubmodel(aasIdentifier, submodelIdentifier);
    	if ( sub.isPresent()) {
    		Endpoint ep = new DefaultEndpoint.Builder()
    				.endpointInterface("SUBMODEL-3.0_ITWIN")
    				.protocolInformation(new DefaultProtocolInformation.Builder()
    						.href(String.format("%s/shells/%s/submodels/%s", 
    								endpointAddress.toString(), 
    								ApiUtils.base64Encode(aasIdentifier),
    								ApiUtils.base64Encode(submodelIdentifier)))
    						.build())
    				.build()
    				;
    		return ep;
    	}
		return null;
    	
    }
	

}

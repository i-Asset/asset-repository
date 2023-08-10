package at.srfg.iasset.connector.component.endpoint;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.endpoint.config.AliasConfig;
import at.srfg.iasset.connector.component.endpoint.config.ShellsConfig;
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

	@Override
	public void start(String contextPath, ResourceConfig rootConfig) {
		
		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, new ShellsConfig(environment));

		httpServer = GrizzlyHttpServerFactory.createHttpServer(
				// always create with localhost
				URI.create(String.format("http://%s:%s/%s", "0.0.0.0", defaultPort, contextPath))
			);
		
//		serverConfiguration = httpServer.getServerConfiguration();
		httpServer.getServerConfiguration().addHttpHandler(handler, contextPath);
		

		
		try {
			httpServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void start(int port, String contextRoot) {
		this.port = port;
		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, new ShellsConfig(environment));

		httpServer = GrizzlyHttpServerFactory.createHttpServer(
				// always create with localhost
				URI.create(String.format("http://%s:%s/%s", "0.0.0.0", this.port, contextRoot))
			);
		
//		serverConfiguration = httpServer.getServerConfiguration();
		httpServer.getServerConfiguration().addHttpHandler(handler, contextRoot);
		

		
		try {
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


}

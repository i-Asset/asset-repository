package at.srfg.iasset.connector.component.impl;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import at.srfg.iasset.connector.component.Component;
import at.srfg.iasset.connector.component.config.MarshallingFeature;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationRepositoryController;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationShellController;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;

public class HttpComponent implements Component {
	private HttpServer httpServer;
	private ServerConfiguration serverConfiguration;
	private int port;
	private String contextPath;
	/**
	 * The local service environment
	 */
	private final ServiceEnvironment environment;
	
	private final Map<String, HttpHandler> httpHandler = new HashMap<String, HttpHandler>();
	
	private final JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
	/**
	 * Specify the service endpoint where the component's http interface will be exposed
	 * @param port
	 * @param environment
	 */
	public HttpComponent(int port, ServiceEnvironment environment) {
		this(port, environment, "");
		
	}
	public HttpComponent(int port, ServiceEnvironment environment, String contextPath) {
		this.port = port;
		this.contextPath = contextPath;
		this.environment = environment;
		this.provider.setMapper(ClientFactory.getObjectMapper());
		
	}
	public String getHostName() {
		try {
			InetAddress adr = InetAddress.getLocalHost();
			return adr.getHostName();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}
	public URI getServerAddress(String context) {
		return URI.create(String.format("http://%s:%s/%s", getHostName(), port, contextPath));
	}

	@Override
	public void start() {
		//
		
		httpServer = GrizzlyHttpServerFactory.createHttpServer(getServerAddress(contextPath));
		serverConfiguration = httpServer.getServerConfiguration();
		
		// create root handler handler with resource config
		ResourceConfig config = new ResourceConfig();
		config.register(MarshallingFeature.class);
		config.register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(environment).to(ServiceEnvironment.class);
				
			}
		});
		config.register(AssetAdministrationRepositoryController.class);

		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, config);		
		serverConfiguration.addHttpHandler(handler, "/");

//		server = new Server(port);
//		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
//		handler.setContextPath("/");
//		server.setHandler(handler);
//		
//		// option - register AAS from the environment with the repository
//		
//		ServletHolder holder = new ServletHolder(new ServletContainer(resourceConfig));
//		holder.setInitOrder(0);
//		handler.addServlet(holder, "/*");
		
		
		// TODO Auto-generated method stub
		try {
			httpServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		if (httpServer != null && httpServer.isStarted() ) {
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
	public void addShellHandler(String alias, String aasIdentifier) {
		Optional<AssetAdministrationShell> aas = environment.getAssetAdministrationShell(aasIdentifier);
		if (aas.isPresent()) {
			ResourceConfig config = new ResourceConfig();
			
			config.register(MarshallingFeature.class);
			config.register(new AbstractBinder() {
				
				@Override
				protected void configure() {
					bind(environment).to(ServiceEnvironment.class);
					
				}
			});
			config.register(new AbstractBinder() {
				
				@Override
				protected void configure() {
					bind(aas.get()).to(AssetAdministrationShell.class);
					
				}
			});
			config.register(AssetAdministrationShellController.class);
			
			
			GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, config);		
			serverConfiguration.addHttpHandler(handler, "/"+alias);
			httpHandler.put(alias, handler);
			
		}
		
		

	}
	@Override
	public boolean removeShellHandler(String alias) {
		HttpHandler handler = httpHandler.get(alias);
		if (handler!=null) {
			return serverConfiguration.removeHttpHandler(handler);
		}
		return false;
	}
}

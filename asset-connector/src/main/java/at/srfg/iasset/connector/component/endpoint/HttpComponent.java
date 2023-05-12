package at.srfg.iasset.connector.component.endpoint;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.config.MarshallingFeature;
import at.srfg.iasset.connector.component.endpoint.controller.AssetAdministrationRepositoryController;
import at.srfg.iasset.connector.component.endpoint.controller.AssetAdministrationShellController;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASJacksonMapperProvider;
import jakarta.ws.rs.ProcessingException;
/**
 * Helper Class managing the HTTP endpoint of the Connector / I4.0 Component.
 *  
 * @author dglachs
 *
 */
public class HttpComponent implements ConnectorEndpoint {
	private HttpServer httpServer;
	private ServerConfiguration serverConfiguration;
	private static int defaultPort = 5050;
	private int currentPort;
	
	/**
	 * The local service environment
	 */
//	private final LocalServiceEnvironment environment;
	/**
	 * List of the dedicated HttpHandlers 
	 */
	private final Map<String, HttpHandler> httpHandler = new HashMap<String, HttpHandler>();
	
	public HttpComponent() {
		this(defaultPort);
	}
	public HttpComponent(int port) {
		this.currentPort = port;
	}
	

	public int getPort() {
		return currentPort;
	}

	public URI getServerAddress(String host, int port, String context) {
		httpServer.getServerConfiguration().getHttpServerName();
		return URI.create(String.format("http://%s:%s/%s", host, port, context));
	}


	@Override
	public void start(String contextPath, ResourceConfig rootConfig) {
//		ResourceConfig rootConfig = new ResourceConfig();
//		rootConfig.register(new AbstractBinder() {
//			
//			@Override
//			protected void configure() {
//				bind(environment).to(ServiceEnvironment.class);
//				
//			}
//		});
//		rootConfig.register(AASJacksonMapperProvider.class);
//		rootConfig.register(MarshallingFeature.class);
//		rootConfig.register(AssetAdministrationRepositoryController.class);
		
		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, rootConfig);
		
		httpServer = GrizzlyHttpServerFactory.createHttpServer(
				// always create with localhost
				URI.create(String.format("http://%s:%s/%s", "0.0.0.0", currentPort, contextPath))
			);
		
		serverConfiguration = httpServer.getServerConfiguration();
		httpServer.getServerConfiguration().addHttpHandler(handler, "/");
		
		// create root handler handler with resource config
		
		
		try {
			httpServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	public void addHttpHandler(String alias, ResourceConfig shellConfig) {
//			ResourceConfig shellConfig = new ResourceConfig();
//			shellConfig.register(new AbstractBinder() {
//				
//				@Override
//				protected void configure() {
//					bind(environment).to(ServiceEnvironment.class);
//					
//				}
//			});
//			shellConfig.register(new AbstractBinder() {
//				
//				@Override
//				protected void configure() {
//					bind(aas.get()).to(AssetAdministrationShell.class);
//					
//				}
//			});
//			shellConfig.register(AASJacksonMapperProvider.class);
//			shellConfig.register(MarshallingFeature.class);
//			shellConfig.register(AssetAdministrationShellController.class);


//		}
		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, shellConfig);
		httpServer.getServerConfiguration().addHttpHandler(handler, "/" + alias);
		httpHandler.put(alias, handler);

	}

	@Override
	public boolean removeHttpHandler(String alias) {
		HttpHandler handler = httpHandler.get(alias);
		if (handler != null) {
			return serverConfiguration.removeHttpHandler(handler);
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			// create custom ObjectMapper
//			ObjectMapper mapper = ClientFactory.getObjectMapper();

			final LocalServiceEnvironment environment = new LocalServiceEnvironment(URI.create("http://localhost:8080/"));
			// create JsonProvider to provide custom ObjectMapper
//			JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
//			provider.setMapper(mapper);

			// configure REST service
			ResourceConfig rc = new ResourceConfig();
			rc.register(new AbstractBinder() {

				@Override
				protected void configure() {
					bind(environment).to(ServiceEnvironment.class);

				}
			});
			rc.register(AASJacksonMapperProvider.class);
			rc.register(MarshallingFeature.class);
			rc.register(AssetAdministrationRepositoryController.class);
//			rc.register(provider);

			// create Grizzly instance and add handler
			HttpHandler handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, rc);
			URI uri = new URI("http://0.0.0.0:5050/");
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri);
			ServerConfiguration config = server.getServerConfiguration();
			config.addHttpHandler(handler, "/");

			Optional<AssetAdministrationShell> aas = environment.getAssetAdministrationShell("https://acplt.org/Test_AssetAdministrationShell");
			if (aas.isPresent()) {
				ResourceConfig shellConfig = new ResourceConfig();
				shellConfig.register(new AbstractBinder() {
					
					@Override
					protected void configure() {
						bind(environment).to(ServiceEnvironment.class);
						
					}
				});
				shellConfig.register(new AbstractBinder() {
					
					@Override
					protected void configure() {
						bind(aas.get()).to(AssetAdministrationShell.class);
						
					}
				});
				shellConfig.register(AASJacksonMapperProvider.class);
				shellConfig.register(MarshallingFeature.class);
				shellConfig.register(AssetAdministrationShellController.class);

				GrizzlyHttpContainer shellHandler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, shellConfig);
				config.addHttpHandler(shellHandler, "/test");

			}

			
			// start
			server.start();
			
			System.in.read();

		} catch (ProcessingException | URISyntaxException | IOException e) {
			throw new Error("Unable to create HTTP server.", e);
		}
	}



}

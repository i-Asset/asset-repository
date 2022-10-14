package at.srfg.iasset.connector.component.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.ws.rs.ProcessingException;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.AssetAdministrationShellDescriptor;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.util.Base64Utils;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.config.MarshallingFeature;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationRepositoryController;
import at.srfg.iasset.connector.component.impl.jersey.AssetAdministrationShellController;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASJacksonMapperProvider;

public class HttpComponent implements ConnectorEndpoint {
	private HttpServer httpServer;
	private ServerConfiguration serverConfiguration;
	private int defaultPort = 5050;
	private int currentPort;
//	private String contextPath;
	private final URI repositoryURI;
	
	private RepositoryConnection repoConnector;
	/**
	 * The local service environment
	 */
	private final ServiceEnvironment environment;
	/**
	 * List of the dedicated HttpHandlers 
	 */
	private final Map<String, HttpHandler> httpHandler = new HashMap<String, HttpHandler>();
	
	private final Set<String> registrations = new HashSet<String>();
	/**
	 * List of {@link AssetAdministrationShellDescriptor}s actively registered with the server
	 */
	private List<AssetAdministrationShellDescriptor> registered;

	public HttpComponent(URI repositoryURI, ServiceEnvironment environment) {
		this.repositoryURI = repositoryURI;
		this.environment = environment;
		this.repoConnector = RepositoryConnection.getConnector(repositoryURI);

	}
	

	public String getHostAddress() {
		try {
			InetAddress adr = InetAddress.getLocalHost();
			return adr.getHostAddress();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}

	public URI getServerAddress(String host, int port, String context) {
		httpServer.getServerConfiguration().getHttpServerName();
		return URI.create(String.format("http://%s:%s/%s", host, port, context));
	}

	public void start(int port) {
		start(port, "");
	}
	public void start(int port, String contextPath) {
		currentPort = port;
		ResourceConfig rootConfig = new ResourceConfig();
		rootConfig.register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(environment).to(ServiceEnvironment.class);
				
			}
		});
		rootConfig.register(AASJacksonMapperProvider.class);
		rootConfig.register(MarshallingFeature.class);
		rootConfig.register(AssetAdministrationRepositoryController.class);
		
		GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, rootConfig);
		
		httpServer = GrizzlyHttpServerFactory.createHttpServer(
				// create with localhost
				URI.create(String.format("http://%s:%s/%s", "0.0.0.0", port, contextPath))
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
	public void start() {
		start(defaultPort, "");
	}

	@Override
	public void stop() {
		if (httpServer != null && httpServer.isStarted()) {
			try {
				registrations.forEach(new Consumer<String>() {

					@Override
					public void accept(String t) {
						repoConnector.unregister(t);
					}
				});
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

			GrizzlyHttpContainer handler = ContainerFactory.createContainer(GrizzlyHttpContainer.class, shellConfig);
			httpServer.getServerConfiguration().addHttpHandler(handler, "/" + alias);
			httpHandler.put(alias, handler);

		}

	}

	@Override
	public boolean removeShellHandler(String alias) {
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

			final LocalServiceEnvironment environment = new LocalServiceEnvironment();
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


	@Override
	public void register(String aasIdentifier) {

		Optional<AssetAdministrationShell> shellToRegister = environment.getAssetAdministrationShell(aasIdentifier);
		if ( shellToRegister.isPresent()) {
			String idEncoded = Base64Utils.encodeToString(aasIdentifier.getBytes());
			String pathToShell = repositoryURI.getPath() + String.format("shells/%s", idEncoded);
			URI shellUri = URI.create(String.format("%s://%s:%s%s", repositoryURI.getScheme(), getHostAddress(), currentPort, pathToShell));
			repoConnector.register(shellToRegister.get(), shellUri);
			// keep in the list of active registrations
			registrations.add(aasIdentifier);
		}
		
	}
	public void unregister(String aasIdentifier) {
		repoConnector.unregister(aasIdentifier);
		registrations.remove(aasIdentifier);
	}
}

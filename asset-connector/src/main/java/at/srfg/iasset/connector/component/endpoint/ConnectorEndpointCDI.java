package at.srfg.iasset.connector.component.endpoint;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Enumeration;
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
			InetAddress search = getLocalHostLANAddress();
			String host = search.getHostAddress();
//			String host = InetAddress.getLocalHost().getHostAddress();
			endpointAddress = URI.create(String.format("http://%s:%s%s", host, this.port, contextRoot));
			httpServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Returns an <code>InetAddress</code> object encapsulating what is most likely the machine's LAN IP address.
	 * <p/>
	 * This method is intended for use as a replacement of JDK method <code>InetAddress.getLocalHost</code>, because
	 * that method is ambiguous on Linux systems. Linux systems enumerate the loopback network interface the same
	 * way as regular LAN network interfaces, but the JDK <code>InetAddress.getLocalHost</code> method does not
	 * specify the algorithm used to select the address returned under such circumstances, and will often return the
	 * loopback address, which is not valid for network communication. Details
	 * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
	 * <p/>
	 * This method will scan all IP addresses on all network interfaces on the host machine to determine the IP address
	 * most likely to be the machine's LAN address. If the machine has multiple IP addresses, this method will prefer
	 * a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually IPv4) if the machine has one (and will return the
	 * first site-local address if the machine has more than one), but if the machine does not hold a site-local
	 * address, this method will return simply the first non-loopback address found (IPv4 or IPv6).
	 * <p/>
	 * If this method cannot find a non-loopback address using this selection algorithm, it will fall back to
	 * calling and returning the result of JDK method <code>InetAddress.getLocalHost</code>.
	 * <p/>
	 *
	 * @throws UnknownHostException If the LAN address of the machine cannot be found.
	 */
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
	    try {
	        InetAddress candidateAddress = null;
	        // Iterate all NICs (network interface cards)...
	        for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
	            NetworkInterface iface = ifaces.nextElement();
	            // Iterate all IP addresses assigned to each card...
	            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
	                InetAddress inetAddr = inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {

	                    if (inetAddr.isSiteLocalAddress()) {
	                        // Found non-loopback site-local address. Return it immediately...
	                        return inetAddr;
	                    }
	                    else if (candidateAddress == null) {
	                        // Found non-loopback address, but not necessarily site-local.
	                        // Store it as a candidate to be returned if site-local address is not subsequently found...
	                        candidateAddress = inetAddr;
	                        // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
	                        // only the first. For subsequent iterations, candidate will be non-null.
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            // We did not find a site-local address, but we found some other non-loopback address.
	            // Server might have a non-site-local address assigned to its NIC (or it might be running
	            // IPv6 which deprecates the "site-local" concept).
	            // Return this non-loopback candidate address...
	            return candidateAddress;
	        }
	        // At this point, we did not find a non-loopback address.
	        // Fall back to returning whatever InetAddress.getLocalHost() returns...
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        if (jdkSuppliedAddress == null) {
	            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
	        }
	        return jdkSuppliedAddress;
	    }
	    catch (Exception e) {
	        UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
	        unknownHostException.initCause(e);
	        throw unknownHostException;
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
    			._interface("AAS-REPOSITORY-3.0_ITWIN")
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
					._interface("AAS-3.0_ITWIN")		
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
    				._interface("SUBMODEL-3.0_ITWIN")
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

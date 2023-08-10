package at.srfg.iasset.connector.component;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Represents the REST endpoint of the I4.0 {@link Connector}. 
 * @author dglachs
 *
 */
public interface ConnectorEndpoint {
	
	/**
	 * Start a I40 Component/Device, eg. start the REST service providing
	 * external access to this device
	 * @param context The context for the component/device
	 * @param rootConfig The {@link ResourceConfig} configuring the REST service endpoint. 
	 * 
	 */
	public void start();
	public void start(int port);
	public void start(String context, ResourceConfig rootConfig);
	/**
	 * Obtain the port the component is currently providing it's services
	 * @return The service port
	 */
	public int getPort();
	/**
	 * Stop servicing the component
	 */
	public void stop();
	/**
	 * Determine whether the component is active or not
	 * @return <code>true</code> when already started, <code>false</code> otherwise
	 */
	public boolean isStarted();
	/**
	 * Create and start an additional {@link HttpHandler}
	 * @param alias The alias name or context path for the handler 
	 * @param config The {@link ResourceConfig} configuring the handler's REST service endpoint
	 */
	public void addHttpHandler(String alias, ResourceConfig config);
	public void startAlias(String alias, AssetAdministrationShell shell);
	/**
	 * Remove a previously started {@link HttpHandler} based on the alias name
	 * @param alias The name for the {@link HttpHandler}
	 * @return <code>true</code> when a handler is found and stopped, <code>false</code> otherwise
	 */
	public boolean removeHttpHandler(String alias);

}

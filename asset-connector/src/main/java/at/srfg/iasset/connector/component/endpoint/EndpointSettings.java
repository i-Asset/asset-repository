package at.srfg.iasset.connector.component.endpoint;

import at.srfg.iasset.connector.component.config.Configurable;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
/**
 * Settings
 * @author dglachs
 *
 */
@Dependent
public class EndpointSettings {

	@Inject
	@Configurable("endpoint.context")
	private String context;
	@Inject
	@Configurable("endpoint.port")
	private int port;

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	
}

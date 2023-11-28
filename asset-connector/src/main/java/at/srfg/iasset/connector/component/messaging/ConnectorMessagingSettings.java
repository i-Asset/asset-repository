package at.srfg.iasset.connector.component.messaging;

import at.srfg.iasset.connector.component.config.Configurable;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
/**
 * Settig
 * @author dglachs
 *
 */
@Dependent
@Default
@Configurable("connector.network.broker")
public class ConnectorMessagingSettings {

	@Inject
	@Configurable("hosts")
	private String hosts;
	@Inject
	@Configurable("type")
	private String type;

	/**
	 * @return the context
	 */
	public String getHosts() {
		return hosts;
	}
	public String getBrokerType() {
		return type;
	}
}

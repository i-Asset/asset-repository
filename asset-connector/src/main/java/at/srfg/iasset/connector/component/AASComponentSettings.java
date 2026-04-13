package at.srfg.iasset.connector.component;

import at.srfg.iasset.connector.component.config.Configurable;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

/**
 * Settings
 * @author fstroh
 *
 */
@Dependent
public class AASComponentSettings {
    @Inject
    @Configurable("aas_component.disable_endpoint")
    private boolean disableEndpoint;

    @Inject
    @Configurable("aas_component.disable_registration")
    private boolean disableRegistration;

    public boolean endpointDisabled() {
        return disableEndpoint;
    }

    public boolean registrationDisabled() {
        return disableRegistration;
    }

}

package at.srfg.iasset.connector.isproNG;

import at.srfg.iasset.connector.component.config.Configurable;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class IsproNGSettings {
    @Inject
    @Configurable("connector.isprong.baseUri")
    private String baseUri;

    public String getBaseUri() {
        return baseUri;
    }

    public String getApikey() {
        return apikey;
    }

    @Inject
    @Configurable("connector.isprong.apikey")
    private String apikey;


}

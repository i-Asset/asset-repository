package at.srfg.iasset.connector.component.config;

import at.srfg.iasset.repository.config.AASJacksonMapperProvider;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
/**
 * Feature is requested for customizing the {@link CustomJsonProvider}
 * for {@link MessageBodyReader} and {@link MessageBodyWriter}
 * 
 * @author dglachs
 *
 */
public class MarshallingFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
    	AASJacksonMapperProvider provider = new AASJacksonMapperProvider();
    	
        context.register(provider);
        return true;
    }
}
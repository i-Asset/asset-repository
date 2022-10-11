package at.srfg.iasset.connector.component.config;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
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
        context.register(CustomJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);
        return true;
    }
}
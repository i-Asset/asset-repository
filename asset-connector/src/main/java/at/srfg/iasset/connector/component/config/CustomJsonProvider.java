package at.srfg.iasset.connector.component.config;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;

/**
 * Provider for the customized ObjectMapper. 
 * 
 * @author dglachs
 *
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class CustomJsonProvider extends JacksonJaxbJsonProvider {

    public CustomJsonProvider() {
        super();
        setMapper(ClientFactory.getObjectMapper());
    }

}

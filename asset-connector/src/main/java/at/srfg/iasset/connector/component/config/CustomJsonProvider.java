package at.srfg.iasset.connector.component.config;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import at.srfg.iasset.repository.connectivity.rest.ClientFactory;

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

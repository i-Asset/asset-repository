package at.srfg.iasset.connector.component.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ext.ContextResolver;

@ApplicationScoped
public class ObjectMapperProvider {
	@Produces
	public ObjectMapper getObjectMapper() {
		return ClientFactory.getObjectMapper();
	}
	@Produces
	public ContextResolver<ObjectMapper> getContextResolver() {
		return ClientFactory.getContextResolver();
	}
}

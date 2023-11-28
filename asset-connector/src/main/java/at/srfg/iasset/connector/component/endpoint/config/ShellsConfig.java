package at.srfg.iasset.connector.component.endpoint.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.AccessDeniedExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.AuthenticationExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.AuthenticationTokenRefreshmentExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.BadRequestExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.NotFoundExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.controller.AssetAdministrationRepositoryController;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASJacksonMapperProvider;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("shells")
public class ShellsConfig extends ResourceConfig {

    public ShellsConfig(ServiceEnvironment environment) {
    	
    	setApplicationName("shells");
        register(AssetAdministrationRepositoryController.class);
        
        // TODO: enable authentication/authorization
//        register(AuthenticationFilter.class);
//        register(AuthorizationFilter.class);
        
        // TODO: add exception mappers
        register(AccessDeniedExceptionMapper.class);
        register(AuthenticationExceptionMapper.class);
        register(AuthenticationTokenRefreshmentExceptionMapper.class);
        register(NotFoundExceptionMapper.class);
        register(BadRequestExceptionMapper.class);
        
        register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(environment).to(ServiceEnvironment.class);
				
			}
		});
        
        // use the AAS configured ObjectMapper Provider
        register(AASJacksonMapperProvider.class);
    }
}
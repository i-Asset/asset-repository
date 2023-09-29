package at.srfg.iasset.connector.component.endpoint.config;

import org.eclipse.digitaltwin.aas4j.v3.model.AssetAdministrationShell;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.AccessDeniedExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.AuthenticationExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.AuthenticationTokenRefreshmentExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.BadRequestExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.config.exception.mapper.NotFoundExceptionMapper;
import at.srfg.iasset.connector.component.endpoint.controller.AssetAdministrationShellController;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.config.AASJacksonMapperProvider;

public class AliasConfig extends ResourceConfig {

    public AliasConfig(ServiceEnvironment environment, AssetAdministrationShell shell) {
    	setApplicationName(shell.getId());

        register(AssetAdministrationShellController.class);
        
        // TODO: enable authentication/authorization
//        register(AuthenticationFilter.class);
//        register(AuthorizationFilter.class);
        
        // TODO: add exception mappers
        register(AccessDeniedExceptionMapper.class);
        register(AuthenticationExceptionMapper.class);
        register(AuthenticationTokenRefreshmentExceptionMapper.class);
        register(NotFoundExceptionMapper.class);
        register(BadRequestExceptionMapper.class);

        // use the AAS configured ObjectMapper Provider
        register(new AbstractBinder() {

				@Override
				protected void configure() {
					bind(environment).to(ServiceEnvironment.class);
					bind(shell).to(AssetAdministrationShell.class);

				}
			});

        register(AASJacksonMapperProvider.class);
    }
}
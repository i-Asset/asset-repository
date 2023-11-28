package at.srfg.iasset.connector.component.endpoint.config;

import java.io.IOException;
import java.util.Collections;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

/**
 * JWT authentication filter.
 *
 * @author cassiomolin
 */
@Provider
@Dependent
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

//    @Inject
//    private UserService userService;
//
//    @Inject
//    private AuthenticationTokenService authenticationTokenService;
	@Inject
	private AuthenticationTokenParser tokenParser;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String authenticationToken = authorizationHeader.substring(7);
            handleTokenBasedAuthentication(authenticationToken, requestContext);
            return;
        }

        // Other authentication schemes (such as Basic) could be supported
    }

    private void handleTokenBasedAuthentication(String authenticationToken, ContainerRequestContext requestContext) {

        AuthenticationTokenDetails authenticationTokenDetails = tokenParser.parseToken(authenticationToken);
//        User user = userService.findByUsernameOrEmail(authenticationTokenDetails.getUsername());
        AuthenticatedUserDetails authenticatedUserDetails = new AuthenticatedUserDetails("demo", Collections.singleton(Authority.ADMIN));

        boolean isSecure = requestContext.getSecurityContext().isSecure();
        SecurityContext securityContext = new TokenBasedSecurityContext(authenticatedUserDetails, authenticationTokenDetails, isSecure);
        requestContext.setSecurityContext(securityContext);
    }
}
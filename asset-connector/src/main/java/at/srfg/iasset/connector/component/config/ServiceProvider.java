package at.srfg.iasset.connector.component.config;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.oauth2.ClientIdentifier;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.OAuth2Parameters;

import com.fasterxml.jackson.core.util.JacksonFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.connector.component.endpoint.RepositoryConnectionSettings;
import at.srfg.iasset.repository.api.DirectoryInterface;
import at.srfg.iasset.repository.api.IAssetAdministrationShellRepositoryInterface;
import at.srfg.iasset.repository.api.SubmodelRepositoryInterface;
import at.srfg.iasset.repository.connectivity.rest.ConsumerFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.ext.ContextResolver;
/**
 * Class providing the service interfaces for local use.
 * 
 * @author dglachs
 *
 */
@ApplicationScoped
public class ServiceProvider {
	@Inject
	private RepositoryConnectionSettings settings;
	
	@Inject
	private ContextResolver<ObjectMapper> contextResolver;
	
	
	public DirectoryInterface getDirectoryInterface() {
		return getService(settings.getRepositoryBaseUri()+"directory", DirectoryInterface.class);
	}
	public IAssetAdministrationShellRepositoryInterface getRepositoryInterface() {
		return getService(settings.getRepositoryBaseUri()+"repository", IAssetAdministrationShellRepositoryInterface.class);
	}
	public SubmodelRepositoryInterface getSubmodelInterface() {
		return getService(settings.getRepositoryBaseUri()+"subrepo", SubmodelRepositoryInterface.class);
	}
	/*
	 * 
	 */
	private <T> T getService(String baseUrl, Class<T> clientClazz) {
		if ( settings.isSecurityEnabled()) {
			
		}
		return ConsumerFactory.createConsumer(
				baseUrl, getClient(
				// is OAuth2 Security enabled
//						(Feature)null
				settings.isSecurityEnabled() 
					? getOAuth2Feature(settings.getClientId(), settings.getClientSecret())
					: null
				), clientClazz);
	}

	private Client getClient() {
		ClientConfig config = new ClientConfig();
//		// register the objectmapper
		config.register(JacksonFeature.class);
		config.register(contextResolver);
		
		return JerseyClientBuilder.newBuilder()
				.withConfig(config)
				// optionally add truststore 
//				.trustStore(keyStore)
				.build();
		
	}
	private Client getClient(Feature ... feature) {
		Client client = getClient();
		if ( feature != null) {
			for (Feature f : feature) {
				if ( f!= null) {
					client.register(f);
				}
			}
		}
		return client;
	}
	private Feature getOAuth2Feature(String clientIdentifier, byte[] secret) {
		ClientIdentifier clientId = new ClientIdentifier(
				// Client Identifier
				clientIdentifier,
				// Client Secret
				secret);
		// TODO: replace "scope", "issuerURI" with values from settings
		OAuth2CodeGrantFlow flow = OAuth2ClientSupport.authorizationCodeGrantFlowBuilder(clientId, "scope", "issuerUri") 
				.property(OAuth2CodeGrantFlow.Phase.ACCESS_TOKEN_REQUEST, OAuth2Parameters.GrantType.key, "client_credentials")
				.property(OAuth2CodeGrantFlow.Phase.ACCESS_TOKEN_REQUEST, OAuth2Parameters.SCOPE, "scope")
				.build();
		// finish the flow to have the state verified and the token stored
		flow.finish(
				settings.getClientCode(), 
				getFlowState(flow));
		return flow.getOAuth2Feature();
		
	}
	private static String getFlowState(OAuth2CodeGrantFlow flow) {
		Pattern pattern = Pattern.compile("[(\\?|\\&)]([^=]+)\\=([^&#]+)");
		Matcher matcher = pattern.matcher(flow.start());
		while (matcher.find()) {
			String paramName = matcher.group(1);
			String paramValue = matcher.group(2);
			if ("state".equals(paramName)) {
				try {
					return URLDecoder.decode(paramValue, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}

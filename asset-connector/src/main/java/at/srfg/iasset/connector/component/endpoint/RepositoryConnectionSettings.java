package at.srfg.iasset.connector.component.endpoint;

import java.net.URI;
import java.net.URISyntaxException;

import at.srfg.iasset.connector.component.config.Configurable;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class RepositoryConnectionSettings {
	@Inject
	@Configurable("repository.baseUri")
	private String uri;
	
	@Inject
	@Configurable("security.client.id")
	private String clientId;
	
	@Inject
	@Configurable("security.client.secret")
	private String clientSecret;
	
	@Inject
	@Configurable("security.client.code")
	private String clientCode;

	@Inject
	@Configurable("security.client.scope")
	private String clientScope;

	@Inject
	@Configurable("security.enabled")
	private boolean oAuthEnabled;
	
	
	
	
	public URI getRepositoryUri() throws URISyntaxException {
		return new URI(uri);
	}
	public String getRepositoryBaseUri() {
		if (! uri.endsWith("/")) {
			return uri + "/";
		}
		return uri;
	}
	public byte[] getClientSecret() {
		return clientSecret.getBytes();
	}
	public String getClientId() {
		return clientId;
	}
	public String getClientCode() {
		return clientCode;
	}
	public boolean isSecurityEnabled() {
		return oAuthEnabled;
	}
}

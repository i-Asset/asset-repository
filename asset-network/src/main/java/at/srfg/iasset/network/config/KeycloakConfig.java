package at.srfg.iasset.network.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class KeycloakConfig {
	  @Value("${iasset.keycloak.server-url")
	  private String serverUrl;
	  @Value("${iasset.keycloak.realm}")
	  private String realm;
	  @Value("${iasset.keycloak.username}")
	  private String username;
	  @Value("${iasset.keycloak.password}")
	  private String password;
	  
	  @Bean
	  public Keycloak keycloak() {
		  return KeycloakBuilder.builder()
		  	.serverUrl(serverUrl)
		  	.realm(realm)
		  	.clientId("admin-cli")
		  	.username(username)
		  	.password(password)
		  	.build();
	  }
}

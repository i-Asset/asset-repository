package at.srfg.iasset.network.services;

import java.util.Arrays;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.network.entities.User;

@Service
public class KeycloakService {
	@Autowired
	private Keycloak keycloak;
	
	public boolean createRole(String role) {
		
		return false;
	}
	public boolean createUser(User user) {
		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
		credentialRepresentation.setValue("changeme");
		credentialRepresentation.setTemporary(Boolean.TRUE);
		
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setUsername(user.getUsername());
		userRepresentation.setEmail(user.getEmail());
		userRepresentation.setFirstName(user.getGivenName());
		userRepresentation.setLastName(user.getFamilyName());
		userRepresentation.setEmailVerified(false);;
		userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));
		
		keycloak.realm("iasset-realm").users().create(userRepresentation);
		return true;
	}


}

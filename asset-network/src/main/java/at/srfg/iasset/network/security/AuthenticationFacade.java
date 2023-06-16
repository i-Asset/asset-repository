package at.srfg.iasset.network.security;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import at.srfg.iasset.network.entities.User;
import at.srfg.iasset.network.services.UserService;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
	
	@Autowired
	private UserService userService;

	@Override
	public Authentication getAuthentication() {
		
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	@Override
	public User getUserDetails() {
		
		
		String id = getAuthentication().getName();
			
		
		Optional<User> optUser =userService.findById(id);
		if ( optUser.isEmpty()) {
			
			Jwt jwt = (Jwt) getAuthentication().getPrincipal();
			
			String preferred_username = jwt.getClaimAsString("preferred_username");
			String given_name = jwt.getClaimAsString("given_name");
			String family_name = jwt.getClaimAsString("family_name");
			String name = jwt.getClaimAsString("name");
			String email = jwt.getClaimAsString("email");
			// 
			User user = new User();
			user.setId(UUID.fromString(id));
			user.setCreated(LocalDateTime.now());
			user.setUsername(preferred_username);
			user.setName(name);
			user.setGivenName(given_name);
			user.setFamilyName(family_name);
			user.setEmail(email);
			userService.create(user);
			return user;
		}
		return optUser.get();
		
	}
	
}

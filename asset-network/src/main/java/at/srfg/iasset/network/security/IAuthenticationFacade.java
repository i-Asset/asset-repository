package at.srfg.iasset.network.security;

import org.springframework.security.core.Authentication;

import at.srfg.iasset.network.entities.User;

public interface IAuthenticationFacade {
	Authentication  getAuthentication();

	User getUserDetails();

}

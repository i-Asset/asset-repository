package at.srfg.iasset.network.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.network.entities.User;
import at.srfg.iasset.network.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public User create(User user) {
		return userRepository.save(user);
	}
	
	public Optional<User> findById(String uuid) {
		try {
			return userRepository.findById(UUID.fromString(uuid));
		} catch (IllegalArgumentException ex) {
			User user = new User();
			user.setId(UUID.randomUUID());
			user.setName("Anonymous");
			// provided argument is not a UUID
			return Optional.of(user);
		}
	}
	public Optional<User> findByUsername(String preferredUsername) {
		return userRepository.findByUsername(preferredUsername);
	}
	

}

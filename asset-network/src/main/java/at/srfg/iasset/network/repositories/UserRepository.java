package at.srfg.iasset.network.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.srfg.iasset.network.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	/**
	 * Find a user by it's username
	 * @param username
	 * @return
	 */
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}

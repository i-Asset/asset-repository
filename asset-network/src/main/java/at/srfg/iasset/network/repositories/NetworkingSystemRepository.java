package at.srfg.iasset.network.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.srfg.iasset.network.entities.Enterprise;
import at.srfg.iasset.network.entities.NetworkingSystem;

@Repository
public interface NetworkingSystemRepository extends JpaRepository<NetworkingSystem, Long> {
	Optional<NetworkingSystem> findByNetwork(String systemName);
	
	List<NetworkingSystem> findByEnterprise(Enterprise enterprise);

}

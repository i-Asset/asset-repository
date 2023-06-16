package at.srfg.iasset.network.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.network.entities.NetworkingSystem;
import at.srfg.iasset.network.repositories.NetworkingSystemRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NetworkingSystemService {
	@Autowired
	NetworkingSystemRepository repo;
	public boolean isAdminOf(NetworkingSystem system) {
		return true;
	}
	public NetworkingSystem create(NetworkingSystem enterprise) {
		return enterprise;
	}
	public NetworkingSystem get(Long id) {
		return repo.findById(id).orElse(null);
	}
	public List<NetworkingSystem> listAll() {
		return repo.findAll();
	}
	public NetworkingSystem update(NetworkingSystem update) {
		return update;
	}
	public NetworkingSystem delete(Long id) {
		Optional<NetworkingSystem> toDelete = repo.findById(id);
		if ( toDelete.isPresent()) {
			repo.delete(toDelete.get());
			return toDelete.get();
		}
		return null;
	}
	public NetworkingSystem findByNamespace(String namespace) {
		return repo.findByNetwork(namespace).orElse(null);
	}

}

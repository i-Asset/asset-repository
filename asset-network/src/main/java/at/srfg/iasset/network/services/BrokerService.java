package at.srfg.iasset.network.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.network.entities.Broker;
import at.srfg.iasset.network.repositories.BrokerRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrokerService {
	@Autowired
	BrokerRepository repo;
	
	public Broker create(Broker enterprise) {
		return enterprise;
	}
	public Broker get(Long id) {
		return repo.findById(id).orElse(null);
	}
	public List<Broker> listAll() {
		return repo.findAll();
	}
	public Broker update(Broker update) {
		return update;
	}
	public Broker delete(Long id) {
		return null;
	}

}

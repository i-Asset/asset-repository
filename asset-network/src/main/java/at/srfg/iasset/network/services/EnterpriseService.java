package at.srfg.iasset.network.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.srfg.iasset.network.entities.Enterprise;
import at.srfg.iasset.network.entities.NetworkingSystem;
import at.srfg.iasset.network.repositories.EnterpriseRepository;
import at.srfg.iasset.network.repositories.NetworkingSystemRepository;
import at.srfg.iasset.network.security.IAuthenticationFacade;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

@Service
@Transactional
public class EnterpriseService {
	@Autowired
	EnterpriseRepository repo;
	
	@Autowired
	NetworkingSystemRepository networkRepo;
	
	@Autowired
	IAuthenticationFacade auth;
	/**
	 * Check whether the current user is admin of the provided enterprise
	 * @param enterprise
	 * @return
	 */
	public boolean isAdminOf(Enterprise enterprise) {
		return true;
	}
	/**
	 * 
	 * @param enterprise
	 * @return
	 */
	public Enterprise create(Enterprise enterprise) {
		return enterprise;
	}
	public Enterprise get(Long id) {
		return repo.findById(id).orElse(null);
	}
	public List<Enterprise> listAll() {
		return List.of();
	}
	public Enterprise update(Enterprise update) {
		return update;
	}
	public Enterprise delete(Long id) {
		return null;
	}
	public List<NetworkingSystem> getNetworks(@NotNull Long id) {
		Optional<Enterprise> enterprise = repo.findById(id);
		if ( enterprise.isPresent()) {
			if ( isAdminOf(enterprise.get())) {
				return networkRepo.findByEnterprise(enterprise.get());
			}
		}
		return Collections.emptyList();
	}

}

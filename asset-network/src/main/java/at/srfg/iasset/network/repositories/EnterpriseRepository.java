package at.srfg.iasset.network.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.srfg.iasset.network.entities.Enterprise;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
	Optional<Enterprise> findByLegalName(String legalName);
	

}

package at.srfg.iasset.network.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.srfg.iasset.network.entities.Broker;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Long> {

}

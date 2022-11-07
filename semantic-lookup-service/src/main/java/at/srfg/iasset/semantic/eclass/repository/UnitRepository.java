package at.srfg.iasset.semantic.eclass.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.eclass.model.PropertyUnit;

public interface UnitRepository extends CrudRepository<PropertyUnit, String>{
	Optional<PropertyUnit> findByEceCode(String eceCode);
	List<PropertyUnit> findByEceName(String eceName);
}

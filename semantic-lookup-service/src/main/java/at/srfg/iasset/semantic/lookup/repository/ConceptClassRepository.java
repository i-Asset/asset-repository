package at.srfg.iasset.semantic.lookup.repository;

import java.util.List;
import java.util.Optional;

import at.srfg.iasset.semantic.model.ConceptClass;

public interface ConceptClassRepository extends ConceptRepository<ConceptClass>{
	List<ConceptClass> findByLevelAndCodedNameLike(int level, String codedName);
	Optional<ConceptClass> findByLevelAndCodedName(int level, String codedName);
	List<ConceptClass> findByParentElement(ConceptClass parent);
}

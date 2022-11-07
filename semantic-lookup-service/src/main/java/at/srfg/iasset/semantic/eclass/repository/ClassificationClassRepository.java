package at.srfg.iasset.semantic.eclass.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.srfg.iasset.semantic.eclass.model.ClassificationClass;

public interface ClassificationClassRepository extends CrudRepository<ClassificationClass, String> {

	ClassificationClass findByIrdiCC(String irdiCC);
	@Query("SELECT c FROM ClassificationClass c WHERE c.level = ?1 AND c.codedName = ?2")
	Optional<ClassificationClass> findParent(int level, String codedName);
	List<ClassificationClass> findByIdCC(String idCC);
	List<ClassificationClass> findByIdentifier(String identifier);
	List<ClassificationClass> findByCodedNameLike(String codedName);
	List<ClassificationClass> findByLevelAndCodedNameLike(int level, String codedName);
	long countBySubclassPresentAndCodedNameLike(boolean subclassPresent, String codedName);
}
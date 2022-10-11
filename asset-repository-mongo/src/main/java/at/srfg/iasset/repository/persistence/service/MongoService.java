//package at.srfg.iasset.repository.persistence.service;
//
//import java.util.Optional;
//
//import org.eclipse.aas4j.v3.model.Identifiable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MongoService<T extends Identifiable> {
//	@Autowired
//	MongoRepository<T, String> repo;
//	
//	public Optional<T> findById(String id) {
//		return repo.findById(id);
//	}
//
//	public void save(T data) {
//		repo.save(data);
//	}
//}

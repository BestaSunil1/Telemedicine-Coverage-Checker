package training.iqgateway.repositories;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Patient;
import training.iqgateway.entities.User;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
	 Optional<Patient> findByUser(User user);
	    Optional<Patient> findByUserId(String userId);
	    Optional<Patient> findByUserEmail(String email);
	    boolean existsByUser(User user);
}

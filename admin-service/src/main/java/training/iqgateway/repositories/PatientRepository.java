package training.iqgateway.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Patient;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

}

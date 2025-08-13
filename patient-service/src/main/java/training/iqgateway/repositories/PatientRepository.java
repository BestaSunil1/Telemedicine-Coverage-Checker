package training.iqgateway.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Patient;


@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    // Finds the patient by the ID of the referenced User (DBRef)
    Optional<Patient> findByUser_Id(String userId);
}


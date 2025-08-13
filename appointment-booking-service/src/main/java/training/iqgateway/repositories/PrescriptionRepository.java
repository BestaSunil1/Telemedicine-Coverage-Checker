package training.iqgateway.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import training.iqgateway.entities.Prescription;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    List<Prescription> findByPatientId(String patientId);
}


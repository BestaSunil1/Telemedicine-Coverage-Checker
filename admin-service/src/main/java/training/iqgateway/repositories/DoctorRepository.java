package training.iqgateway.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Doctor;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

	Optional<Doctor> findByUserId(String userId);
    List<Doctor> findByActiveTrue();
    List<Doctor> findBySpecializationsContaining(String specialization);
    List<Doctor> findByActiveTrueAndSpecializationsContaining(String specialization);


}

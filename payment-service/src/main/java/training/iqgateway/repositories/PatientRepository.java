package training.iqgateway.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Patient;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
	@Query("{ 'user.email': ?0 }")
    Optional<Patient> findByEmail(String email);
    
    Optional<Patient> findByContactNumber(String contactNumber);
    
    @Query("{ 'user.name': { $regex: ?0, $options: 'i' } }")
    List<Patient> findByNameContainingIgnoreCase(String name);
    
    List<Patient> findByGender(String gender);
    
    List<Patient> findByDateOfBirth(String dateOfBirth);
    
    @Query("{ 'user.username': ?0 }")
    Optional<Patient> findByUsername(String username);
    
    @Query("{ 'user.id': ?0 }")
    Optional<Patient> findByUserId(String userId);

}

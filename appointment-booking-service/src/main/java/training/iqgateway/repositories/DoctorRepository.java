package training.iqgateway.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Doctor;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

	@Query("{ 'specializations': { $in: [?0] } }")
    List<Doctor> findBySpecialization(String specialization);
    
    @Query("{ 'user.name': { $regex: ?0, $options: 'i' } }")
    List<Doctor> findByNameContainingIgnoreCase(String name);
    
    @Query("{ 'active': true }")
    List<Doctor> findAvailableDoctors();
    
    @Query("{ 'specializations': { $in: [?0] }, 'active': true }")
    List<Doctor> findAvailableDoctorsBySpecialization(String specialization);
    
    List<Doctor> findByActive(boolean active);
    
    @Query("{ 'specializations': { $all: ?0 } }")
    List<Doctor> findByAllSpecializations(List<String> specializations);

}

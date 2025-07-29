package training.iqgateway.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Doctor;
import training.iqgateway.entities.DoctorAvailability;


@Repository
public interface DoctorAvalilabilityRepository extends MongoRepository<DoctorAvailability, String> {

	@Query("{ 'doctor.$id': ?0 }")
    DoctorAvailability findByDoctorId(String doctorId);
    
	@Query("{'doctor.$id': ?#{[0]} , 'day_of_week': ?1, 'is_available': ?2}")
	DoctorAvailability findByDoctorIdAndDayOfWeekAndIsAvailable(ObjectId doctorId, String dayOfWeek, boolean isAvailable);

 

}

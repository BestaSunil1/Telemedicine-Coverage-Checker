package training.iqgateway.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.DoctorAvailability;

@Repository
public interface DoctorAvailabilityRepository extends MongoRepository<DoctorAvailability, String> {
    List<DoctorAvailability> findByDoctorId(String doctorId);
    List<DoctorAvailability> findByDoctorIdAndDayOfWeek(String doctorId, String dayOfWeek);
    List<DoctorAvailability> findByDoctorIdAndIsAvailableTrue(String doctorId);
    void deleteByDoctorId(String doctorId);
}

package training.iqgateway.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.AppointmentNote;

@Repository
public interface AppointmentNoteRepository extends MongoRepository<AppointmentNote, String> {

	 @Query("{ 'appointment.$id': ?0 }")
    List<AppointmentNote> findByAppointmentId(String appointmentId);
    
    @Query(value = "{ 'appointment.$id': ?0 }", sort = "{ 'created_at': -1 }")
    List<AppointmentNote> findByAppointmentIdOrderByCreatedAtDesc(String appointmentId);
    
    @Query("{ 'appointment.$id': ?0, 'note_type': ?1 }")
    List<AppointmentNote> findByAppointmentIdAndNoteType(String appointmentId, String noteType);
    
    @Query(value = "{ 'appointment.$id': ?0 }", count = true)
    Long countByAppointmentId(String appointmentId);
    
    List<AppointmentNote> findByCreatedBy(String createdBy);
    
    List<AppointmentNote> findByNoteType(String noteType);
    
    @Query("{ 'created_at': { $gte: ?0, $lte: ?1 } }")
    List<AppointmentNote> findByCreatedAtBetween(String startDate, String endDate);

}

package training.iqgateway.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.Appointment;
import training.iqgateway.entities.Notifications;
import training.iqgateway.entities.User;

@Repository
public interface NotificationRepository extends MongoRepository<Notifications, String> {

	// Custom query methods can be defined here if needed
	// For example, to find notifications by user or appointment
	List<Notifications> findByUser(User user);
	List<Notifications> findByAppointment(Appointment appointment);
	List<Notifications> findByIsRead(boolean isRead);
	List<Notifications> findByNotificationType(String notificationType);
	List<Notifications> findByUserAndIsRead(User user, boolean isRead);
//	findByUserIdAndIsReadOrderByCreatedAtDesc
	// Additional methods can be added as required

}

package training.iqgateway.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "notifications")
public class Notifications {

	@Id
	private String id;
	@DBRef
	private User user;
	@DBRef
	private Appointment appointment;
	@Field("notification_type")
	private String notificationType;
	@Field("title")
	private String title;
	@Field("message")
	private String message;
	@Field("is_read")
	private boolean isRead;
	@Field("scheduled_for")
	private LocalDateTime scheduledFor;
	@Field("sent_at")
	private LocalDateTime sentAt;
	@Field("created_at")
	private LocalDateTime createdAt;
	
	public Notifications() {
		// Default constructor
	}
	public Notifications(String id, User user, Appointment appointment, String notificationType, String title,
			String message, boolean isRead, LocalDateTime scheduledFor, LocalDateTime sentAt, LocalDateTime createdAt) {
		this.id = id;
		this.user = user;
		this.appointment = appointment;
		this.notificationType = notificationType;
		this.title = title;
		this.message = message;
		this.isRead = isRead;
		this.scheduledFor = scheduledFor;
		this.sentAt = sentAt;
		this.createdAt = createdAt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public LocalDateTime getScheduledFor() {
		return scheduledFor;
	}
	public void setScheduledFor(LocalDateTime scheduledFor) {
		this.scheduledFor = scheduledFor;
	}
	public LocalDateTime getSentAt() {
		return sentAt;
	}
	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
}

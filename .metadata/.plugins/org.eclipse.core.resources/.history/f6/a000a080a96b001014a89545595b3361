package training.iqgateway.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "doctor_availability")
public class DoctorAvailability {
	
	@Id
	private String id;
	@DBRef
	private Doctor doctor;
	@Field(name = "day_of_week")
	private String dayOfWeek;
	@Field(name = "start_time")
	private LocalDateTime startTime;
	@Field(name = "end_time")
	private LocalDateTime endTime;
	@Field(name = "is_available")
	private boolean isAvailable;
	@Field(name = "appointment_duration")
	private int appointmentDuration;
	public DoctorAvailability() {
		super();
	}
	public DoctorAvailability(String id, Doctor doctor, String dayOfWeek, LocalDateTime startTime,
			LocalDateTime endTime, boolean isAvailable, int appointmentDuration) {
		super();
		this.id = id;
		this.doctor = doctor;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isAvailable = isAvailable;
		this.appointmentDuration = appointmentDuration;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public int getAppointmentDuration() {
		return appointmentDuration;
	}
	public void setAppointmentDuration(int appointmentDuration) {
		this.appointmentDuration = appointmentDuration;
	}
	
}

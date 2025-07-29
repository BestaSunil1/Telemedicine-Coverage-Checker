package training.iqgateway.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DoctorAvailabilityResponseDto {
    private String id;
    private String doctorId;
    private String doctorName;
    private String doctorEmail;
    private List<String> doctorSpecializations;
    private String dayOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private int appointmentDuration;
    private boolean doctorActive;
    
    public DoctorAvailabilityResponseDto() {
	}

	public DoctorAvailabilityResponseDto(String id, String doctorId, String doctorName, String doctorEmail,
			List<String> doctorSpecializations, String dayOfWeek, LocalDateTime startTime, LocalDateTime endTime,
			boolean isAvailable, int appointmentDuration, boolean doctorActive) {
		super();
		this.id = id;
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.doctorEmail = doctorEmail;
		this.doctorSpecializations = doctorSpecializations;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isAvailable = isAvailable;
		this.appointmentDuration = appointmentDuration;
		this.doctorActive = doctorActive;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorEmail() {
		return doctorEmail;
	}

	public void setDoctorEmail(String doctorEmail) {
		this.doctorEmail = doctorEmail;
	}

	public List<String> getDoctorSpecializations() {
		return doctorSpecializations;
	}

	public void setDoctorSpecializations(List<String> doctorSpecializations) {
		this.doctorSpecializations = doctorSpecializations;
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

	public boolean isDoctorActive() {
		return doctorActive;
	}

	public void setDoctorActive(boolean doctorActive) {
		this.doctorActive = doctorActive;
	}
    
    
}
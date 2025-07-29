package training.iqgateway.dtos;

import java.time.LocalDateTime;

public class DoctorAvailabilityDto {
    private String id;
    private String doctorId;
    private String dayOfWeek;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable;
    private int appointmentDuration;
    
    public DoctorAvailabilityDto() {
	}

	public DoctorAvailabilityDto(String id, String doctorId, String dayOfWeek, LocalDateTime startTime,
			LocalDateTime endTime, boolean isAvailable, int appointmentDuration) {
		super();
		this.id = id;
		this.doctorId = doctorId;
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

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
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

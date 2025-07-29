package training.iqgateway.dtos;

import java.time.LocalDateTime;

public class AppointmentBookingRequest {
    private String patientId;
    private String doctorId;
    private LocalDateTime scheduledAt;
    private String insuranceCoverageStatus;
    private String videoSessionId;
    
    // Constructors
    public AppointmentBookingRequest() {}
    
    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    
    public String getInsuranceCoverageStatus() { return insuranceCoverageStatus; }
    public void setInsuranceCoverageStatus(String insuranceCoverageStatus) { this.insuranceCoverageStatus = insuranceCoverageStatus; }
    
    public String getVideoSessionId() { return videoSessionId; }
    public void setVideoSessionId(String videoSessionId) { this.videoSessionId = videoSessionId; }
}
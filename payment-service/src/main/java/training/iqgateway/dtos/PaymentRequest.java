package training.iqgateway.dtos;

import java.math.BigDecimal;

public class PaymentRequest {
    private String appointmentId;
    private String patientId;
    private BigDecimal amount;
    private String currency = "INR";
    
    // Constructors, getters, setters
    public PaymentRequest() {}
    
    public PaymentRequest(String appointmentId, String patientId, BigDecimal amount) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.amount = amount;
    }
    
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}

package training.iqgateway.dtos;

import java.math.BigDecimal;

import training.iqgateway.entities.PaymentStatus;

public class PaymentResponse {
    private String paymentId;
    private String razorpayOrderId;
    private String appointmentId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String message;
    
    public PaymentResponse() {}
    
    public PaymentResponse(String paymentId, String razorpayOrderId, String appointmentId, 
                          BigDecimal amount, String currency, PaymentStatus status, String message) {
        this.paymentId = paymentId;
        this.razorpayOrderId = razorpayOrderId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.message = message;
    }
    
    // Getters and Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    
    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }
    
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}


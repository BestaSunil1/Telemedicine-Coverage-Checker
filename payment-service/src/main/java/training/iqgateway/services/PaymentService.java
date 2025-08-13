package training.iqgateway.services; // Replace with your actual package

// Java Standard Library
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
// javax.xml.bind.DatatypeConverter removed in Java 11+ - using HexFormat instead

import org.json.JSONObject;
// Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Refund;


import training.iqgateway.dtos.PaymentRequest;
import training.iqgateway.dtos.PaymentResponse;
import training.iqgateway.dtos.PaymentVerificationRequest;
import training.iqgateway.entities.Appointment;
import training.iqgateway.entities.AppointmentStatus;
import training.iqgateway.entities.Patient;
import training.iqgateway.entities.BookingPayment;
import training.iqgateway.entities.PaymentMethod;
import training.iqgateway.entities.PaymentStatus;
import training.iqgateway.repositories.AppointmentRepository;
import training.iqgateway.repositories.PatientRepository;
import training.iqgateway.repositories.PaymentRepository;



@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private RazorpayClient razorpayClient;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    public PaymentResponse initiatePayment(PaymentRequest request) {
        try {
            // Validate appointment and patient
            Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
            
            // Check if payment already exists for this appointment
            Optional<BookingPayment> existingPayment = paymentRepository.findByAppointmentId(request.getAppointmentId());
            if (existingPayment.isPresent() && existingPayment.get().getStatus() == PaymentStatus.SUCCESS) {
                throw new RuntimeException("Payment already completed for this appointment");
            }
            
            // Create Razorpay order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount().multiply(BigDecimal.valueOf(100)).intValue()); // Convert to paise
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", "appointment_" + request.getAppointmentId());
            
            Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            
            // Create payment record
            BookingPayment payment = new BookingPayment();
            payment.setAppointment(appointment);
            payment.setPatient(patient);
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setPaymentMethod(PaymentMethod.RAZORPAY);
            payment.setStatus(PaymentStatus.PENDING);
            
            BookingPayment savedPayment = paymentRepository.save(payment);
            
            return new PaymentResponse(
                savedPayment.getId(),
                razorpayOrder.get("id"),
                request.getAppointmentId(),
                request.getAmount(),
                request.getCurrency(),
                PaymentStatus.PENDING,
                "Payment initiated successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error initiating payment: ", e);
            throw new RuntimeException("Failed to initiate payment: " + e.getMessage());
        }
    }
    
    public PaymentResponse verifyPayment(PaymentVerificationRequest request) {
        try {
            // Find payment by Razorpay order ID
            BookingPayment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
            
            // Verify signature
            boolean isSignatureValid = verifyRazorpaySignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
            );
            
            if (isSignatureValid) {
                // Update payment status
                payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
                payment.setRazorpaySignature(request.getRazorpaySignature());
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setProcessedAt(LocalDateTime.now());
                
                BookingPayment updatedPayment = paymentRepository.save(payment);
                
                // Update appointment status to confirmed
                Appointment appointment = payment.getAppointment();
                appointment.setStatus(AppointmentStatus.BOOKED);
                appointmentRepository.save(appointment);
                
                return new PaymentResponse(
                    updatedPayment.getId(),
                    request.getRazorpayOrderId(),
                    appointment.getId(),
                    payment.getAmount(),
                    payment.getCurrency(),
                    PaymentStatus.SUCCESS,
                    "Payment verified successfully"
                );
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                
                throw new RuntimeException("Payment verification failed");
            }
            
        } catch (Exception e) {
            logger.error("Error verifying payment: ", e);
            throw new RuntimeException("Failed to verify payment: " + e.getMessage());
        }
    }
    
    private boolean verifyRazorpaySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            String expectedSignature = hmacSha256(payload, razorpayKeySecret);
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            logger.error("Error verifying signature: ", e);
            return false;
        }
    }
    
    private String hmacSha256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes());
        
        // Convert bytes to hex string (Java 11+ compatible)
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    public List<BookingPayment> getPaymentsByPatient(String patientId) {
        return paymentRepository.findByPatientId(patientId);
    }
    
    public BookingPayment getPaymentByAppointment(String appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId)
            .orElseThrow(() -> new RuntimeException("Payment not found for appointment"));
    }
    
    public PaymentResponse refundPayment(String paymentId) {
        try {
            BookingPayment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
            
            if (payment.getStatus() != PaymentStatus.SUCCESS) {
                throw new RuntimeException("Cannot refund non-successful payment");
            }
            
            // Create refund request
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", payment.getAmount().multiply(BigDecimal.valueOf(100)).intValue());
            refundRequest.put("speed", "normal");
            
            Refund refund = razorpayClient.payments.refund(payment.getRazorpayPaymentId(), refundRequest);
            
            // Update payment status
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
            
            return new PaymentResponse(
                payment.getId(),
                payment.getRazorpayOrderId(),
                payment.getAppointment().getId(),
                payment.getAmount(),
                payment.getCurrency(),
                PaymentStatus.REFUNDED,
                "Payment refunded successfully"
            );
            
        } catch (Exception e) {
            logger.error("Error processing refund: ", e);
            throw new RuntimeException("Failed to process refund: " + e.getMessage());
        }
    }
}
package training.iqgateway.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.dtos.ErrorResponse;
import training.iqgateway.dtos.PaymentRequest;
import training.iqgateway.dtos.PaymentResponse;
import training.iqgateway.dtos.PaymentVerificationRequest;
import training.iqgateway.entities.BookingPayment;
import training.iqgateway.services.PaymentService;

@RestController
@RequestMapping("/api/payments")

public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = paymentService.initiatePayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error initiating payment: ", e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to initiate payment", e.getMessage()));
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        try {
            PaymentResponse response = paymentService.verifyPayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error verifying payment: ", e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Payment verification failed", e.getMessage()));
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientPayments(@PathVariable String patientId) {
        try {
            List<BookingPayment> payments = paymentService.getPaymentsByPatient(patientId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            logger.error("Error fetching patient payments: ", e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Failed to fetch payments", e.getMessage()));
        }
    }
    
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getAppointmentPayment(@PathVariable String appointmentId) {
        try {
            BookingPayment payment = paymentService.getPaymentByAppointment(appointmentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            logger.error("Error fetching appointment payment: ", e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Payment not found", e.getMessage()));
        }
    }
    
    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<?> refundPayment(@PathVariable String paymentId) {
        try {
            PaymentResponse response = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing refund: ", e);
            return ResponseEntity.badRequest().body(new ErrorResponse("Refund failed", e.getMessage()));
        }
    }
}

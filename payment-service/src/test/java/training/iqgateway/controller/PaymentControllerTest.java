package training.iqgateway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import training.iqgateway.dtos.PaymentRequest;
import training.iqgateway.dtos.PaymentResponse;
import training.iqgateway.dtos.PaymentVerificationRequest;
import training.iqgateway.entities.Appointment;
import training.iqgateway.entities.BookingPayment;
import training.iqgateway.entities.Patient;
import training.iqgateway.entities.PaymentStatus;
import training.iqgateway.services.PaymentService;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void initiatePayment_Success_ReturnsPaymentResponse() throws Exception {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setPatientId("patient123");
        request.setAppointmentId("appointment123");
        request.setCurrency("INR");

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId("payment123");
        response.setRazorpayOrderId("order_123");
        response.setAppointmentId("appointment123");
        response.setAmount(new BigDecimal("100.00"));
        response.setCurrency("INR");
        response.setStatus(PaymentStatus.PENDING);
        response.setMessage("Payment initiated successfully");

        when(paymentService.initiatePayment(any(PaymentRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/payments/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value("payment123"))
                .andExpect(jsonPath("$.razorpayOrderId").value("order_123"))
                .andExpect(jsonPath("$.appointmentId").value("appointment123"))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("Payment initiated successfully"));
    }

    @Test
    void initiatePayment_ServiceThrowsException_ReturnsBadRequest() throws Exception {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setAmount(new BigDecimal("100.00"));

        when(paymentService.initiatePayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException("Payment gateway error"));

        // Act & Assert
        mockMvc.perform(post("/api/payments/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payment gateway error"));
                
    }

    @Test
    void verifyPayment_Success_ReturnsPaymentResponse() throws Exception {
        // Arrange
        PaymentVerificationRequest request = new PaymentVerificationRequest();
        request.setRazorpayOrderId("order_123");
        request.setRazorpayPaymentId("pay_123");
        request.setRazorpaySignature("signature_123");

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId("payment123");
        response.setRazorpayOrderId("order_123");
        response.setAppointmentId("appointment123");
        response.setAmount(new BigDecimal("100.00"));
        response.setCurrency("INR");
        response.setStatus(PaymentStatus.SUCCESS);
        response.setMessage("Payment verified successfully");

        when(paymentService.verifyPayment(any(PaymentVerificationRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/payments/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value("payment123"))
                .andExpect(jsonPath("$.razorpayOrderId").value("order_123"))
                .andExpect(jsonPath("$.appointmentId").value("appointment123"))
                .andExpectAll(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.message").value("Payment verified successfully"));
    }

    @Test
    void verifyPayment_ServiceThrowsException_ReturnsBadRequest() throws Exception {
        // Arrange
        PaymentVerificationRequest request = new PaymentVerificationRequest();
        request.setRazorpayOrderId("order_123");
        request.setRazorpayPaymentId("pay_123");
        request.setRazorpaySignature("invalid_signature");

        when(paymentService.verifyPayment(any(PaymentVerificationRequest.class)))
                .thenThrow(new RuntimeException("Signature verification failed"));

        // Act & Assert
        mockMvc.perform(post("/api/payments/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
    
                .andExpect(jsonPath("$.message").value("Signature verification failed"));
    }

    @Test
    void getPatientPayments_Success_ReturnsPaymentsList() throws Exception {
        // Arrange
        BookingPayment payment1 = new BookingPayment();
        payment1.setId("payment1");
        Patient patient1 = new Patient();
        patient1.setId("patient123");
        payment1.setPatient(patient1);
        Appointment appointment1 = new Appointment();
        payment1.setAppointment(appointment1);
        payment1.setAmount(new BigDecimal("100.00"));
        payment1.setStatus(PaymentStatus.SUCCESS);

        BookingPayment payment2 = new BookingPayment();
        payment2.setId("payment2");
        Patient patient2 = new Patient();
        patient2.setId("patient123");
        payment2.setPatient(patient2);
        Appointment appointment2 = new Appointment();
        appointment2.setId("appointment2");
        payment2.setAppointment(appointment2);
        payment2.setAmount(new BigDecimal("150.00"));
        payment2.setStatus(PaymentStatus.PENDING);

        List<BookingPayment> payments = Arrays.asList(payment1, payment2);

        when(paymentService.getPaymentsByPatient(eq("patient123")))
                .thenReturn(payments);

        // Act & Assert
        mockMvc.perform(get("/api/payments/patient/{patientId}", "patient123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("payment1"))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[0].status").value("SUCCESS"))
                .andExpect(jsonPath("$[1].id").value("payment2"))
                .andExpect(jsonPath("$[1].amount").value(150.00))
                .andExpect(jsonPath("$[1].status").value("PENDING"));
    }

    @Test
    void getPatientPayments_ServiceThrowsException_ReturnsBadRequest() throws Exception {
        // Arrange
        when(paymentService.getPaymentsByPatient(eq("patient123")))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/api/payments/patient/{patientId}", "patient123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Database error"));

    }

    @Test
    void getAppointmentPayment_Success_ReturnsBookingPayment() throws Exception {
        // Arrange
        BookingPayment payment = new BookingPayment();
        payment.setId("payment123");
        Appointment appointment = new Appointment();
        appointment.setId("appointment123");
        payment.setAppointment(appointment);
        Patient patient = new Patient();
        patient.setId("patient456");
        payment.setPatient(patient);
        payment.setAmount(new BigDecimal("200.00"));
        payment.setStatus(PaymentStatus.SUCCESS);

        when(paymentService.getPaymentByAppointment(eq("appointment123")))
                .thenReturn(payment);

        // Act & Assert
        mockMvc.perform(get("/api/payments/appointment/{appointmentId}", "appointment123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("payment123"))
                .andExpect(jsonPath("$.appointment.id").value("appointment123"))
                .andExpect(jsonPath("$.amount").value(200.00))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void getAppointmentPayment_ServiceThrowsException_ReturnsBadRequest() throws Exception {
        // Arrange
        when(paymentService.getPaymentByAppointment(eq("appointment123")))
                .thenThrow(new RuntimeException("Payment not found"));

        // Act & Assert
        mockMvc.perform(get("/api/payments/appointment/{appointmentId}", "appointment123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payment not found"));



    }

    @Test
    void refundPayment_Success_ReturnsPaymentResponse() throws Exception {
        // Arrange
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId("payment123");
        response.setRazorpayOrderId("order_123");
        response.setAppointmentId("appointment123");
        response.setAmount(new BigDecimal("100.00"));
        response.setCurrency("INR");
        response.setStatus(PaymentStatus.REFUNDED);
        response.setMessage("Payment refunded successfully");

        when(paymentService.refundPayment(eq("payment123")))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/payments/refund/{paymentId}", "payment123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value("payment123"))
                .andExpect(jsonPath("$.razorpayOrderId").value("order_123"))
                .andExpect(jsonPath("$.appointmentId").value("appointment123"))
                .andExpect(jsonPath("$.status").value("REFUNDED"))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.message").value("Payment refunded successfully"));
    }

    @Test
    void refundPayment_ServiceThrowsException_ReturnsBadRequest() throws Exception {
        // Arrange
        when(paymentService.refundPayment(eq("payment123")))
                .thenThrow(new RuntimeException("Refund processing error"));

        // Act & Assert
        mockMvc.perform(post("/api/payments/refund/{paymentId}", "payment123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Refund processing error"));

    }

    @Test
    void getPatientPayments_EmptyList_ReturnsEmptyArray() throws Exception {
        // Arrange
        when(paymentService.getPaymentsByPatient(eq("patient456")))
                .thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/payments/patient/{patientId}", "patient456")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
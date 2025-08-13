package training.iqgateway.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import training.iqgateway.dtos.AppointmentBookingRequest;
import training.iqgateway.dtos.AppointmentResponse;
import training.iqgateway.entities.Appointment;
import training.iqgateway.entities.Notifications;
import training.iqgateway.services.AppointmentService;
import training.iqgateway.services.NotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.List;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private NotificationService notificationService;

    @Test
    void testBookAppointment_Success() throws Exception {
        AppointmentBookingRequest req = new AppointmentBookingRequest();
        AppointmentResponse resp = new AppointmentResponse("app1", "Booked successfully");

        when(appointmentService.bookAppointment(any(AppointmentBookingRequest.class)))
                .thenReturn(resp);

        mockMvc.perform(post("/api/appointments/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentId").value("app1"));
    }

    @Test
    void testAcceptAppointment() throws Exception {
        AppointmentResponse resp = new AppointmentResponse("app1", "Accepted");
        when(appointmentService.acceptAppointment(eq("app1"), eq("doc1"))).thenReturn(resp);

        mockMvc.perform(put("/api/appointments/app1/accept")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("doctorId", "doc1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Accepted"));
    }

    @Test
    void testRejectAppointment() throws Exception {
        AppointmentResponse resp = new AppointmentResponse("app1", "Rejected");
        when(appointmentService.rejectAppointment(eq("app1"), eq("doc1"), eq("Busy")))
                .thenReturn(resp);

        mockMvc.perform(put("/api/appointments/app1/reject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "doctorId", "doc1",
                                "reason", "Busy"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rejected"));
    }

    @Test
    void testGetPendingAppointments() throws Exception {
        Appointment a = new Appointment();
        a.setId("a1");
        when(appointmentService.getPendingAppointments("doc1")).thenReturn(List.of(a));

        mockMvc.perform(get("/api/appointments/pending/doc1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("a1"));
    }

    @Test
    void testGetAvailableSlots() throws Exception {
        List<LocalDateTime> slots = List.of(LocalDateTime.of(2025, 8, 10, 10, 0));
        when(appointmentService.getAvailableSlots("doc1", LocalDate.parse("2025-08-10")))
                .thenReturn(slots);

        mockMvc.perform(get("/api/appointments/availableslots/doc1/2025-08-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testGetAppointmentDetails_Found() throws Exception {
        Appointment a = new Appointment();
        a.setId("a1");
        when(appointmentService.getAppointmentDetails("a1")).thenReturn(a);

        mockMvc.perform(get("/api/appointments/a1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("a1"));
    }

    @Test
    void testGetNotificationsForUser() throws Exception {
        Notifications n = new Notifications();
        n.setId("n1");
        when(notificationService.getNotificationsForUser("u1")).thenReturn(List.of(n));

        mockMvc.perform(get("/api/appointments/getNotifications/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("n1"));
    }

    @Test
    void testGetAllAppointments() throws Exception {
        Appointment a = new Appointment();
        a.setId("a1");
        when(appointmentService.getAllAppointments()).thenReturn(List.of(a));

        mockMvc.perform(get("/api/appointments/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("a1"));
    }

    @Test
    void testUpdateReadStatus_Found() throws Exception {
        Notifications n = new Notifications();
        n.setId("n1");
        when(notificationService.updateRead("n1", true)).thenReturn(n);

        mockMvc.perform(put("/api/appointments/n1/read?read=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("n1"));
    }

    @Test
    void testGetConfirmedAppointmentsDoctor() throws Exception {
        Appointment a = new Appointment();
        a.setId("a1");
        when(appointmentService.getConfirmedAppointmentsDoctor("doc1")).thenReturn(List.of(a));

        mockMvc.perform(get("/api/appointments/confirmed/doc1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("a1"));
    }

    @Test
    void testGetConfirmedAppointmentsPatient() throws Exception {
        Appointment a = new Appointment();
        a.setId("a1");
        when(appointmentService.getConfirmedAppointmentsPatinet("p1")).thenReturn(List.of(a));

        mockMvc.perform(get("/api/appointments/patient/conform/p1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("a1"));
    }
}


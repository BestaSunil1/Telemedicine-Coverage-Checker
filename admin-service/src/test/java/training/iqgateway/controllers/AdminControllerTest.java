package training.iqgateway.controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import training.iqgateway.dtos.DoctorDto;
import training.iqgateway.dtos.DoctorRegistrationDto;
import training.iqgateway.dtos.PatientRegistrationDTO;
import training.iqgateway.entities.Patient;
import training.iqgateway.entities.User;
import training.iqgateway.services.AdminService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For JSON conversion

    @MockBean
    private AdminService adminService;

    @Test
    void testRegisterDoctor_Success() throws Exception {
        DoctorRegistrationDto registrationDto = new DoctorRegistrationDto();
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId("doc123");

        when(adminService.registerDoctor(any(DoctorRegistrationDto.class))).thenReturn(doctorDto);

        mockMvc.perform(post("/api/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("doc123"));
    }

    @Test
    void testGetAllDoctors() throws Exception {
        DoctorDto d1 = new DoctorDto();
        d1.setId("doc1");
        DoctorDto d2 = new DoctorDto();
        d2.setId("doc2");

        when(adminService.getAllDoctors()).thenReturn(Arrays.asList(d1, d2));

        mockMvc.perform(get("/api/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetDoctorById_Found() throws Exception {
        DoctorDto doctor = new DoctorDto();
        doctor.setId("doc1");
        when(adminService.getDoctorById("doc1")).thenReturn(Optional.of(doctor));

        mockMvc.perform(get("/api/admin/doc1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("doc1"));
    }

    @Test
    void testGetDoctorById_NotFound() throws Exception {
        when(adminService.getDoctorById("123")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Doctor not found"));
    }

    @Test
    void testDeleteDoctor_Success() throws Exception {
        Mockito.doNothing().when(adminService).deleteDoctor("doc1");

        mockMvc.perform(delete("/api/admin/doc1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor deleted successfully"));
    }

    @Test
    void testRegisterPatient_Success() throws Exception {
        PatientRegistrationDTO dto = new PatientRegistrationDTO();
        Patient patient = new Patient();
        patient.setId("p1");

        when(adminService.registerPatient(any(PatientRegistrationDTO.class))).thenReturn(patient);

        mockMvc.perform(post("/api/admin/register/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("p1"));
    }

    @Test
    void testGetPatientById_Found() throws Exception {
        Patient patient = new Patient();
        patient.setId("p1");
        when(adminService.getPatientById("p1")).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/admin/patient/p1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("p1"));
    }

    @Test
    void testGetAdminByUserId_Found() throws Exception {
        User user = new User();
        user.setId("u1");
        when(adminService.getUserById("u1")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/admin/user/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("u1"));
    }
}


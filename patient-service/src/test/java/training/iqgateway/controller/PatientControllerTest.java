package training.iqgateway.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import training.iqgateway.contollers.PatientController;
import training.iqgateway.entities.Patient;
import training.iqgateway.entities.User;
import training.iqgateway.services.PatientService;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getPatientByUserId_Found_ReturnsPatient() throws Exception {
        // Arrange test user
        User user = new User();
        user.setId("user123");
        // Set other user properties as needed
        
        // Arrange test patient
        Patient patient = new Patient();
        patient.setId("p1");
        patient.setUser(user);
        patient.setDateOfBirth("1995-04-04"); // String format as per entity
        patient.setGender("male");
        patient.setContactNumber("1234567890");
        patient.setProfilePhoto("profile.jpg");

        Mockito.when(patientService.getPatientByUserId(eq("user123")))
                .thenReturn(Optional.of(patient));

        // Act & Assert
        mockMvc.perform(get("/api/patients/user/{userId}", "user123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("p1"))
                .andExpect(jsonPath("$.dateOfBirth").value("1995-04-04"))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.contactNumber").value("1234567890"))
                .andExpectAll(jsonPath("$.profilePhoto").value("profile.jpg"))
                .andExpect(jsonPath("$.user.id").value("user123")); // Verify nested user object
    }

    @Test
    void getPatientByUserId_NotFound_Returns404() throws Exception {
        Mockito.when(patientService.getPatientByUserId(eq("unknownUser")))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/patients/user/{userId}", "unknownUser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getPatientByUserId_WithMinimalData_ReturnsPatient() throws Exception {
        // Test with minimal required data
        User user = new User();
        user.setId("user456");
        
        Patient patient = new Patient();
        patient.setId("p2");
        patient.setUser(user);
        patient.setDateOfBirth("1990-01-01");
        patient.setGender("female");
        // contactNumber and profilePhoto can be null

        Mockito.when(patientService.getPatientByUserId(eq("user456")))
                .thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/user/{userId}", "user456")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("p2"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.gender").value("female"))
                .andExpect(jsonPath("$.user.id").value("user456"));
    }
}
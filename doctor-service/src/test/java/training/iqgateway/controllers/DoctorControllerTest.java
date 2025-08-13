//package training.iqgateway.controllers;
//
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import training.iqgateway.dto.DoctorAvailabilityResponseDto;
//import training.iqgateway.entities.Doctor;
//import training.iqgateway.services.DoctorService;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(DoctorController.class)
//class DoctorControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private DoctorService doctorService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testGetAllDoctorAvailabilities() throws Exception {
//        DoctorAvailabilityResponseDto dto1 = new DoctorAvailabilityResponseDto();
//        dto1.setDoctorId("doc1");
//        dto1.setDayOfWeek("Monday");
//
//        DoctorAvailabilityResponseDto dto2 = new DoctorAvailabilityResponseDto();
//        dto2.setDoctorId("doc2");
//        dto2.setDayOfWeek("Tuesday");
//
//        List<DoctorAvailabilityResponseDto> list = Arrays.asList(dto1, dto2);
//        when(doctorService.getAllDoctorAvailabilities()).thenReturn(list);
//
//        mockMvc.perform(get("/api/doctors/availabilities")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].doctorId").value("doc1"))
//                .andExpect(jsonPath("$[1].dayOfWeek").value("Tuesday"));
//    }
//
//    @Test
//    void testGetAllAvailableDoctorAvailabilities() throws Exception {
//        DoctorAvailabilityResponseDto dto = new DoctorAvailabilityResponseDto();
//        dto.setDoctorId("doc3");
//        dto.setDayOfWeek("Friday");
//
//        when(doctorService.getAllAvailableDoctorAvailabilities()).thenReturn(List.of(dto));
//
//        mockMvc.perform(get("/api/doctors/availabilities/available")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].doctorId").value("doc3"));
//    }
//
//    @Test
//    void testGetDoctorAvailabilitiesByDay() throws Exception {
//        DoctorAvailabilityResponseDto dto = new DoctorAvailabilityResponseDto();
//        dto.setDoctorId("doc4");
//        dto.setDayOfWeek("Wednesday");
//
//        when(doctorService.getDoctorAvailabilitiesByDay("Wednesday"))
//                .thenReturn(List.of(dto));
//
//        mockMvc.perform(get("/api/doctors/availabilities/day/Wednesday")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].dayOfWeek").value("Wednesday"));
//    }
//
//    @Test
//    void testGetByUserId_Found() throws Exception {
//        Doctor doctor = new Doctor();
//        doctor.setId("doc5");
//        doctor.setUser("Dr. Priya");
//
//        when(doctorService.getByUserId("u1")).thenReturn(doctor);
//
//        mockMvc.perform(get("/api/doctors/user/u1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("doc5"))
//                .andExpect(jsonPath("$.name").value("Dr. Smith"));
//    }
//
//    @Test
//    void testGetByUserId_NotFound() throws Exception {
//        when(doctorService.getByUserId("u2")).thenReturn(null);
//
//        mockMvc.perform(get("/api/doctors/user/u2"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Doctor not found"));
//    }
//
//    @Test
//    void testUpdateDoctor_Found() throws Exception {
//        Doctor doctor = new Doctor();
//        doctor.setId("doc6");
//        doctor.setName("Updated Doctor");
//
//        when(doctorService.updateDoctor(eq("doc6"), any(Doctor.class))).thenReturn(doctor);
//
//        mockMvc.perform(post("/api/doctors/doc6/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(doctor)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Doctor"));
//    }
//
//    @Test
//    void testUpdateDoctor_NotFound() throws Exception {
//        when(doctorService.updateDoctor(eq("doc7"), any(Doctor.class))).thenReturn(null);
//
//        mockMvc.perform(post("/api/doctors/doc7/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new Doctor())))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testGetAllDoctors() throws Exception {
//        Doctor d1 = new Doctor();
//        d1.setId("d1");
//        d1.setName("Doctor One");
//
//        Doctor d2 = new Doctor();
//        d2.setId("d2");
//        d2.setName("Doctor Two");
//
//        when(doctorService.getAllDoctors()).thenReturn(Arrays.asList(d1, d2));
//
//        mockMvc.perform(get("/api/doctors/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[1].name").value("Doctor Two"));
//    }
//}


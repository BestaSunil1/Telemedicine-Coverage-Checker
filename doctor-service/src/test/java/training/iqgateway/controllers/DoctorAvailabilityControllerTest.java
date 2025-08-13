package training.iqgateway.controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import training.iqgateway.dto.DoctorAvailabilityResponseDto;
import training.iqgateway.services.DoctorService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorAvailabilityController.class)
class DoctorAvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllDoctorAvailabilities() throws Exception {
        DoctorAvailabilityResponseDto dto1 = new DoctorAvailabilityResponseDto();
        dto1.setDoctorId("doc1");
        dto1.setDayOfWeek("Monday");

        DoctorAvailabilityResponseDto dto2 = new DoctorAvailabilityResponseDto();
        dto2.setDoctorId("doc2");
        dto2.setDayOfWeek("Tuesday");

        List<DoctorAvailabilityResponseDto> mockList = Arrays.asList(dto1, dto2);

        when(doctorService.getAllDoctorAvailabilities()).thenReturn(mockList);

        mockMvc.perform(get("/api/availabilities")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].doctorId").value("doc1"))
                .andExpect(jsonPath("$[1].doctorId").value("doc2"));
    }

    @Test
    void testGetAllAvailableDoctorAvailabilities() throws Exception {
        DoctorAvailabilityResponseDto dto = new DoctorAvailabilityResponseDto();
        dto.setDoctorId("doc3");
        dto.setDayOfWeek("Wednesday");

        when(doctorService.getAllAvailableDoctorAvailabilities()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/availabilities/available")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].doctorId").value("doc3"))
                .andExpect(jsonPath("$[0].dayOfWeek").value("Wednesday"));
    }

    @Test
    void testGetDoctorAvailabilitiesByDay() throws Exception {
        DoctorAvailabilityResponseDto dto = new DoctorAvailabilityResponseDto();
        dto.setDoctorId("doc4");
        dto.setDayOfWeek("Friday");

        when(doctorService.getDoctorAvailabilitiesByDay("Friday")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/availabilities/day/Friday")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].doctorId").value("doc4"))
                .andExpect(jsonPath("$[0].dayOfWeek").value("Friday"));
    }
}

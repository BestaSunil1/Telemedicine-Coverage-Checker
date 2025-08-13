package training.iqgateway.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import training.iqgateway.dtos.EligibilityCheckRequest;
import training.iqgateway.dtos.EligibilityCheckResponse;
import training.iqgateway.dtos.PatientInsuranceDTO;
import training.iqgateway.entities.EligibilityStatus;
import training.iqgateway.entities.InsuranceEligibility;
import training.iqgateway.entities.InsurancePlan;
import training.iqgateway.service.InsuranceEligibilityService;

@WebMvcTest(InsuranceEligibilityController.class)
@AutoConfigureMockMvc(addFilters = false) // disables security filters for testing
class InsuranceEligibilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InsuranceEligibilityService eligibilityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCheckEligibility_eligible() throws Exception {
        EligibilityCheckRequest req = new EligibilityCheckRequest("p1", "plan1", null);
        EligibilityCheckResponse resp = new EligibilityCheckResponse(null, null, null, null, null, null, null, null, null, true, "Eligible", null);

        Mockito.when(eligibilityService.checkEligibility(any())).thenReturn(resp);

        mockMvc.perform(post("/api/eligibility/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eligibleForBooking").value(true));
    }

    @Test
    void testCheckEligibility_notEligible() throws Exception {
        EligibilityCheckResponse resp = new EligibilityCheckResponse(null, null, null, null, null, null, null, null, null, false, "Not eligible", null);

        Mockito.when(eligibilityService.checkEligibility(any())).thenReturn(resp);

        mockMvc.perform(post("/api/eligibility/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new EligibilityCheckRequest("p1", "plan1", null))))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.eligibleForBooking").value(false));
    }

    @Test
    void testGetPatientInsurancePlans() throws Exception {
        PatientInsuranceDTO dto = new PatientInsuranceDTO();
        Mockito.when(eligibilityService.getPatientInsurancePlans("p1"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/eligibility/patient/p1/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testVerifyEligibility() throws Exception {
        EligibilityCheckResponse resp = new EligibilityCheckResponse(null, null, null, null, null, null, null, null, null, true, "Verified", null);

        Mockito.when(eligibilityService.verifyEligibility(eq("elig1"), eq(EligibilityStatus.VERIFIED), any()))
                .thenReturn(resp);

        mockMvc.perform(put("/api/eligibility/verify/elig1")
                        .param("status", "VERIFIED")
                        .param("benefitDetails", "Full Cover"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Verified"));
    }

    @Test
    void testCreateEligibilityRecord_success() throws Exception {
        InsuranceEligibility e = new InsuranceEligibility(
                "elig1", null, null, EligibilityStatus.PENDING,
                LocalDateTime.now(), "some details");

        Mockito.when(eligibilityService.createEligibilityRecord("p1", "plan1")).thenReturn(e);

        mockMvc.perform(post("/api/eligibility/create")
                        .param("patientId", "p1")
                        .param("planId", "plan1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("elig1"));
    }

    @Test
    void testCreateEligibilityRecord_error() throws Exception {
        Mockito.when(eligibilityService.createEligibilityRecord(any(), any()))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/eligibility/create")
                        .param("patientId", "p1")
                        .param("planId", "plan1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCheckEligibilityBeforeBooking_eligible() throws Exception {
        EligibilityCheckResponse resp = new EligibilityCheckResponse(null, null, null, null, null, null, null, null, null, true, "Eligible", null);

        Mockito.when(eligibilityService.checkEligibility(any())).thenReturn(resp);

        mockMvc.perform(get("/api/eligibility/check-before-booking")
                        .param("patientId", "p1")
                        .param("insurancePlanId", "plan1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.canBookAppointment").value(true))
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void testCheckEligibilityBeforeBooking_notEligible() throws Exception {
        EligibilityCheckResponse resp = new EligibilityCheckResponse(null, null, null, null, null, null, null, null, null, false, "Not eligible", null);

        Mockito.when(eligibilityService.checkEligibility(any())).thenReturn(resp);

        mockMvc.perform(get("/api/eligibility/check-before-booking")
                        .param("patientId", "p1")
                        .param("insurancePlanId", "plan1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.canBookAppointment").value(false))
                .andExpect(jsonPath("$.statusCode").value(403));
    }

    @Test
    void testGetAllEligibilityRecords_nonEmpty() throws Exception {
        InsuranceEligibility e = new InsuranceEligibility();
        e.setId("elig1");

        Mockito.when(eligibilityService.getAllEligibilities()).thenReturn(List.of(e));

        mockMvc.perform(get("/api/eligibility/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("elig1"));
    }

    @Test
    void testGetAllEligibilityRecords_empty() throws Exception {
        Mockito.when(eligibilityService.getAllEligibilities()).thenReturn(List.of());

        mockMvc.perform(get("/api/eligibility/all"))
                .andExpect(status().isNoContent());
    }

//    @Test
//    void testGetAllInsurancePlans_nonEmpty() throws Exception {
//        InsurancePlan plan = new InsurancePlan();
//        plan.setId("plan1");
//
//        Mockito.when(eligibilityService.getAllInsurancePlans()).thenReturn(List.of(plan));
//
//        mockMvc.perform(get("/api/eligibility/getAllInsurancePlans"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].planId").value("plan1"));
//    }

    @Test
    void testGetAllInsurancePlans_empty() throws Exception {
        Mockito.when(eligibilityService.getAllInsurancePlans()).thenReturn(List.of());

        mockMvc.perform(get("/api/eligibility/getAllInsurancePlans"))
                .andExpect(status().isNoContent());
    }
}

package training.iqgateway.dtos;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import training.iqgateway.entities.EligibilityStatus;
import training.iqgateway.entities.InsuranceCoverageStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityCheckResponse {
    private String patientId;
    private String patientName;
    private String insurancePlanId;
    private String planName;
    private String planProvider;
    private EligibilityStatus eligibilityStatus;
    private InsuranceCoverageStatus coverageStatus;
    private String benefitDetails;
    private LocalDateTime verifiedAt;
    private boolean isEligibleForBooking;
    private String message;
    
    @JsonProperty("coverages")
    private List<String> coverages;
}
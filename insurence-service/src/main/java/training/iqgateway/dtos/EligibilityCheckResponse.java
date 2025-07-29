package training.iqgateway.dtos;

import java.time.LocalDateTime;

import lombok.Builder;
import training.iqgateway.entities.EligibilityStatus;
import training.iqgateway.entities.InsuranceCoverageStatus;

@Builder
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
    
    public EligibilityCheckResponse() {
	}
    
    public EligibilityCheckResponse(String patientId, String patientName, String insurancePlanId, String planName,
			String planProvider, EligibilityStatus eligibilityStatus, InsuranceCoverageStatus coverageStatus,
			String benefitDetails, LocalDateTime verifiedAt, boolean isEligibleForBooking, String message) {
		this.patientId = patientId;
		this.patientName = patientName;
		this.insurancePlanId = insurancePlanId;
		this.planName = planName;
		this.planProvider = planProvider;
		this.eligibilityStatus = eligibilityStatus;
		this.coverageStatus = coverageStatus;
		this.benefitDetails = benefitDetails;
		this.verifiedAt = verifiedAt;
		this.isEligibleForBooking = isEligibleForBooking;
		this.message = message;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getInsurancePlanId() {
		return insurancePlanId;
	}

	public void setInsurancePlanId(String insurancePlanId) {
		this.insurancePlanId = insurancePlanId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanProvider() {
		return planProvider;
	}

	public void setPlanProvider(String planProvider) {
		this.planProvider = planProvider;
	}

	public EligibilityStatus getEligibilityStatus() {
		return eligibilityStatus;
	}

	public void setEligibilityStatus(EligibilityStatus eligibilityStatus) {
		this.eligibilityStatus = eligibilityStatus;
	}

	public InsuranceCoverageStatus getCoverageStatus() {
		return coverageStatus;
	}

	public void setCoverageStatus(InsuranceCoverageStatus coverageStatus) {
		this.coverageStatus = coverageStatus;
	}

	public String getBenefitDetails() {
		return benefitDetails;
	}

	public void setBenefitDetails(String benefitDetails) {
		this.benefitDetails = benefitDetails;
	}

	public LocalDateTime getVerifiedAt() {
		return verifiedAt;
	}

	public void setVerifiedAt(LocalDateTime verifiedAt) {
		this.verifiedAt = verifiedAt;
	}

	public boolean isEligibleForBooking() {
		return isEligibleForBooking;
	}

	public void setEligibleForBooking(boolean isEligibleForBooking) {
		this.isEligibleForBooking = isEligibleForBooking;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
}

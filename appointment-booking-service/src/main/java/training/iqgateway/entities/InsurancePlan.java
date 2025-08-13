package training.iqgateway.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "insurance_plans")
public class InsurancePlan {
	
	@Id
	private String id;
	@Field("plan_provider")
    private String planProvider;
	@Field("plan_name")
    private String planName;
	@Field("plan_type")
    private String planType;
	@Field("plan_description")
    private String planDescription;
	@Field("plan_cost")
    private Integer planCost;
	@Field("is_active")
    private boolean isActive;
	@Field("coverages")
    private List<String> coverages = new java.util.ArrayList<>(); 

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// Default constructor
    public InsurancePlan() {
    }

    // Parameterized constructor
    public InsurancePlan(String planProvider, String planName, String planType,
                               String planDescription, Integer planCost, boolean isActive,
                               List<String> coverages) {
        this.planProvider = planProvider;
        this.planName = planName;
        this.planType = planType;
        this.planDescription = planDescription;
        this.planCost = planCost;
        this.isActive = isActive;
        this.coverages = coverages;
    }

    // Getters and Setters

    public String getPlanProvider() {
        return planProvider;
    }

    public void setPlanProvider(String planProvider) {
        this.planProvider = planProvider;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public Integer getPlanCost() {
        return planCost;
    }

    public void setPlanCost(Integer planCost) {
        this.planCost = planCost;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<String> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<String> coverages) {
        this.coverages = coverages;
    }

    @Override
    public String toString() {
        return "HealthInsurancePlan{" +
                "planProvider='" + planProvider + '\'' +
                ", planName='" + planName + '\'' +
                ", planType='" + planType + '\'' +
                ", planDescription='" + planDescription + '\'' +
                ", planCost=" + planCost +
                ", isActive=" + isActive +
                ", coverages=" + coverages +
                '}';
    }
}

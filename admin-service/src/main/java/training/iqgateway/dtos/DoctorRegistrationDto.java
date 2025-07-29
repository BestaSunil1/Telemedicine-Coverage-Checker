package training.iqgateway.dtos;

import java.util.List;

import training.iqgateway.entities.Role;

public class DoctorRegistrationDto {
	
	private String username;
    private String email;
    private String password;
    private Role role =  Role.DOCTOR; // Default role for doctor registration
    
    // Doctor details
    private String profilePictureBase64;
    private boolean active = true;
    private List<String> specializations;
    
    // Availability details
    private List<DoctorAvailabilityDto> availabilities;
    
    public DoctorRegistrationDto() {
		// Default constructor
	}

	public DoctorRegistrationDto(String username, String email, String password, Role role,
			String profilePictureBase64, boolean active, List<String> specializations,
			List<DoctorAvailabilityDto> availabilities) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.profilePictureBase64 = profilePictureBase64;
		this.active = active;
		this.specializations = specializations;
		this.availabilities = availabilities;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getProfilePictureBase64() {
		return profilePictureBase64;
	}

	public void setProfilePictureBase64(String profilePictureBase64) {
		this.profilePictureBase64 = profilePictureBase64;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<String> getSpecializations() {
		return specializations;
	}

	public void setSpecializations(List<String> specializations) {
		this.specializations = specializations;
	}

	public List<DoctorAvailabilityDto> getAvailabilities() {
		return availabilities;
	}

	public void setAvailabilities(List<DoctorAvailabilityDto> availabilities) {
		this.availabilities = availabilities;
	}
    
    
}

package training.iqgateway.dtos;

import java.util.List;

public class DoctorDto {
    private String id;
    private UserDto user;
    private String profilePictureBase64; // Base64 encoded profile picture
    private boolean active = true;
    private List<String> specializations;
    private List<DoctorAvailabilityDto> availabilities;
    
    public DoctorDto() {
		// Default constructor
	}

	public DoctorDto(String id, UserDto user, String profilePictureBase64, boolean active, List<String> specializations,
			List<DoctorAvailabilityDto> availabilities) {
		super();
		this.id = id;
		this.user = user;
		this.profilePictureBase64 = profilePictureBase64;
		this.active = active;
		this.specializations = specializations;
		this.availabilities = availabilities;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
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

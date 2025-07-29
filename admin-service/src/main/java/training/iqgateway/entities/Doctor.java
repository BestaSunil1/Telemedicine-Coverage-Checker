package training.iqgateway.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "doctors")
public class Doctor {
	
	@Id
	private String id;
	@DBRef(lazy = false)
	private User user;
	private byte[] profilePicture; 
	private boolean active = true;
	private List<String> specializations;
	
	public Doctor() {
	}
	
	public Doctor(String id, User user, byte[] profilePicture, boolean active, List<String> specializations) {
		this.id = id;
		this.user = user;
		this.profilePicture = profilePicture;
		this.active = active;
		this.specializations = specializations;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
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
}

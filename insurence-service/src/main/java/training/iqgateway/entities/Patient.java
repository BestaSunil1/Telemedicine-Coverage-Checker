package training.iqgateway.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection  = "patients")
public class Patient {
	@Id
	private String id;
	@DBRef
	private User user;
	private String dateOfBirth;
	private String gender;
	private String contactNumber;
	
	public Patient() {
		
	}

	public Patient(String id, User user, String dateOfBirth, String gender, String contactNumber) {
		super();
		this.id = id;
		this.user = user;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.contactNumber = contactNumber;
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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	@Override
	public String toString() {
		return "Patient [id=" + id + ", user=" + user + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender
				+ ", contactNumber=" + contactNumber + "]";
	}	
	
				
}

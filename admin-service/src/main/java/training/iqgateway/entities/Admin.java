package training.iqgateway.entities;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="admin")
public class Admin {
	@Id
	private String id;
	@DBRef
	private User user;
	private String dateOfBirth;
	private String gender;
	private String contactNumber;
	
	private String profilePhoto;
	
	public Admin(String id, User user, String dateOfBirth, String gender, String contactNumber, String profilePhoto) {
		super();
		this.id = id;
		this.user = user;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.contactNumber = contactNumber;
		this.profilePhoto = profilePhoto;
	}

	public Admin() {
		
	}

	public Admin(String id, User user, String dateOfBirth, String gender, String contactNumber) {
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

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}	
	
	
}


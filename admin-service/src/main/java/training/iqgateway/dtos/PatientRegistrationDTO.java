package training.iqgateway.dtos;



import training.iqgateway.entities.Role;

public class PatientRegistrationDTO {
    
    // User details
    private String username;
    private String email;
    private String password;
    private Role role;
    
    // Patient specific details
    private String dateOfBirth;
    private String gender;
    private String contactNumber;
    
    public PatientRegistrationDTO() {
    }
    
    public PatientRegistrationDTO(String username, String email, String password, Role role, 
                                String dateOfBirth, String gender, String contactNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactNumber = contactNumber;
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
}
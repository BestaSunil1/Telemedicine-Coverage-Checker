package training.iqgateway.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import training.iqgateway.entities.Patient;
import training.iqgateway.repositories.PatientRepository;

@Service
public class PatientService {

	@Autowired
	private  PatientRepository patientRepository;
	
	public Patient createPatient(Patient patient) {
		return patientRepository.
				save(patient);
	}
	
	public Optional<Patient> getPatientById(String id) {
		return patientRepository.findById(id);
	}
	public Patient updatePatient(String id, Patient updatedPatient) {
	    Optional<Patient> optionalPatient = patientRepository.findById(id);
	    if (optionalPatient.isPresent()) {
	        Patient existingPatient = optionalPatient.get();
	        
	        // Update fields as needed, for example:
	        existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
	        existingPatient.setGender(updatedPatient.getGender());
	        existingPatient.setContactNumber(updatedPatient.getContactNumber());

	        // Update profile photo:
	        existingPatient.setProfilePhoto(updatedPatient.getProfilePhoto());

	        // Optionally update user fields...
	        
	        return patientRepository.save(existingPatient);
	    }
	    return null;
	}

	public void deletePatient(String id) {
		patientRepository.deleteById(id);
	}
	
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}
	
	public Optional<Patient> getPatientByUserId(String userId) {
		return patientRepository.findByUser_Id(userId);
	}
	
}

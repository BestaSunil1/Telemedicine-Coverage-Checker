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
	public Patient updatePatient(String id, Patient patient) {
		if (patientRepository.existsById(id)) {
			patient.setId(id);
			return patientRepository.save(patient);
		}
		return null; // or throw an exception
	}
	public void deletePatient(String id) {
		patientRepository.deleteById(id);
	}
	
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}
	
}

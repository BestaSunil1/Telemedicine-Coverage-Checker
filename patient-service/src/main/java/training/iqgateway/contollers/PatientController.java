package training.iqgateway.contollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.entities.Patient;
import training.iqgateway.services.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	@PostMapping("/create")
	public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
		Patient createdPatient = patientService.createPatient(patient);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
	}
	
	@GetMapping
	public ResponseEntity<List<Patient>> getAllPatients() {
		List<Patient> patients = patientService.getAllPatients();
		return ResponseEntity.ok(patients);
	}
	
	@PostMapping("/{id}/update") 
	public ResponseEntity<Patient> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
	    Patient updatedPatient = patientService.updatePatient(id, patient);
	    if (updatedPatient != null) {
	        return ResponseEntity.ok(updatedPatient);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}

	
	@GetMapping("/user/{userId}")
	public ResponseEntity<Patient> getPatientByUserId(@PathVariable String userId) {
	    return patientService.getPatientByUserId(userId)
	        .map(patient -> ResponseEntity.ok(patient))
	        .orElse(ResponseEntity.notFound().build());
	}

}

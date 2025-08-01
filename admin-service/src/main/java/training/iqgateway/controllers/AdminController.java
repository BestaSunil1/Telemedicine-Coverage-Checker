package training.iqgateway.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.dtos.DoctorDto;
import training.iqgateway.dtos.DoctorRegistrationDto;
import training.iqgateway.dtos.PatientRegistrationDTO;
import training.iqgateway.entities.Patient;
import training.iqgateway.services.AdminService;

@RestController
@RequestMapping("/api/admin")

public class AdminController {
	
	@Autowired
	private AdminService adminService;

	/**
	 * Endpoint to register a new doctor.
	 * @param registrationDto the registration details of the doctor
	 * @return ResponseEntity with the created DoctorDto or an error message
	 */
	
	@PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@RequestBody DoctorRegistrationDto registrationDto) {
        try {
            DoctorDto doctor = adminService.registerDoctor(registrationDto);
            return new ResponseEntity<>(doctor, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = adminService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DoctorDto>> getActiveDoctors() {
        List<DoctorDto> doctors = adminService.getActiveDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable String id) {
        Optional<DoctorDto> doctor = adminService.getDoctorById(id);
        if (doctor.isPresent()) {
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorDto> doctors = adminService.getDoctorsBySpecialization(specialization);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable String id, @RequestBody DoctorDto doctorDto) {
        try {
            DoctorDto updatedDoctor = adminService.updateDoctor(id, doctorDto);
            return new ResponseEntity<>(updatedDoctor, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable String id) {
        try {
        	adminService.deleteDoctor(id);
            return new ResponseEntity<>("Doctor deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting doctor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@RequestBody PatientRegistrationDTO registrationDTO) {
        try {
            Patient registeredPatient = adminService.registerPatient(registrationDTO);
            return new ResponseEntity<>(registeredPatient, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/patient")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = adminService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }
    
    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) {
        Optional<Patient> patient = adminService.getPatientById(id);
        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/user/patient/{userId}")
    public ResponseEntity<?> getPatientByUserId(@PathVariable String userId) {
        Optional<Patient> patient = adminService.getPatientByUserId(userId);
        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Patient not found for user ID: " + userId, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/email/patient/{email}")
    public ResponseEntity<?> getPatientByEmail(@PathVariable String email) {
        Optional<Patient> patient = adminService.getPatientByEmail(email);
        if (patient.isPresent()) {
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Patient not found for email: " + email, HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/patient/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
        try {
            Patient patient = adminService.updatePatient(id, updatedPatient);
            if (patient != null) {
                return new ResponseEntity<>(patient, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Update failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable String id) {
        try {
            boolean deleted = adminService.deletePatient(id);
            if (deleted) {
                return new ResponseEntity<>("Patient deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Deletion failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

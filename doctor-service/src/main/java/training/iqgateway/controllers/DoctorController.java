package training.iqgateway.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.dto.DoctorAvailabilityResponseDto;
import training.iqgateway.entities.Doctor;

import training.iqgateway.services.DoctorService;

@RestController
@RequestMapping("/api/doctors")

public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // ... existing methods ...

    // NEW ENDPOINTS - Get all doctor availabilities
    @GetMapping("/availabilities")
    public ResponseEntity<List<DoctorAvailabilityResponseDto>> getAllDoctorAvailabilities() {
        List<DoctorAvailabilityResponseDto> availabilities = doctorService.getAllDoctorAvailabilities();
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    @GetMapping("/availabilities/available")
    public ResponseEntity<List<DoctorAvailabilityResponseDto>> getAllAvailableDoctorAvailabilities() {
        List<DoctorAvailabilityResponseDto> availabilities = doctorService.getAllAvailableDoctorAvailabilities();
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    @GetMapping("/availabilities/day/{dayOfWeek}")
    public ResponseEntity<List<DoctorAvailabilityResponseDto>> getDoctorAvailabilitiesByDay(@PathVariable String dayOfWeek) {
        List<DoctorAvailabilityResponseDto> availabilities = doctorService.getDoctorAvailabilitiesByDay(dayOfWeek);
        return new ResponseEntity<>(availabilities, HttpStatus.OK);

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable String userId) {
    	Doctor doctor = doctorService.getByUserId(userId);
    	if (doctor != null) {
			return new ResponseEntity<>(doctor, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
		}
    }
	@PostMapping("/{id}/update") 
	public ResponseEntity<?> updateDoctor(@PathVariable String id, @RequestBody Doctor patient) {
	    Doctor updatedDoctor = doctorService.updateDoctor(id, patient);
	    if (updatedDoctor != null) {
	        return ResponseEntity.ok(updatedDoctor);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Doctor>> getAllDoctors() {
		List<Doctor> doctors = doctorService.getAllDoctors();
		return new ResponseEntity<>(doctors, HttpStatus.OK);
	}
}


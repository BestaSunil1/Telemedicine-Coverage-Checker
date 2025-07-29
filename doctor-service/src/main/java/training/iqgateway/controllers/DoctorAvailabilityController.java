package training.iqgateway.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.dto.DoctorAvailabilityResponseDto;
import training.iqgateway.services.DoctorService;

@RestController
@RequestMapping("/api/availabilities")
@CrossOrigin(origins = "*")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorAvailabilityResponseDto>> getAllDoctorAvailabilities() {
        List<DoctorAvailabilityResponseDto> availabilities = doctorService.getAllDoctorAvailabilities();
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DoctorAvailabilityResponseDto>> getAllAvailableDoctorAvailabilities() {
        List<DoctorAvailabilityResponseDto> availabilities = doctorService.getAllAvailableDoctorAvailabilities();
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }

    @GetMapping("/day/{dayOfWeek}")
    public ResponseEntity<List<DoctorAvailabilityResponseDto>> getDoctorAvailabilitiesByDay(@PathVariable String dayOfWeek) {
        List<DoctorAvailabilityResponseDto> availabilities = doctorService.getDoctorAvailabilitiesByDay(dayOfWeek);
        return new ResponseEntity<>(availabilities, HttpStatus.OK);
    }
}

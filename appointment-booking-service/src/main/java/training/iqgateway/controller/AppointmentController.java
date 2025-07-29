package training.iqgateway.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.dtos.AppointmentBookingRequest;
import training.iqgateway.dtos.AppointmentResponse;
import training.iqgateway.entities.Appointment;
import training.iqgateway.entities.Notifications;
import training.iqgateway.services.AppointmentService;
import training.iqgateway.services.NotificationService;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*") // Allow all origins for CORS
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private NotificationService notificationService;

	// Define endpoints for appointment-related operations
	// Book new appointment
	@PostMapping("/book")
	public ResponseEntity<AppointmentResponse> bookAppointment(@RequestBody AppointmentBookingRequest request) {
		try {
			AppointmentResponse response = appointmentService.bookAppointment(request);
			if (response.getAppointmentId() != null) {
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.badRequest().body(response);
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new AppointmentResponse(null, "Internal server error: " + e.getMessage()));
		}
	}

	// Doctor accepts appointment
	@PutMapping("/{appointmentId}/accept")
	public ResponseEntity<AppointmentResponse> acceptAppointment(@PathVariable String appointmentId,
			@RequestBody Map<String, String> request) {
		try {
			String doctorId = request.get("doctorId");
			AppointmentResponse response = appointmentService.acceptAppointment(appointmentId, doctorId);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new AppointmentResponse(appointmentId, "Internal server error: " + e.getMessage()));
		}
	}

	// Doctor rejects appointment
	@PutMapping("/{appointmentId}/reject")
	public ResponseEntity<AppointmentResponse> rejectAppointment(@PathVariable String appointmentId,
			@RequestBody Map<String, String> request) {
		try {
			String doctorId = request.get("doctorId");
			String reason = request.get("reason");
			AppointmentResponse response = appointmentService.rejectAppointment(appointmentId, doctorId, reason);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(new AppointmentResponse(appointmentId, "Internal server error: " + e.getMessage()));
		}
	}

	// Get pending appointments for doctor
	@GetMapping("/pending/{doctorId}")
	public ResponseEntity<List<Appointment>> getPendingAppointments(@PathVariable String doctorId) {
		try {
			List<Appointment> appointments = appointmentService.getPendingAppointments(doctorId);
			return ResponseEntity.ok(appointments);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	// Get available slots for doctor on specific date
	@GetMapping("/available-slots/{doctorId}/{date}")
	public ResponseEntity<List<LocalDateTime>> getAvailableSlots(@PathVariable String doctorId,
			@PathVariable String date) {
		try {
			LocalDate localDate = LocalDate.parse(date);
			List<LocalDateTime> slots = appointmentService.getAvailableSlots(doctorId, localDate);
			return ResponseEntity.ok(slots);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	// Get appointment details
	@GetMapping("/{appointmentId}")
	public ResponseEntity<Appointment> getAppointmentDetails(@PathVariable String appointmentId) {
		try {
			Appointment appointment = appointmentService.getAppointmentDetails(appointmentId);
			if (appointment != null) {
				return ResponseEntity.ok(appointment);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/getNotifications/{userId}")
	public ResponseEntity<List<Notifications>> getNotificationsForUser(@PathVariable String userId) {
		try {
			List<Notifications> notifications = notificationService.getNotificationsForUser(userId);
			return ResponseEntity.ok(notifications);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Appointment>> getAllAppointments() {
		try {
			List<Appointment> appointments = appointmentService.getAllAppointments();
			return ResponseEntity.ok(appointments);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@GetMapping("/doctorId/{doctorId}")
	public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@PathVariable String doctorId) {
		try {
			List<Appointment> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
			return ResponseEntity.ok(appointments);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}

package training.iqgateway.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import training.iqgateway.entities.Prescription;
import training.iqgateway.services.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
//@CrossOrigin(origins = "*")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // POST /api/prescriptions
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(
            @RequestBody PrescriptionRequest request) {

        Prescription prescription = new Prescription();
        prescription.setMedication(request.getMedication());
        prescription.setDosage(request.getDosage());
        prescription.setDuration(request.getDuration());
        prescription.setInstructions(request.getInstructions());
        prescription.setRefills(request.getRefills());
        prescription.setPharmacy(request.getPharmacy());
        prescription.setDoctor(request.getDoctor());

        Prescription saved = prescriptionService.createPrescription(
                prescription,
                request.getAppointmentId(),
                request.getPatientId()
        );
        return ResponseEntity.ok(saved);
    }

    // GET /api/prescriptions/patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatient(patientId));
    }

    // DTO for request
    public static class PrescriptionRequest {
        private String medication;
        private String dosage;
        private String duration;
        private String instructions;
        private int refills;
        private String pharmacy;
        private String doctor;
        private String appointmentId;
        private String patientId;
        // getters and setters
		public String getMedication() {
			return medication;
		}
		public void setMedication(String medication) {
			this.medication = medication;
		}
		public String getDosage() {
			return dosage;
		}
		public void setDosage(String dosage) {
			this.dosage = dosage;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		public String getInstructions() {
			return instructions;
		}
		public void setInstructions(String instructions) {
			this.instructions = instructions;
		}
		public int getRefills() {
			return refills;
		}
		public void setRefills(int refills) {
			this.refills = refills;
		}
		public String getPharmacy() {
			return pharmacy;
		}
		public void setPharmacy(String pharmacy) {
			this.pharmacy = pharmacy;
		}
		public String getDoctor() {
			return doctor;
		}
		public void setDoctor(String doctor) {
			this.doctor = doctor;
		}
		public String getAppointmentId() {
			return appointmentId;
		}
		public void setAppointmentId(String appointmentId) {
			this.appointmentId = appointmentId;
		}
		public String getPatientId() {
			return patientId;
		}
		public void setPatientId(String patientId) {
			this.patientId = patientId;
		}
        
    }
}


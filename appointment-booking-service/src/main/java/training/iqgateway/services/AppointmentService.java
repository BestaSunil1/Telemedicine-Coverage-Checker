//package training.iqgateway.services;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import training.iqgateway.dtos.AppointmentBookingRequest;
//import training.iqgateway.dtos.AppointmentResponse;
//import training.iqgateway.entities.Appointment;
//import training.iqgateway.entities.AppointmentNote;
//import training.iqgateway.entities.AppointmentStatus;
//import training.iqgateway.entities.Doctor;
//import training.iqgateway.entities.DoctorAvailability;
//import training.iqgateway.entities.InsuranceCoverageStatus;
//import training.iqgateway.entities.Patient;
//import training.iqgateway.repositories.AppointmentNoteRepository;
//import training.iqgateway.repositories.AppointmentRepository;
//import training.iqgateway.repositories.DoctorAvalilabilityRepository;
//import training.iqgateway.repositories.DoctorRepository;
//import training.iqgateway.repositories.PatientRepository;
//
//@Service
//public class AppointmentService {
//
//	@Autowired
//    private AppointmentRepository appointmentRepository;
//    
//    @Autowired
//    private DoctorRepository doctorRepository;
//    
//    @Autowired
//    private PatientRepository patientRepository;
//    
//    @Autowired
//    private DoctorAvalilabilityRepository availabilityRepository;
//    
//    @Autowired
//    private NotificationService notificationService;
//    
//    @Autowired
//    private AppointmentNoteRepository noteRepository;
////    
//    public AppointmentResponse bookAppointment(AppointmentBookingRequest request) {
//        try {
//            // Validate doctor availability
//        	System.out.println("Booking appointment for doctor: " + request.getDoctorId() + 
//				" at " + request.getScheduledAt());
//            if (!isDoctorAvailable(request.getDoctorId(), request.getScheduledAt())) {
//                return new AppointmentResponse(null, "Doctor is not available at the requested time");
//            }
//            
//            // Check for conflicting appointments
//            List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
//                request.getDoctorId(), request.getScheduledAt());
//            System.out.println("Conflicting appointments found: " + conflicts.size() + conflicts);
//            
//            if (!conflicts.isEmpty()) {
//                return new AppointmentResponse(null, "Time slot is already booked");
//            }
//            
//            // Fetch Patient and Doctor entities
//            Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
//            System.out.println("Patient found: " + (patient != null ? patient.getId() : "null"));
//            Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
//            System.out.println("Doctor found: " + (doctor != null ? doctor.getId() : "null"));
//            
//            if (patient == null || doctor == null) {
//                return new AppointmentResponse(null, "Patient or Doctor not found");
//            }
//            
//            // Create appointment
//            Appointment appointment = new Appointment();
//            appointment.setPatient(patient);
//            appointment.setDoctor(doctor);
//            appointment.setAppointmentDate(request.getScheduledAt());
//            String coverageStatusString = request.getInsuranceCoverageStatus();
//            if (coverageStatusString != null) {
//                InsuranceCoverageStatus status = InsuranceCoverageStatus.valueOf(coverageStatusString.toUpperCase());
//                appointment.setInsuranceCoverageStatus(status);
//            }
//            appointment.setCoverageCheckDate(LocalDateTime.now());
//            appointment.setCreatedAt(LocalDateTime.now());
//            appointment.setStatus(AppointmentStatus.BOOKED); // Assuming enum value
//            
//            Appointment savedAppointment = appointmentRepository.save(appointment);
//            
//            // Create appointment note
//            createAppointmentNote(savedAppointment.getId(), 
//                "Appointment booked for " + request.getScheduledAt(), "booking");
//            
//            // Send notification to doctor
//            List<String> fromNotSer = notificationService.notifyDoctorOfNewAppointment(
//                request.getDoctorId(), savedAppointment.getId(), request.getPatientId());
//            System.out.println("Notification sent to doctor: " + fromNotSer);
//            
//            return new AppointmentResponse(
//				savedAppointment.getId(), 
//				fromNotSer.get(1), 
//				fromNotSer.get(0), 
//				savedAppointment.getAppointmentDate(), 
//				savedAppointment.getStatus().toString(), 
//				"Appointment booked successfully");
//            
//        } catch (Exception e) {
//            return new AppointmentResponse(null, "Error booking appointment: " + e.getMessage());
//        }
//    }
//
//   
//    // Doctor accepts appointment
//    public AppointmentResponse acceptAppointment(String appointmentId, String doctorId) {
//        try {
//            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
//            
//            if (!appointmentOpt.isPresent()) {
//                return new AppointmentResponse(appointmentId, "Appointment not found");
//            }
//            
//            Appointment appointment = appointmentOpt.get();
//            
//            if (!appointment.getDoctor().getId().equals(doctorId)) {
//                return new AppointmentResponse(appointmentId, "Unauthorized access");
//            }
//            
//            if (!"BOOKED".equals(appointment.getStatus().toString())) {
//                return new AppointmentResponse(appointmentId, 
//                    "Cannot accept appointment. Current status: " + appointment.getStatus());
//            }
//            
//            // Update status to confirmed
//            appointment.setStatus(AppointmentStatus.CONFIRMED);
//            appointmentRepository.save(appointment);
//            
//            // Create note
//            createAppointmentNote(appointmentId, "Appointment confirmed by doctor", "status_update");
//            
//            // Notify patient
//            notificationService.notifyPatientOfAppointmentUpdate(
//                appointment.getPatient().getId(), appointmentId, 
//                "Your appointment has been confirmed by the doctor.");
//            
//            return new AppointmentResponse(
//				appointmentId, 
//				appointment.getDoctor().getUser().getUsername(), // Doctor's name
//				appointment.getPatient().getUser().getUsername(), // Patient's name
//				appointment.getAppointmentDate(), 
//				appointment.getStatus().toString(), 
//				"Appointment accepted successfully");
//            
//        } catch (Exception e) {
//            return new AppointmentResponse(appointmentId, "Error accepting appointment: " + e.getMessage());
//        }
//    }
////    
//    // Doctor rejects appointment
//    public AppointmentResponse rejectAppointment(String appointmentId, String doctorId, String reason) {
//        try {
//            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
//            
//            if (!appointmentOpt.isPresent()) {
//                return new AppointmentResponse(appointmentId, "Appointment not found");
//            }
//            
//            Appointment appointment = appointmentOpt.get();
//            
//            if (!appointment.getDoctor().getId().equals(doctorId)) {
//                return new AppointmentResponse(appointmentId, "Unauthorized access");
//            }
//            
//            if (!"BOOKED".equals(appointment.getStatus().toString())) {
//                return new AppointmentResponse(appointmentId, 
//                    "Cannot reject appointment. Current status: " + appointment.getStatus());
//            }
//            
//            // Update status to cancelled
//            appointment.setStatus(AppointmentStatus.REJECTED);
//            appointmentRepository.save(appointment);
//            
//            // Create note
//            String noteText = reason != null ? 
//                "Appointment cancelled by doctor. Reason: " + reason : 
//                "Appointment cancelled by doctor";
//            createAppointmentNote(appointmentId, noteText, "cancellation");
//            
//            // Notify patient
//            String notificationMessage = reason != null ? 
//                "Your appointment has been cancelled by the doctor. Reason: " + reason :
//                "Your appointment has been cancelled by the doctor.";
//            
//            notificationService.notifyPatientOfAppointmentUpdate(
//                appointment.getPatient().getId(), appointmentId, notificationMessage);
//            
//            return new AppointmentResponse(appointmentId, 
//				appointment.getDoctor().getUser().getUsername(), // Doctor's name
//				appointment.getPatient().getUser().getUsername(), // Patient's name
//				appointment.getAppointmentDate(), 
//				appointment.getStatus().toString(), 
//				"Appointment rejected successfully");
//            
//        } catch (Exception e) {
//            return new AppointmentResponse(appointmentId, "Error rejecting appointment: " + e.getMessage());
//        }
//    }
////    
//    // Get pending appointments for doctor
//    public List<Appointment> getPendingAppointments(String doctorId) {
//        System.out.println("Looking for appointments with doctorId: " + doctorId);
//        System.out.println("Status: " + AppointmentStatus.BOOKED);
//        
//        List<Appointment> allAppointments = appointmentRepository.findAll();
//        System.out.println("Total appointments in database: " + allAppointments.size());
//        
//        // ADD THIS LOOP to see what's actually in the database:
//        for (Appointment apt : allAppointments) {
//            System.out.println("Appointment ID: " + apt.getId() + 
//                              ", Doctor ID: " + apt.getDoctor().getId() + 
//                             
//                              ", Status: " + apt.getStatus());
//        }
//        
//        List<Appointment> result = appointmentRepository.findByDoctor_IdAndStatus(doctorId, "BOOKED");
//        System.out.println("Query result size: " + result.size());
//        
//        return result;
//    }
////    
//    // Get available time slots
//    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date) {
//        String dayOfWeek = date.getDayOfWeek().toString().toLowerCase();
//        ObjectId doctorObjectId = new ObjectId(doctorId);
//        DoctorAvailability availability = availabilityRepository
//            .findByDoctorIdAndDayOfWeekAndIsAvailable(doctorObjectId, dayOfWeek, true);
//        
//        if (availability == null) {
//            return new ArrayList<>();
//        }
//        
//        // Get existing appointments for the day
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(23, 59, 59);
//        
//        List<Appointment> existingAppointments = appointmentRepository
//            .findAppointmentsByDoctorAndDateRange(doctorId, startOfDay, endOfDay);
//        
//        // Generate available slots
//        List<LocalDateTime> availableSlots = new ArrayList<>();
//        
//        // Extract time from LocalDateTime fields
//        LocalTime currentTime = availability.getStartTime().toLocalTime();
//        LocalTime endTime = availability.getEndTime().toLocalTime();
//        int duration = availability.getAppointmentDuration() > 0 ? 
//            availability.getAppointmentDuration() : 30;
//        
//        while (currentTime.isBefore(endTime)) {
//            LocalDateTime slotDateTime = date.atTime(currentTime);
//            
//            // Check if slot is not booked
//            boolean isBooked = existingAppointments.stream()
//                .anyMatch(apt -> apt.getAppointmentDate() != null && 
//                               apt.getAppointmentDate().equals(slotDateTime));
//            
//            if (!isBooked) {
//                availableSlots.add(slotDateTime);
//            }
//            
//            currentTime = currentTime.plusMinutes(duration);
//        }
//        
//        return availableSlots;
//    }
////    // Check if doctor is available
//    private boolean isDoctorAvailable(String doctorId, LocalDateTime requestedTime) {
//        String dayOfWeek = requestedTime.getDayOfWeek().toString().toUpperCase();
//        Doctor doctor = doctorRepository.findById(doctorId)
//			.orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
//        ObjectId doctorObjectId = new ObjectId(doctor.getId());
//        System.out.println(dayOfWeek);
//        DoctorAvailability availability = availabilityRepository
//            .findByDoctorIdAndDayOfWeekAndIsAvailable(doctorObjectId, dayOfWeek, true);
//        
//  	  System.out.println(availability);
//        if (availability == null) {
//            return false;
//        }
//        System.out.println(availability.getStartTime());
//        // Extract time part from LocalDateTime fields
//        LocalTime requestedTimeOnly = requestedTime.toLocalTime();
//        LocalTime startTime = availability.getStartTime().toLocalTime();
//        LocalTime endTime = availability.getEndTime().toLocalTime();
//        
//        return !requestedTimeOnly.isBefore(startTime) && 
//               !requestedTimeOnly.isAfter(endTime);
//    }
//   
//    // Create appointment note
//    private void createAppointmentNote(String appointmentId, String noteText, String noteType) {
//        AppointmentNote note = new AppointmentNote();
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//			.orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//        note.setAppointment(appointment);
//        note.setNoteText(noteText);
//        note.setNoteType(noteType);
//        noteRepository.save(note);
//    }
//    
//    // Get appointment details
//    public Appointment getAppointmentDetails(String appointmentId) {
//        return appointmentRepository.findById(appointmentId).orElse(null);
//    }
//    
//    public List<Appointment> getAllAppointments() {
//		return appointmentRepository.findAll();
//	}
//    
//    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
//		return appointmentRepository.findByDoctor_Id(doctorId);
//	}
//    
//    public List<Appointment> getAppointmentsByPatientId(String patientId) {
//    			return appointmentRepository.findByPatient_Id(patientId);
//    }
//    
////    public boolean isCoverageCovered(String doctorId, String patientId) {
////		Optional<Patient> patient = patientRepository.findById(patientId);
////		
////		Patient patientEntity = patient.orElse(null);
////		System.out.println("Patient entity: " + patientEntity);
////		
////		Optional<Doctor> doctor = doctorRepository.findById(doctorId);
////		Doctor doctorEntity = doctor.orElse(null);
////		System.out.println("Doctor entity: " + doctorEntity);
////		
////		
////		
////	}
///
///
///
///
///
///
///
///
///
///
///
package training.iqgateway.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import training.iqgateway.dtos.AppointmentBookingRequest;
import training.iqgateway.dtos.AppointmentResponse;
import training.iqgateway.entities.Appointment;
import training.iqgateway.entities.AppointmentNote;
import training.iqgateway.entities.AppointmentStatus;
import training.iqgateway.entities.Doctor;
import training.iqgateway.entities.DoctorAvailability;
import training.iqgateway.entities.EligibilityStatus;
import training.iqgateway.entities.InsuranceCoverageStatus;
import training.iqgateway.entities.InsuranceEligibility;
import training.iqgateway.entities.InsurancePlan;
import training.iqgateway.entities.Patient;
import training.iqgateway.repositories.AppointmentNoteRepository;
import training.iqgateway.repositories.AppointmentRepository;
import training.iqgateway.repositories.DoctorAvalilabilityRepository;
import training.iqgateway.repositories.DoctorRepository;
import training.iqgateway.repositories.InsuranceEligibilityRepository;
import training.iqgateway.repositories.InsurancePlanRepository;
import training.iqgateway.repositories.PatientRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorAvalilabilityRepository availabilityRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private AppointmentNoteRepository noteRepository;
    
    // NEW: Insurance-related repositories
    @Autowired
    private InsuranceEligibilityRepository eligibilityRepository;
    
    @Autowired
    private InsurancePlanRepository insurancePlanRepository;

    public AppointmentResponse bookAppointment(AppointmentBookingRequest request) {
        try {
            // Validate doctor availability
            System.out.println("Booking appointment for doctor: " + request.getDoctorId() + 
                " at " + request.getScheduledAt());
            if (!isDoctorAvailable(request.getDoctorId(), request.getScheduledAt())) {
                return new AppointmentResponse(null, "Doctor is not available at the requested time");
            }
            
            // Check for conflicting appointments
            List<Appointment> conflicts = appointmentRepository.findConflictingAppointments(
                request.getDoctorId(), request.getScheduledAt());
            System.out.println("Conflicting appointments found: " + conflicts.size() + conflicts);
            
            if (!conflicts.isEmpty()) {
                return new AppointmentResponse(null, "Time slot is already booked");
            }
            
            // Fetch Patient and Doctor entities
            Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
            System.out.println("Patient found: " + (patient != null ? patient.getId() : "null"));
            Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
            System.out.println("Doctor found: " + (doctor != null ? doctor.getId() : "null"));
            
            if (patient == null || doctor == null) {
                return new AppointmentResponse(null, "Patient or Doctor not found");
            }
            
            // NEW: Validate insurance coverage if insurance plan is provided
            if (request.getInsurancePlanId() != null && !request.getInsurancePlanId().trim().isEmpty()) {
                CoverageValidationResult coverageResult = validateInsuranceCoverage(
                    patient, doctor, request.getInsurancePlanId()
                );
                
                if (!coverageResult.isValid()) {
                    return new AppointmentResponse(null, coverageResult.getErrorMessage());
                }
                
                // Create appointment with insurance coverage
                return createAppointmentWithInsurance(request, patient, doctor, coverageResult);
            } else {
                // Create appointment without insurance (existing logic)
                return createSelfPayAppointment(request, patient, doctor);
            }
            
        } catch (Exception e) {
            return new AppointmentResponse(null, "Error booking appointment: " + e.getMessage());
        }
    }
    
    // NEW: Validate insurance coverage method
    private CoverageValidationResult validateInsuranceCoverage(Patient patient, Doctor doctor, String insurancePlanId) {
        try {
            System.out.println("Validating insurance coverage for patient: " + patient.getId() + 
                             ", doctor: " + doctor.getId() + ", plan: " + insurancePlanId);
            
            // Step 1: Check if patient has eligibility record for this insurance plan
            Optional<InsuranceEligibility> eligibilityOpt = 
                eligibilityRepository.findByPatient_IdAndPlan_Id(patient.getId(), insurancePlanId);
            
            if (eligibilityOpt.isEmpty()) {
                return CoverageValidationResult.invalid(
                    "No insurance eligibility record found for this plan. Please verify your insurance eligibility first."
                );
            }
            
            InsuranceEligibility eligibility = eligibilityOpt.get();
            System.out.println("Found eligibility record: " + eligibility.getId() + " with status: " + eligibility.getStatus());
            
            // Step 2: Check if eligibility is verified
            if (eligibility.getStatus() != EligibilityStatus.VERIFIED) {
                return CoverageValidationResult.invalid(
                    "Insurance eligibility is not verified. Status: " + eligibility.getStatus() + 
                    ". Please contact admin to verify your insurance."
                );
            }
            
            // Step 3: Get insurance plan details
            Optional<InsurancePlan> planOpt = insurancePlanRepository.findById(insurancePlanId);
            if (planOpt.isEmpty() || !planOpt.get().isActive()) {
                return CoverageValidationResult.invalid("Insurance plan not found or inactive");
            }
            
            InsurancePlan plan = planOpt.get();
            System.out.println("Found insurance plan: " + plan.getPlanName() + " with coverages: " + plan.getCoverages());
            
            // Step 4: Check if doctor's specialization is covered by the insurance plan
            List<String> planCoverages = plan.getCoverages();
            List<String> doctorSpecialization = doctor.getSpecializations();
            System.out.println("Doctor specialization: " + doctorSpecialization);
            
            if (planCoverages == null || planCoverages.isEmpty()) {
                return CoverageValidationResult.invalid("Insurance plan has no coverage information");
            }
            
            // Check if doctor's specialization matches any of the plan coverages
            boolean isCovered = planCoverages.stream()
            	    .anyMatch(coverage -> 
            	        doctorSpecialization.stream()
            	            .anyMatch(spec -> spec.equalsIgnoreCase(coverage))
            	    );

            
            System.out.println("Coverage check result: " + isCovered);
            
            if (!isCovered) {
                return CoverageValidationResult.invalid(
                    String.format("Doctor's specialization '%s' is not covered by your insurance plan '%s'. " +
                                "Covered specializations: %s", 
                                doctorSpecialization, 
                                plan.getPlanName(),
                                String.join(", ", planCoverages))
                );
            }
            
            // All validations passed
            System.out.println("Insurance coverage validation successful!");
            return CoverageValidationResult.valid(eligibility, plan);
            
        } catch (Exception e) {
            System.err.println("Error validating insurance coverage: " + e.getMessage());
            return CoverageValidationResult.invalid("Error validating insurance coverage: " + e.getMessage());
        }
    }
    
    // NEW: Create appointment with insurance coverage
    private AppointmentResponse createAppointmentWithInsurance(
            AppointmentBookingRequest request, 
            Patient patient, 
            Doctor doctor, 
            CoverageValidationResult coverageResult) {
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getScheduledAt());
        appointment.setInsuranceCoverageStatus(InsuranceCoverageStatus.COVERED);
        appointment.setCoverageCheckDate(LocalDateTime.now());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        
        // Set insurance-related fields (you may need to add these to your Appointment entity)
        // appointment.setInsurancePlan(coverageResult.getInsurancePlan());
        // appointment.setInsuranceEligibility(coverageResult.getEligibility());
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Create appointment note with insurance details
        createAppointmentNote(savedAppointment.getId(), 
            String.format("Appointment booked for %s with insurance coverage. Plan: %s (%s)", 
                request.getScheduledAt(),
                coverageResult.getInsurancePlan().getPlanName(),
                coverageResult.getInsurancePlan().getPlanProvider()), 
            "booking_with_insurance");
        
        // Send notification to doctor
        List<String> fromNotSer = notificationService.notifyDoctorOfNewAppointment(
            request.getDoctorId(), savedAppointment.getId(), request.getPatientId());
        System.out.println("Notification sent to doctor: " + fromNotSer);
        
        return new AppointmentResponse(
            savedAppointment.getId(), 
            fromNotSer.get(1), 
            fromNotSer.get(0), 
            savedAppointment.getAppointmentDate(), 
            savedAppointment.getStatus().toString(), 
            String.format("Appointment booked successfully with insurance coverage (%s - %s)",
                coverageResult.getInsurancePlan().getPlanName(),
                coverageResult.getInsurancePlan().getPlanProvider()));
    }
    
    // NEW: Create self-pay appointment (existing logic)
    private AppointmentResponse createSelfPayAppointment(
            AppointmentBookingRequest request, 
            Patient patient, 
            Doctor doctor) {
        
        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getScheduledAt());
        
        // Handle insurance coverage status from request (your existing logic)
        String coverageStatusString = request.getInsuranceCoverageStatus();
        if (coverageStatusString != null) {
            InsuranceCoverageStatus status = InsuranceCoverageStatus.valueOf(coverageStatusString.toUpperCase());
            appointment.setInsuranceCoverageStatus(status);
        } else {
            appointment.setInsuranceCoverageStatus(InsuranceCoverageStatus.NOT_COVERED);
        }
        
        appointment.setCoverageCheckDate(LocalDateTime.now());
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.BOOKED);
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Create appointment note
        createAppointmentNote(savedAppointment.getId(), 
            "Appointment booked for " + request.getScheduledAt() + " (Self-pay)", "booking");
        
        // Send notification to doctor
        List<String> fromNotSer = notificationService.notifyDoctorOfNewAppointment(
            request.getDoctorId(), savedAppointment.getId(), request.getPatientId());
        System.out.println("Notification sent to doctor: " + fromNotSer);
        
        return new AppointmentResponse(
            savedAppointment.getId(), 
            fromNotSer.get(1), 
            fromNotSer.get(0), 
            savedAppointment.getAppointmentDate(), 
            savedAppointment.getStatus().toString(), 
            "Appointment booked successfully (Self-pay)");
    }

    // Doctor accepts appointment
    public AppointmentResponse acceptAppointment(String appointmentId, String doctorId) {
        try {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
            
            if (!appointmentOpt.isPresent()) {
                return new AppointmentResponse(appointmentId, "Appointment not found");
            }
            
            Appointment appointment = appointmentOpt.get();
            
            if (!appointment.getDoctor().getId().equals(doctorId)) {
                return new AppointmentResponse(appointmentId, "Unauthorized access");
            }
            
            if (!"BOOKED".equals(appointment.getStatus().toString())) {
                return new AppointmentResponse(appointmentId, 
                    "Cannot accept appointment. Current status: " + appointment.getStatus());
            }
            
            // Update status to confirmed
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);
            
            // Create note
            createAppointmentNote(appointmentId, "Appointment confirmed by doctor", "status_update");
            
            // Notify patient
            notificationService.notifyPatientOfAppointmentUpdate(
                appointment.getPatient().getId(), appointmentId, 
                "Your appointment has been confirmed by the doctor.");
            
            return new AppointmentResponse(
                appointmentId, 
                appointment.getDoctor().getUser().getUsername(), // Doctor's name
                appointment.getPatient().getUser().getUsername(), // Patient's name
                appointment.getAppointmentDate(), 
                appointment.getStatus().toString(), 
                "Appointment accepted successfully");
            
        } catch (Exception e) {
            return new AppointmentResponse(appointmentId, "Error accepting appointment: " + e.getMessage());
        }
    }
    
    // Doctor rejects appointment
    public AppointmentResponse rejectAppointment(String appointmentId, String doctorId, String reason) {
        try {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
            
            if (!appointmentOpt.isPresent()) {
                return new AppointmentResponse(appointmentId, "Appointment not found");
            }
            
            Appointment appointment = appointmentOpt.get();
            
            if (!appointment.getDoctor().getId().equals(doctorId)) {
                return new AppointmentResponse(appointmentId, "Unauthorized access");
            }
            
            if (!"BOOKED".equals(appointment.getStatus().toString())) {
                return new AppointmentResponse(appointmentId, 
                    "Cannot reject appointment. Current status: " + appointment.getStatus());
            }
            
            // Update status to cancelled
            appointment.setStatus(AppointmentStatus.REJECTED);
            appointmentRepository.save(appointment);
            
            // Create note
            String noteText = reason != null ? 
                "Appointment cancelled by doctor. Reason: " + reason : 
                "Appointment cancelled by doctor";
            createAppointmentNote(appointmentId, noteText, "cancellation");
            
            // Notify patient
            String notificationMessage = reason != null ? 
                "Your appointment has been cancelled by the doctor. Reason: " + reason :
                "Your appointment has been cancelled by the doctor.";
            
            notificationService.notifyPatientOfAppointmentUpdate(
                appointment.getPatient().getId(), appointmentId, notificationMessage);
            
            return new AppointmentResponse(appointmentId, 
                appointment.getDoctor().getUser().getUsername(), // Doctor's name
                appointment.getPatient().getUser().getUsername(), // Patient's name
                appointment.getAppointmentDate(), 
                appointment.getStatus().toString(), 
                "Appointment rejected successfully");
            
        } catch (Exception e) {
            return new AppointmentResponse(appointmentId, "Error rejecting appointment: " + e.getMessage());
        }
    }
    
    // Get pending appointments for doctor
    public List<Appointment> getPendingAppointments(String doctorId) {
        System.out.println("Looking for appointments with doctorId: " + doctorId);
        System.out.println("Status: " + AppointmentStatus.BOOKED);
        
        List<Appointment> allAppointments = appointmentRepository.findAll();
        System.out.println("Total appointments in database: " + allAppointments.size());
        
        // ADD THIS LOOP to see what's actually in the database:
        for (Appointment apt : allAppointments) {
            System.out.println("Appointment ID: " + apt.getId() + 
                              ", Doctor ID: " + apt.getDoctor().getId() + 
                              ", Status: " + apt.getStatus());
        }
        
        List<Appointment> result = appointmentRepository.findByDoctor_IdAndStatus(doctorId, "BOOKED");
        System.out.println("Query result size: " + result);
        
        return result;
    }
    
    // Get available time slots
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().toString().toLowerCase();
        ObjectId doctorObjectId = new ObjectId(doctorId);
        DoctorAvailability availability = availabilityRepository
            .findByDoctorIdAndDayOfWeekAndIsAvailable(doctorObjectId, dayOfWeek, true);
        
        if (availability == null) {
            return new ArrayList<>();
        }
        
        // Get existing appointments for the day
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        List<Appointment> existingAppointments = appointmentRepository
            .findAppointmentsByDoctorAndDateRange(doctorId, startOfDay, endOfDay);
        
        // Generate available slots
        List<LocalDateTime> availableSlots = new ArrayList<>();
        
        // Extract time from LocalDateTime fields
        LocalTime currentTime = availability.getStartTime().toLocalTime();
        LocalTime endTime = availability.getEndTime().toLocalTime();
        int duration = availability.getAppointmentDuration() > 0 ? 
            availability.getAppointmentDuration() : 30;
        
        while (currentTime.isBefore(endTime)) {
            LocalDateTime slotDateTime = date.atTime(currentTime);
            
            // Check if slot is not booked
            boolean isBooked = existingAppointments.stream()
                .anyMatch(apt -> apt.getAppointmentDate() != null && 
                               apt.getAppointmentDate().equals(slotDateTime));
            
            if (!isBooked) {
                availableSlots.add(slotDateTime);
            }
            
            currentTime = currentTime.plusMinutes(duration);
        }
        
        return availableSlots;
    }
    
    // Check if doctor is available
    private boolean isDoctorAvailable(String doctorId, LocalDateTime requestedTime) {
        String dayOfWeek = requestedTime.getDayOfWeek().toString().toUpperCase();
        Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        ObjectId doctorObjectId = new ObjectId(doctor.getId());
        System.out.println(dayOfWeek);
        DoctorAvailability availability = availabilityRepository
            .findByDoctorIdAndDayOfWeekAndIsAvailable(doctorObjectId, dayOfWeek, true);
        
        System.out.println(availability);
        if (availability == null) {
            return false;
        }
        System.out.println(availability.getStartTime());
        // Extract time part from LocalDateTime fields
        LocalTime requestedTimeOnly = requestedTime.toLocalTime();
        LocalTime startTime = availability.getStartTime().toLocalTime();
        LocalTime endTime = availability.getEndTime().toLocalTime();
        
        return !requestedTimeOnly.isBefore(startTime) && 
               !requestedTimeOnly.isAfter(endTime);
    }
   
    // Create appointment note
    private void createAppointmentNote(String appointmentId, String noteText, String noteType) {
        AppointmentNote note = new AppointmentNote();
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        note.setAppointment(appointment);
        note.setNoteText(noteText);
        note.setNoteType(noteType);
        noteRepository.save(note);
    }
    
    // Get appointment details
    public Appointment getAppointmentDetails(String appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId);
    }
    
    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        return appointmentRepository.findByPatient_Id(patientId);
    }
    
    // NEW: Helper class for coverage validation result
    public static class CoverageValidationResult {
        private boolean valid;
        private String errorMessage;
        private InsuranceEligibility eligibility;
        private InsurancePlan insurancePlan;
        
        private CoverageValidationResult(boolean valid, String errorMessage, 
                                       InsuranceEligibility eligibility, InsurancePlan plan) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.eligibility = eligibility;
            this.insurancePlan = plan;
        }
        
        public static CoverageValidationResult valid(InsuranceEligibility eligibility, InsurancePlan plan) {
            return new CoverageValidationResult(true, null, eligibility, plan);
        }
        
        public static CoverageValidationResult invalid(String errorMessage) {
            return new CoverageValidationResult(false, errorMessage, null, null);
        }
        
        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
        public InsuranceEligibility getEligibility() { return eligibility; }
        public InsurancePlan getInsurancePlan() { return insurancePlan; }
    }
    
    public List<Appointment> getConfirmedAppointmentsDoctor(String doctorId) {
        System.out.println("Looking for appointments with doctorId: " + doctorId);
        System.out.println("Status: " + AppointmentStatus.BOOKED);
        
        List<Appointment> allAppointments = appointmentRepository.findAll();
        System.out.println("Total appointments in database: " + allAppointments.size());
        
        // ADD THIS LOOP to see what's actually in the database:
        for (Appointment apt : allAppointments) {
            System.out.println("Appointment ID: " + apt.getId() + 
                              ", Doctor ID: " + apt.getDoctor().getId() + 
                              ", Status: " + apt.getStatus());
        }
        
        List<Appointment> result = appointmentRepository.findByDoctor_IdAndStatus(doctorId, "CONFIRMED");
        System.out.println("Query result size: " + result);
        
        return result;
    }
    
    public List<Appointment> getConfirmedAppointmentsPatinet(String doctorId) {
        System.out.println("Looking for appointments with doctorId: " + doctorId);
        System.out.println("Status: " + AppointmentStatus.BOOKED);
        
        List<Appointment> allAppointments = appointmentRepository.findAll();
        System.out.println("Total appointments in database: " + allAppointments.size());
        
        // ADD THIS LOOP to see what's actually in the database:
        for (Appointment apt : allAppointments) {
            System.out.println("Appointment ID: " + apt.getId() + 
                              ", Doctor ID: " + apt.getDoctor().getId() + 
                              ", Status: " + apt.getStatus());
        }
        
        List<Appointment> result = appointmentRepository.findByPatient_IdAndStatus(doctorId, "CONFIRMED");
        System.out.println("Query result size: " + result);
        
        return result;
    }
}
//    
//}

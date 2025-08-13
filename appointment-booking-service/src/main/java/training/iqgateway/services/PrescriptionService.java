package training.iqgateway.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import training.iqgateway.entities.Appointment;
import training.iqgateway.entities.Patient;
import training.iqgateway.entities.Prescription;
import training.iqgateway.repositories.AppointmentRepository;
import training.iqgateway.repositories.PatientRepository;
import training.iqgateway.repositories.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                                AppointmentRepository appointmentRepository,
                                PatientRepository patientRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public Prescription createPrescription(Prescription prescription, String appointmentId, String patientId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        prescription.setAppointment(appointment);
        prescription.setPatient(patient);
        prescription.setDate(LocalDate.now());

        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsByPatient(String patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }
}

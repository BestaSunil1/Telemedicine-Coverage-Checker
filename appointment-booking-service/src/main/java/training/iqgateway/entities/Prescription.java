package training.iqgateway.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDate;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;

    private String medication;
    private String dosage;
    private String duration;
    private String instructions;
    private int refills;
    private String pharmacy;
    private String doctor;
    private LocalDate date;

    @DBRef
    private Appointment appointment;

    @DBRef
    private Patient patient;

    public Prescription() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Prescription(String id, String medication, String dosage, String duration, String instructions, int refills,
			String pharmacy, String doctor, LocalDate date, Appointment appointment, Patient patient) {
		super();
		this.id = id;
		this.medication = medication;
		this.dosage = dosage;
		this.duration = duration;
		this.instructions = instructions;
		this.refills = refills;
		this.pharmacy = pharmacy;
		this.doctor = doctor;
		this.date = date;
		this.appointment = appointment;
		this.patient = patient;
	}
    
    
}


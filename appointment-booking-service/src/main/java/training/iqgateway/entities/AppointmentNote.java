package training.iqgateway.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "appointment_note")
public class AppointmentNote {

    @Id
    private String id;
    @DBRef
    private Appointment appointment;
    @Field(name = "created_by")
    private String createdBy;
    @Field(name = "note_text")
    private String noteText;
    @Field(name = "created_at")
    private String createdAt;
    @Field(name = "note_type")
    private String noteType;
    
    public AppointmentNote() {
		super();
	}
    
    public AppointmentNote(String id, Appointment appointment, String createdBy, String noteText, String createdAt,
			String noteType) {
		super();
		this.id = id;
		this.appointment = appointment;
		this.createdBy = createdBy;
		this.noteText = noteText;
		this.createdAt = createdAt;
		this.noteType = noteType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getNoteText() {
		return noteText;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
}

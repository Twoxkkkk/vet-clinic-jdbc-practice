package domain.appointment;

import domain.pet_owner.Pet;
import domain.shared.Id;

import domain.shared.ProcedureId;
import domain.vet.Vet;


import java.time.LocalDateTime;

public class Appointment{

    private Id<Appointment> id;

    private Id<Vet> vetId;
    private Id<Pet> petId;

    private LocalDateTime dateTimeOfAppointment;

    private ProcedureId procedureId;

    private AppointmentStatus status;


    public Appointment(Id<Appointment> id, Id<Vet> vetId, Id<Pet> petId,
                LocalDateTime dateTimeOfAppointment, AppointmentStatus status, ProcedureId procedureId
    ) {

        this.setId(id);
        this.setPetId(petId);
        this.setVetId(vetId);
        this.setDateTimeOfAppointment(dateTimeOfAppointment);
        this.setStatus(status);
        this.setProcedureId(procedureId);

    }

    public Id<Appointment> getId() {
        return id;
    }

    public void setId(Id<Appointment> id) {
        this.id = id;
    }

    public ProcedureId getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(ProcedureId procedureId) {
        this.procedureId = procedureId;
    }

    public Id<Vet> getVetId() {
        return vetId;
    }

    public void setVetId(Id<Vet> vet_id) {
        this.vetId = vet_id;
    }

    public Id<Pet> getPetId() {
        return petId;
    }

    public void setPetId(Id<Pet> petId) {
        this.petId = petId;
    }

    public LocalDateTime getDateTimeOfAppointment() {
        return dateTimeOfAppointment;
    }

    public void setDateTimeOfAppointment(LocalDateTime dateTimeOfAppointment) {
        this.dateTimeOfAppointment = dateTimeOfAppointment;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}

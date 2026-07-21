package domain.appointment;

import domain.pet_owner.Pet;
import domain.shared.Id;

import domain.vet.Vet;


import java.time.LocalDateTime;

public class Appointment{

    private Id<Appointment> id;

    private Id<Vet> vetId;
    private Id<Pet> petId;

    private LocalDateTime dateTimeOfAppointment;

    private AppointmentStatus status;


    public Appointment(Id<Appointment> id, Id<Vet> vetId, Id<Pet> petId,
                LocalDateTime dateTimeOfAppointment, AppointmentStatus status
    ) {

        this.setId(id);
        this.setPetOwnerId(petId);
        this.setVetId(vetId);
        this.setDateTimeOfAppointment(dateTimeOfAppointment);
        this.setStatus(status);

    }

    public Id<Appointment> getId() {
        return id;
    }

    public void setId(Id<Appointment> id) {
        this.id = id;
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

    public void setPetOwnerId(Id<Pet> petId) {
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

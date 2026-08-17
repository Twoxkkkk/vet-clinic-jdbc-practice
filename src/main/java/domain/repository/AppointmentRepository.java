package domain.repository;

import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.shared.Id;
import domain.vet.Vet;

import java.util.List;

public interface AppointmentRepository extends Repository<Appointment>{

    List<Appointment> findAllByPetIdWithStatus(Id<Pet> petId, AppointmentStatus status);
    List<Appointment> findAllByVetIdWithStatus(Id<Vet> vetId, AppointmentStatus status);

    List<Appointment> findAllForTodayByVetId(Id<Vet> vetId);
    List<Appointment> findAllForTodayByPetId(Id<Pet> petId);

}

package domain.repository;

import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.shared.Id;
import domain.vet.Vet;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {

    void save(Appointment appointment);
    void delete(Id<Appointment> appointmentId);

    List<Appointment> findAllByPetId(Id<Pet> petId);
    List<Appointment> findAllByVetIdWithStatus(Id<Vet> vetId, AppointmentStatus status);

    List<Appointment> findAllForTodayByVetId(Id<Vet> vetId);
    List<Appointment> findAllForTodayByPetId(Id<Pet> petId);

    Optional<Appointment> findById(Id<Appointment> appointmentId);
}

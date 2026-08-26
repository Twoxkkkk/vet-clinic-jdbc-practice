package domain.repository;

import application.appointment.dto.AppointmentDetailsDto;
import application.appointment.dto.AppointmentPlannedWithinIntervalDto;
import application.appointment.dto.ProcedureInfo;
import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.shared.Id;
import domain.vet.Vet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends Repository<Appointment>{

    List<Appointment> findAllByVetIdAndDateTime(Id<Vet> vetId, LocalDateTime dateTime);

    List<Appointment> findAllForTodayByVetId(Id<Vet> vetId);
    List<Appointment> findAllForTodayByPetOwnerId(Id<PetOwner> petOwnerId);

    Optional<Appointment> findPlannedByPetOwnerIdAndDateTime(Id<PetOwner> petOwnerId, LocalDateTime dateTime);

    List<AppointmentPlannedWithinIntervalDto> getAmountPlannedTodayWithInterval(int intervalInMinutes, int amount);

    Optional<AppointmentDetailsDto> getDetailsById(Id<Appointment> appointmentId);
    List<ProcedureInfo> getAllProcedures();

}

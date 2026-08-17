package application;

import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.repository.AppointmentRepository;
import domain.shared.Id;
import domain.shared.ProcedureId;
import domain.vet.Vet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService extends BaseService<Appointment, AppointmentRepository>{

    public AppointmentService(RepositoryFactory<AppointmentRepository> repositoryFactory){
        super(repositoryFactory);
    }

    public Id<Appointment> scheduleAppointment(Id<Pet> petId, Id<Vet> vetId, ProcedureId procedureId, LocalDateTime dateTime){

        Id<Appointment> appointmentId = Id.generate();

        Appointment appointment = new Appointment(
            appointmentId,
            vetId,
            petId,
            dateTime,
            AppointmentStatus.PLANNED,
            procedureId
        );

        repository.save(appointment);
        return appointmentId;
    }

    public void rescheduleAppointment(Id<Appointment> appointmentId, LocalDateTime dateTime){
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        if(dateTime == null || dateTime.isBefore(startOfDay))
            throw new IllegalArgumentException("Invalid time for appointment reschedule!");

        Appointment appointment = this.getById(appointmentId);

        if (appointment.getStatus() == AppointmentStatus.FINISHED)
            throw new IllegalArgumentException("Cannot reschedule the appointment that has been already finished!");

        appointment.setDateTimeOfAppointment(dateTime);

        repository.save(appointment);
    }

    public void cancelAppointment(Id<Appointment> appointmentId){
        Appointment appointment = this.getById(appointmentId);

        appointment.setStatus(AppointmentStatus.CANCELED);

        repository.save(appointment);
    }

    public List<Appointment> getAllByVetIdWithStatus(Id<Vet> vetId, AppointmentStatus status){
        return repository.findAllByVetIdWithStatus(vetId, status);
    }

    public List<Appointment> getAllByPetIdWithStatus(Id<Pet> petId, AppointmentStatus status){
        return repository.findAllByPetIdWithStatus(petId, status);
    }

    public List<Appointment> getAllForTodayByVetId(Id<Vet> vetId){
        return repository.findAllForTodayByVetId(vetId);
    }

    public List<Appointment> getAllForTodayByPetId(Id<Pet> petId){
        return repository.findAllForTodayByPetId(petId);
    }

}

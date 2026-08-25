package application.appointment;

import application.shared.BaseService;
import application.shared.RepositoryFactory;
import application.appointment.dto.AppointmentDetailsDto;
import application.appointment.dto.AppointmentOverdueDto;
import application.shared.NotificationSender;
import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.repository.AppointmentRepository;
import domain.shared.Email;
import domain.shared.Id;
import domain.shared.ProcedureId;
import domain.vet.Vet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AppointmentService extends BaseService<Appointment, AppointmentRepository> {

    private static final DateTimeFormatter dateTimeHumanizeFormatter = DateTimeFormatter.ofPattern("d MMMM HH:mm", new Locale("ru"));

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

    public void finishAppointment(Id<Appointment> appointmentId){
        Appointment appointment = this.getById(appointmentId);

        appointment.setStatus(AppointmentStatus.FINISHED);

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

    public void notifyAboutMissedAppointments(NotificationSender<Email> notificationSender){

        LocalDateTime today = LocalDate.now().atStartOfDay();

        List<AppointmentOverdueDto> missedAppointments = repository.getAllPlannedBeforeDate(today);

        if (missedAppointments.isEmpty()) return;

        for(AppointmentOverdueDto appointmentOverdueDto: missedAppointments){
            String formattedDateTimeOfAppointment = dateTimeHumanizeFormatter
                    .format(appointmentOverdueDto.appointment().getDateTimeOfAppointment());

            String body = String.format(
                "Дорогой, %s%nСпешим вам сообщить, что вы не пришли на приём, запланированный на %s.%nВы можете перезаписаться в приложении.",
                appointmentOverdueDto.ownerInitials(), formattedDateTimeOfAppointment
            );

            notificationSender.send(
                appointmentOverdueDto.ownerEmail(),
        "Пропущенный приём.",
                body
            );
        }

    }

    public String getAppointmentDetails(Id<Appointment> appointmentId){

        Optional<AppointmentDetailsDto> detailsDto = repository.getDetailsById(appointmentId);

        if(detailsDto.isPresent()){

            AppointmentDetailsDto details = detailsDto.get();

            return String.format(
                "[%s][%s][Статус: %s]%nВетеринар: %s (%s)%nПитомец: %s (%s)%nХозяин: %s%nТелефон: %s%n",
                dateTimeHumanizeFormatter.format(details.appointmentDateTime()), details.procedureType(), details.appointmentStatus().alias,
                details.vetInitials(), details.vetSpecialization().alias, details.petNickname(), details.petType(),
                details.ownerInitials(), details.ownerContactNumber()
            );
        }

        throw new IllegalArgumentException(
            "Couldn't get details for appointment with id: " + appointmentId
        );
    }

}

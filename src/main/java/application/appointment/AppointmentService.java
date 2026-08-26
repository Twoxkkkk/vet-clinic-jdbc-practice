package application.appointment;

import application.appointment.dto.ProcedureInfo;
import application.shared.BaseService;
import application.appointment.dto.AppointmentDetailsDto;
import application.appointment.dto.AppointmentPlannedWithinIntervalDto;
import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.repository.AppointmentRepository;
import domain.shared.Id;
import domain.shared.ProcedureId;
import domain.vet.Vet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AppointmentService extends BaseService<Appointment, AppointmentRepository> {

    private static final DateTimeFormatter dateTimeHumanizeFormatter = DateTimeFormatter.ofPattern("d MMMM HH:mm", new Locale("ru"));

    private static List<ProcedureInfo> cachedProceduresInfo;

    public AppointmentService(AppointmentRepository repository){
        super(repository);
    }

    public void scheduleAppointment(Id<Pet> petId, Id<Vet> vetId, ProcedureId procedureId, LocalDateTime dateTime){

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
    }

    public void cancelAppointment(Id<Appointment> appointmentId){
        Appointment appointment = this.getById(appointmentId);

        appointment.setStatus(AppointmentStatus.CANCELED);

        repository.save(appointment);
    }

    public List<Appointment> getAllByVetIdAndDateTime(Id<Vet> vetId, LocalDateTime  dateTime){
        return repository.findAllByVetIdAndDateTime(vetId, dateTime);
    }

    public List<Appointment> getAllForTodayByVetId(Id<Vet> vetId){
        return repository.findAllForTodayByVetId(vetId);
    }


    public List<Appointment> getAllForTodayByPetOwnerId(Id<PetOwner> petOwnerId){
        return repository.findAllForTodayByPetOwnerId(petOwnerId);
    }

    public List<ProcedureInfo> getAllProcedures(){

        if(cachedProceduresInfo != null && !cachedProceduresInfo.isEmpty())
            return cachedProceduresInfo;


        cachedProceduresInfo = repository.getAllProcedures();

        return repository.getAllProcedures();
    }

    public String getAllProceduresInfo(){
        List<ProcedureInfo> infos = getAllProcedures();
        StringBuilder result = new StringBuilder();

        for (ProcedureInfo dto: infos){
            result.append(
                String.format(
                    "%s) %s [Цена: %s][Длительтность в минутах: %s]%n",
                    dto.id().value(), dto.name(), dto.price(), dto.durationInMinutes()
                )
            );
        }
        return result.toString();
    }

    public String formatPlannedWithinInterval(List<AppointmentPlannedWithinIntervalDto> dtos){
        StringBuilder result = new StringBuilder();

        for(AppointmentPlannedWithinIntervalDto dto: dtos){
            String formattedDateTimeOfAppointment = dateTimeHumanizeFormatter
                    .format(dto.appointment().getDateTimeOfAppointment());

            result.append(
                String.format(
                    "Владелец питомца: %s%nТелефон: %s%nПочта: %s%nЗапланированный приём на: %s%nВремя до приёма: %s минут%n%n",
                    dto.ownerInitials(), dto.ownersContactInfo().phone().getValue(), dto.ownersContactInfo().email().getValue(),
                    formattedDateTimeOfAppointment, dto.timeLeftBeforeAppointment()
                )
            );
        }

        return result.toString();
    }

    public Appointment findPlannedByPetOwnerIdAndDateTime(Id<PetOwner> petOwnerId, LocalDateTime dateTime){
        return repository.findPlannedByPetOwnerIdAndDateTime(petOwnerId, dateTime).orElseThrow(
            () -> new IllegalArgumentException("Couldn't get certain appointment with given date time and pet owner!")
        );
    }

    public List<AppointmentPlannedWithinIntervalDto> getAllWithinInterval(int interval, int amount){
        return repository.getAmountPlannedTodayWithInterval(interval, amount);
    }

    public String getAppointmentDetails(Id<Appointment> appointmentId){

        Optional<AppointmentDetailsDto> detailsDto = repository.getDetailsById(appointmentId);

        if(detailsDto.isPresent()){

            AppointmentDetailsDto details = detailsDto.get();

            String result = String.format(
                "[%s][%s][Статус: %s]%nВетеринар: %s (%s)%nПитомец: %s (%s)%nХозяин: %s%nТелефон: %s%n",
                dateTimeHumanizeFormatter.format(details.appointmentDateTime()), details.procedureType(), details.appointmentStatus().alias,
                details.vetInitials(), details.vetSpecialization().alias, details.petNickname(), details.petType(),
                details.ownerInitials(), details.ownerContactNumber()
            );

            if(details.diagnosis() != null)
                result += String.format(
                    "Во время приёма была произведена запись в историю болезней:%nДиагноз: %s%nЛечение: %s%n",
                    details.diagnosis(), details.treatment()
                );

            return result;
        }

        throw new IllegalArgumentException(
            "Couldn't get details for appointment with id: " + appointmentId
        );
    }

}

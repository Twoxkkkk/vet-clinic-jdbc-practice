package application.vet;

import application.shared.BaseService;
import application.shared.RepositoryFactory;
import application.vet.dto.VetPerformanceDto;
import domain.repository.VetRepository;
import domain.shared.*;
import domain.vet.Vet;
import domain.vet.VetSpecialization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


public class VetService extends BaseService<Vet, VetRepository> {

    public VetService(RepositoryFactory<VetRepository> repositoryFactory){
        super(repositoryFactory);
    }

    public Id<Vet> registerVet(String firstName, String lastName, ContactInfo contactInfo, VetSpecialization specialization){

        if (repository.findByPhoneNumber(contactInfo.phone()).isPresent()) {
            throw new IllegalArgumentException("Ветеринар с таким номером телефона уже зарегистрирован!");
        }
        if (repository.findByEmail(contactInfo.email()).isPresent()) {
            throw new IllegalArgumentException("Ветеринар с таким email уже зарегистрирован!");
        }

        Id<Vet> vetId = Id.generate();

        Vet vet = new Vet(
            vetId,
            firstName,
            lastName,
            specialization,
            contactInfo
        );

        repository.save(vet);
        return vetId;
    }

    public Vet findByPhoneNumber(Phone number){
        return repository.findByPhoneNumber(number).orElseThrow(
            () -> new IllegalArgumentException("Couldn't find the pet owner with the given phone number!")
        );
    }

    public Vet findByEmail(Email email){
        return repository.findByEmail(email).orElseThrow(
            () -> new IllegalArgumentException("Couldn't find the pet owner with the given email!")
        );
    }


    public List<Vet> findAllAvailableForTimeBySpecializationAndProcedure(VetSpecialization specialization, LocalDateTime dateTime){
        return repository.findAllAvailableForTimeBySpecializationAndProcedure(specialization, dateTime);
    }

    public String formatMonthlyVetsPerformanceReport(){

        LocalDate today = LocalDate.now();

        LocalDate startOfAMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfAMonth = today.with(TemporalAdjusters.firstDayOfNextMonth());

        List<VetPerformanceDto> vetsPerformanceDtos = repository.getVetsPerformanceReport(startOfAMonth, endOfAMonth);

        if(vetsPerformanceDtos.isEmpty()) return "";

        StringBuilder result = new StringBuilder();

        for(VetPerformanceDto dto: vetsPerformanceDtos){
            result.append(
                String.format(
                    "Инициалы: %s%nКоличество завершенных приёмов: %d%nИтоговый доход: %s%n%n",
                    dto.vetInitials(), dto.appointmentsCount(), dto.totalRevenue()
                )
            );
        }

        return result.toString();
    }

}

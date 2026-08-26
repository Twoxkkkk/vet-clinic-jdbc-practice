package application.vet;

import application.shared.BaseService;
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

    public VetService(VetRepository repository){
        super(repository);
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
        return repository.findAllAvailableForTimeBySpecialization(specialization, dateTime);
    }

    public String formatMonthlyVetsPerformanceReport(){

        LocalDate today = LocalDate.now();

        LocalDate startOfAMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfAMonth = today.with(TemporalAdjusters.firstDayOfNextMonth());

        List<VetPerformanceDto> vetsPerformanceDtos = repository.getVetsPerformanceReport(startOfAMonth, endOfAMonth);

        if(vetsPerformanceDtos.isEmpty()) return null;

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

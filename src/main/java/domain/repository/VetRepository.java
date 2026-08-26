package domain.repository;

import application.vet.dto.VetPerformanceDto;
import domain.shared.Email;
import domain.shared.Phone;
import domain.vet.Vet;
import domain.vet.VetSpecialization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface VetRepository extends Repository<Vet>{

    Optional<Vet> findByPhoneNumber(Phone phone);
    Optional<Vet> findByEmail(Email email);

    List<Vet> findAllAvailableForTimeBySpecialization(VetSpecialization specialization, LocalDateTime dateTime);

    List<VetPerformanceDto> getVetsPerformanceReport(LocalDate start, LocalDate end);

}

import application.shared.RepositoryFactory;
import application.vet.VetService;
import domain.repository.VetRepository;
import domain.vet.VetSpecialization;
import infrastructure.factory.VetRepositoryFactory;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        RepositoryFactory<VetRepository> factory = new VetRepositoryFactory();

        VetService service = new VetService(factory);

        System.out.println(service.formatMonthlyVetsPerformanceReport());

        LocalDateTime localDateTime = LocalDateTime.of(2026, 12, 12, 12, 30);

        System.out.println(service.findAllAvailableForTimeBySpecializationAndProcedure(VetSpecialization.THERAPIST, localDateTime));
    }
}

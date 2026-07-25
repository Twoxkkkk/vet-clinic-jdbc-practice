package domain.repository;

import domain.shared.Id;
import domain.vet.Vet;

import java.util.Optional;

public interface VetRepository {

    void save(Vet vet);
    void delete(Id<Vet> vetId);

    Optional<Vet> findById(Id<Vet> vetId);
}

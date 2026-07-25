package domain.repository;

import domain.pet_owner.PetOwner;
import domain.shared.Id;

import java.util.Optional;

public interface PetOwnerRepository {

    void save(PetOwner petOwner);
    void delete(Id<PetOwner> petOwnerId);

    Optional<PetOwner> findById(Id<PetOwner> petOwnerId);
}

package domain.repository;

import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.shared.Email;
import domain.shared.Id;
import domain.shared.Phone;

import java.util.Optional;

public interface PetOwnerRepository {

    void save(PetOwner petOwner);
    void delete(Id<PetOwner> petOwnerId);

    Optional<PetOwner> findById(Id<PetOwner> petOwnerId);
    Optional<PetOwner> findByPhoneNumber(Phone number);
    Optional<PetOwner> findByEmail(Email email);

    Optional<PetOwner> findByPetId(Id<Pet> petId);
}

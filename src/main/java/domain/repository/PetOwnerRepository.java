package domain.repository;

import domain.pet_owner.PetOwner;
import domain.shared.Email;
import domain.shared.Id;
import domain.shared.Phone;

import java.util.Optional;

public interface PetOwnerRepository extends Repository<PetOwner> {

    Optional<PetOwner> findById(Id<PetOwner> petOwnerId);
    Optional<PetOwner> findByPhoneNumber(Phone number);
    Optional<PetOwner> findByEmail(Email email);

}

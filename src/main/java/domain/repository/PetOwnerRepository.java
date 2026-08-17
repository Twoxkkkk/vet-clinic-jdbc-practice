package domain.repository;

import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.shared.Email;
import domain.shared.Id;
import domain.shared.Phone;

public interface PetOwnerRepository extends Repository<PetOwner> {

public interface PetOwnerRepository {

    Optional<PetOwner> findById(Id<PetOwner> petOwnerId);
    Optional<PetOwner> findByPhoneNumber(Phone number);
    Optional<PetOwner> findByEmail(Email email);

}

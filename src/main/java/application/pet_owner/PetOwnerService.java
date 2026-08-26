package application.pet_owner;

import application.shared.BaseService;
import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.pet_owner.PetSex;
import domain.repository.PetOwnerRepository;
import domain.shared.*;

import java.time.LocalDate;

public class PetOwnerService extends BaseService<PetOwner, PetOwnerRepository> {

    public PetOwnerService(PetOwnerRepository repository){
        super(repository);
    }

    public void registerPetOwner(String firstName, String lastName, Address address, ContactInfo contactInfo){

        if (repository.findByPhoneNumber(contactInfo.phone()).isPresent()) {
            throw new IllegalArgumentException("Клиент с таким номером телефона уже зарегистрирован!");
        }
        if (repository.findByEmail(contactInfo.email()).isPresent()) {
            throw new IllegalArgumentException("Клиент с таким email уже зарегистрирован!");
        }

        Id<PetOwner> petOwnerId = Id.generate();

        PetOwner petOwner = new PetOwner(
            petOwnerId,
            firstName,
            lastName,
            address,
            contactInfo,
            LocalDate.now()
        );

        repository.save(petOwner);
    }

    public BreedId getBreedIdByName(String name){
        return this.repository.findBreedIdByStringPattern(name).orElseThrow(
            () -> new IllegalArgumentException("Couldn't find breed with specific name!")
        );
    }



    public void registerPet(Id<PetOwner> petOwnerId, String nickname, LocalDate dateOfBirth, PetSex sex, double weight, BreedId breedId){

        PetOwner petOwner = this.getById(petOwnerId);

        Id<Pet> petId = Id.generate();

        petOwner.addPet(
            petId,
            nickname,
            dateOfBirth,
            sex,
            weight,
            breedId
        );

        repository.save(petOwner);
    }

    public PetOwner findByPhoneNumber(Phone number){
        return repository.findByPhoneNumber(number).orElseThrow(
            () -> new IllegalArgumentException("Couldn't find the pet owner with the given phone number!")
        );
    }

    public PetOwner findByEmail(Email email){
        return repository.findByEmail(email).orElseThrow(
            () -> new IllegalArgumentException("Couldn't find the pet owner with the given email!")
        );
    }
}

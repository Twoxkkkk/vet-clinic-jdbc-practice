package application.pet_owner;

import application.shared.BaseService;
import application.shared.RepositoryFactory;
import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.pet_owner.PetSex;
import domain.repository.PetOwnerRepository;
import domain.shared.*;

import java.time.LocalDate;

public class PetOwnerService extends BaseService<PetOwner, PetOwnerRepository> {

    public PetOwnerService(RepositoryFactory<PetOwnerRepository> repositoryFactory){
        super(repositoryFactory);
    }

    public PetOwner getByPetId(Id<Pet> petId){
        return repository.findByPetId(petId).orElseThrow(
            () -> new IllegalArgumentException("Invalid pet id!")
        );
    }

    public Id<PetOwner> registerPetOwner(String firstName, String lastName, Address address, ContactInfo contactInfo){

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
        return petOwnerId;
    }

    public Id<Pet> registerPet(Id<PetOwner> petOwnerId, String nickname, LocalDate dateOfBirth, PetSex sex, double weight, BreedId breedId){

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
        return petId;
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

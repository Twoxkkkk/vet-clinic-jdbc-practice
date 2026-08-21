package application;

import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.repository.PetOwnerRepository;
import domain.shared.Id;

public class PetOwnerService extends BaseService<PetOwner, PetOwnerRepository>{

    public PetOwnerService(RepositoryFactory<PetOwnerRepository> repositoryFactory){
        super(repositoryFactory);
    }

    public PetOwner getByPetId(Id<Pet> petId){
        return repository.findByPetId(petId).orElseThrow(
            () -> new IllegalArgumentException("Invalid pet id!")
        );
    }

}

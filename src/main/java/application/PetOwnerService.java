package application;

import domain.pet_owner.PetOwner;
import domain.repository.PetOwnerRepository;

public class PetOwnerService extends BaseService<PetOwner, PetOwnerRepository>{

    public PetOwnerService(RepositoryFactory<PetOwnerRepository> repositoryFactory){
        super(repositoryFactory);
    }

}

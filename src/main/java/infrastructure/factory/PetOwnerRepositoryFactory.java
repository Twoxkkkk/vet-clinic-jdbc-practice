package infrastructure.factory;

import application.RepositoryFactory;
import domain.repository.PetOwnerRepository;
import infrastructure.database.PetOwnerRepositoryImpl;

public class PetOwnerRepositoryFactory implements RepositoryFactory<PetOwnerRepository> {

    @Override
    public PetOwnerRepository create() {
        return new PetOwnerRepositoryImpl();
    }

}

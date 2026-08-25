package infrastructure.factory;

import application.shared.RepositoryFactory;
import domain.repository.VetRepository;
import infrastructure.database.VetRepositoryImpl;

public class VetRepositoryFactory implements RepositoryFactory<VetRepository> {

    @Override
    public VetRepository create() {
        return new VetRepositoryImpl();
    }
}

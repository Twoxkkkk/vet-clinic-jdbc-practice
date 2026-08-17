package infrastructure.factory;

import application.RepositoryFactory;
import domain.repository.AppointmentRepository;
import infrastructure.database.AppointmentRepositoryImpl;

public class AppointmentRepositoryFactory implements RepositoryFactory<AppointmentRepository> {

    @Override
    public AppointmentRepository create() {
        return new AppointmentRepositoryImpl();
    }
}

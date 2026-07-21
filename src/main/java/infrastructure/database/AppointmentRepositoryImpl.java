package infrastructure.database;

import domain.appointment.Appointment;
import domain.repository.AppointmentRepository;
import domain.shared.Id;

import java.util.Optional;

//TODO
public class AppointmentRepositoryImpl implements AppointmentRepository {

    @Override
    public void save(Appointment appointment) {

    }

    @Override
    public void delete(Id<Appointment> appointmentId) {

    }

    @Override
    public Optional<Appointment> findById(Id<Appointment> appointmentId) {
        return Optional.empty();
    }
}

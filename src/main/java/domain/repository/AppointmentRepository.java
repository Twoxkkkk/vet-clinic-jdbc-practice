package domain.repository;

import domain.appointment.Appointment;
import domain.shared.Id;

import java.util.Optional;

public interface AppointmentRepository {
    void save(Appointment appointment);
    void delete(Id<Appointment> appointmentId);

    Optional<Appointment> findById(Id<Appointment> appointmentId);
}

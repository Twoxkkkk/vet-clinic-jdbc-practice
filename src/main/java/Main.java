import application.appointment.AppointmentService;
import domain.appointment.Appointment;
import domain.shared.Id;
import infrastructure.factory.AppointmentRepositoryFactory;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        AppointmentRepositoryFactory factory = new AppointmentRepositoryFactory();

        AppointmentService service = new AppointmentService(factory);

        String app = service.getAppointmentDetails(new Id<>(UUID.fromString("77777777-7777-7777-7777-777777777777")));

        System.out.println(app);

    }
}

package presentation.cli.controllers;

import application.appointment.dto.AppointmentPlannedWithinIntervalDto;
import presentation.cli.shared.ServicesFactory;

import java.util.List;
import java.util.Scanner;

public class AppointmentsController extends BaseController{
    public AppointmentsController(Scanner scanner, ServicesFactory services) {
        super(scanner, services);
    }

    public void printOutAppointmentsWithin(){
        while (true){
            try {
                System.out.print("Введите в течении скольки минут рассматривать приёмы: ");
                int minutes = Integer.parseInt(scanner.nextLine());

                System.out.print("Введите количество выводимых приёмов: ");
                int amount = Integer.parseInt(scanner.nextLine());

                if(minutes <= 0 || amount <= 0)
                    throw new IllegalArgumentException();

                List<AppointmentPlannedWithinIntervalDto> appointments = services.appointments()
                        .getAllWithinInterval(minutes, amount);

                if(appointments.isEmpty()){
                    System.out.println("Таковых не нашлось!");
                    return;
                }

                System.out.println(
                    services.appointments().formatPlannedWithinInterval(appointments)
                );

                return;

            } catch (Exception e){
                System.out.println("Проверьте правильность входящих данных!");
            }
        }
    }
}

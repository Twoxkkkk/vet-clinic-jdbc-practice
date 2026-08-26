package presentation.cli.controllers;

import domain.appointment.Appointment;
import domain.shared.Email;
import domain.shared.Phone;
import domain.shared.exceptions.ValidationException;
import domain.vet.Vet;
import presentation.cli.shared.ServicesFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class VetsController extends BaseController{
    public VetsController(Scanner scanner, ServicesFactory services) {
        super(scanner, services);
    }

    public void printOutVetsPerformanceReport(){
        System.out.println(
            services.vets().formatMonthlyVetsPerformanceReport()
        );
    }

    public void printOutAllAppointmentsForToday(Vet vet){
        this.services.appointments()
                .getAllForTodayByVetId(vet.getId())
                .forEach(appointment -> {
                    System.out.println(services.appointments().getAppointmentDetails(appointment.getId()));
                });
    }

    public void printOutAppointmentInfoByDate(Vet vet){
        while (true){
            try {
                System.out.print("Введите дату и время приёма[дд.мм.гггг чч:мм]: ");

                LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

                List<Appointment> appointments = services.appointments()
                        .getAllByVetIdAndDateTime(vet.getId(), dateTime);

                if(appointments.isEmpty()){
                    System.out.println("В эту дату и время у вас не было приёма!");
                    return;
                }

                for(Appointment appointment: appointments){
                    System.out.println(services.appointments().getAppointmentDetails(appointment.getId()));
                }
                return;

            } catch (Exception e){
                System.out.println("Проверьте правильность входящих данных");
            }
        }
    }

    public void manipulateVet(Vet vet){
        System.out.println("Выбранный ветеринар:");
        System.out.println(vet);

        int input;

        while (true){
            System.out.println("\nВыберите действие:");
            System.out.println("""
            1) Посмотреть все запланнированные приёмы на сегодня
            2) Посмотреть всю информацию о приёмах по дате
            3) Выйти из просмотра карточки ветеринара
            """);

            input = Integer.parseInt(scanner.nextLine());

            switch (input){
                case 1:
                    printOutAllAppointmentsForToday(vet);
                    break;
                case 2:
                    printOutAppointmentInfoByDate(vet);
                    break;
                case 3:
                    return;
            }
        }
    }

    public void findVetByContacts(){
        while (true){
            try {
                System.out.print("Введите почту/телефон ветеринара: ");

                String contact = scanner.nextLine();

                if(contact.contains("+")){
                    Phone phone = new Phone(contact);
                    manipulateVet(services.vets().findByPhoneNumber(phone));

                    return;
                }

                Email email = new Email(contact);
                manipulateVet(services.vets().findByEmail(email));

                return;

            } catch (ValidationException e) {
                System.out.println("Проверьте правильность написания данных!");
            } catch (Exception e){
                System.out.println("Не смогли найти ветеринара по данному контакту!");
                return;
            }
        }
    }
}

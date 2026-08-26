package presentation.cli;

import presentation.cli.controllers.AppointmentsController;
import presentation.cli.controllers.MedicalRecordsController;
import presentation.cli.controllers.PetOwnersController;
import presentation.cli.controllers.VetsController;
import presentation.cli.shared.ServicesFactory;

import java.util.Scanner;

public class MainConsoleController {

    private static boolean running = true;
    private static final Scanner scanner = new Scanner(System.in);

    private final PetOwnersController petOwnerController;
    private final VetsController vetController;
    private final MedicalRecordsController medicalRecordController;
    private final AppointmentsController appointmentController;

    public MainConsoleController(ServicesFactory servicesFactory) {
        this.petOwnerController = new PetOwnersController(scanner, servicesFactory);
        this.vetController = new VetsController(scanner, servicesFactory);
        this.medicalRecordController = new MedicalRecordsController(scanner, servicesFactory);
        this.appointmentController = new AppointmentsController(scanner, servicesFactory);
    }

    private void printOutControls(){
        System.out.println("""
        Введите цифру чтоб начать выполнять сооствествующее действие:
        
        1) Создать нового клиента
        2) Найти клиента по контактным данным
        3) Найти ветеринара по контактным данным
        4) Вывести отчёт по производительности работников за этот месяц
        5) Вывести отчёт по поставленным диагнозам за этот месяц
        6) Вывести число грядущих приёмов в течении данного времени для обзвона клиентов
        7) Завершить выполение программы
        
        """);
    }

    private void handleInput(int input){
        switch (input){
            case 1:
                petOwnerController.createUser();
                break;

            case 2:
                petOwnerController.findUserByContacts();
                break;

            case 3:
                vetController.findVetByContacts();
                break;

            case 4:
                vetController.printOutVetsPerformanceReport();
                break;

            case 5:
                medicalRecordController.printOutTopDiagnosises();
                break;

            case 6:
                appointmentController.printOutAppointmentsWithin();
                break;

            case 7:
                running = false;
                break;

            default:
                System.out.println("Неизвестная нахуй цифра");
                break;
        }
    }

    public void start(){
        while (running){
            printOutControls();

            handleInput(Integer.parseInt(scanner.nextLine()));
        }
    }

}

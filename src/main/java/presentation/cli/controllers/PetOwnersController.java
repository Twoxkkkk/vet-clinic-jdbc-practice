package presentation.cli.controllers;

import application.appointment.dto.ProcedureInfo;
import domain.appointment.Appointment;
import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.pet_owner.PetSex;
import domain.shared.*;
import domain.shared.exceptions.ValidationException;
import domain.vet.Vet;
import presentation.cli.shared.ServicesFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PetOwnersController extends BaseController{


    public PetOwnersController(Scanner scanner, ServicesFactory services) {
        super(scanner, services);
    }

    public void createUser(){
        String firstName = null;
        String lastName = null;
        Address address = null;
        Phone phone = null;
        Email email = null;

        while (true){
            try {
                if (firstName == null){
                    System.out.print("Введите имя: ");
                    firstName = this.scanner.nextLine();
                }
                else if (lastName == null){
                    System.out.print("Введите фамилию: ");
                    lastName = this.scanner.nextLine();
                }
                else if (address == null){
                    System.out.print("Введите адрес: ");
                    address = new Address(this.scanner.nextLine());
                }
                else if (phone == null){
                    System.out.print("Введите номер телефона: ");
                    phone = new Phone(this.scanner.nextLine());
                }
                else if (email == null){
                    System.out.print("Введите электронную почту: ");
                    email = new Email(this.scanner.nextLine());
                } else {
                    this.services.petOwners().registerPetOwner(
                        firstName,
                        lastName,
                        address,
                        new ContactInfo(phone, email)
                    );
                    System.out.println("Пользователь успешно создан!\n");
                    return;
                }
            } catch (Exception e){
                System.out.println("Проверьте правильность написания значений!");
            }
        }
    }

    public void createAndAddPet(PetOwner client){
        String nickname = null;
        LocalDate dateOfBirth = null;
        PetSex sex = null;
        double weight = -1.0;
        BreedId breedId = null;

        while (true){
            try {
                if (nickname == null){
                    System.out.print("Введите кличку: ");
                    nickname = this.scanner.nextLine();
                }
                else if (dateOfBirth == null){
                    System.out.print("Введите дату рождения[дд.мм.гггг]: ");
                    dateOfBirth = LocalDate.parse(this.scanner.nextLine());
                }
                else if (sex == null){
                    System.out.print("Введите пол: ");
                    for(PetSex petSex: PetSex.values()){
                        if(petSex.alias.equalsIgnoreCase(scanner.nextLine())){
                            sex = petSex;
                        }
                    }
                }
                else if (weight == -1.0){
                    System.out.print("Введите вес в кг: ");
                    weight = Integer.parseInt(this.scanner.nextLine());
                }
                else if (breedId == null){
                    System.out.print("Введите породу: ");
                    breedId = this.services.petOwners().getBreedIdByName(this.scanner.nextLine());
                    System.out.println(breedId);
                } else {
                    this.services.petOwners().registerPet(
                        client.getId(),
                        nickname,
                        dateOfBirth,
                        sex,
                        weight,
                        breedId
                    );
                    System.out.println("Питомец успешно создан и добавлен выбранному клиенту!\n");
                    System.out.println("Данные просмотра карточки клиента возможно устарели!");
                    return;
                }
            } catch (Exception e){
                System.out.println("Проверьте правильность написания значений!");
                throw new RuntimeException(e);
            }
        }
    }

    public void printOutAllAppointmentsForToday(PetOwner client){
        this.services.appointments()
         .getAllForTodayByPetOwnerId(client.getId())
         .forEach(appointment -> {
             System.out.println(services.appointments().getAppointmentDetails(appointment.getId()));
         });
    }

    public void printOutAllPets(PetOwner client){

        if(client.getPets().isEmpty()) {
            System.out.println("Питомцев нету!");
            return;
        }

        int counter = 1;
        for (Pet pet: client.getPets()){
            System.out.printf("%d) %s%n", counter++, pet);
        }
    }

    public void scheduleAnAppointmentForPet(PetOwner client){
        System.out.println("Выберите порядковый номер питомца для записи:\n");

        this.printOutAllPets(client);

        int petsCount = client.getPets().size();

        Id<Pet> petId = null;
        Id<Vet> vetId = null;
        ProcedureId procedureId = null;
        LocalDateTime dateTime = null;

        List<ProcedureInfo> procedures = services.appointments().getAllProcedures();

        while (true){
            try {
                if(petId == null){
                    int petIndex = Integer.parseInt(scanner.nextLine());

                    if(petIndex > petsCount || petIndex < 1)
                        throw new IllegalArgumentException();

                    petId = client.getPets().get(petIndex - 1).getId();
                }
                else if (procedureId == null) {
                    System.out.println(services.appointments().getAllProceduresInfo());
                    System.out.print("Выберите необходимую процедуру: ");

                    int procedureIndex = Integer.parseInt(scanner.nextLine());

                    if(procedureIndex < 1 || procedureIndex > procedures.size())
                        throw new IllegalArgumentException();

                    procedureId = new ProcedureId(procedureIndex-1);


                } else if (dateTime == null) {
                    System.out.print("Введите дату и время приёма[дд.мм.гггг чч:мм]: ");

                    dateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                } else if (vetId == null) {
                    List<Vet> vetsAvailable = services.vets()
                        .findAllAvailableForTimeBySpecializationAndProcedure(
                            procedures.get(procedureId.value()).vetSpecialization(),
                            dateTime
                    );
                    if(vetsAvailable.isEmpty()){
                        System.out.println("Подходящих специалистов на это время нет! Попробуйте другую дату или время.");

                        dateTime = null;
                        continue;
                    }
                    Vet vet = vetsAvailable.getFirst();

                    System.out.println("Вас примит:");
                    System.out.println(vet);

                    vetId = vet.getId();

                } else {
                    services.appointments().scheduleAppointment(
                        petId, vetId, procedureId, dateTime
                    );

                    System.out.println("Успешно! Ввы записаны на приём!");
                    return;
                }

            } catch (Exception e){
                System.out.println("Проверьте правильность написания данных!");
                throw new RuntimeException(e);
            }
        }
    }

    public void manipulateClient(PetOwner client){

        System.out.println("Выбранный клиент:");
        System.out.println(client);

        int input;

        while (true){
            System.out.println("\nВыберите действие:");
            System.out.println("""
            1) Добавить питомца
            2) Вывести всех питомцев
            3) Посмотреть все запланнированные приёмы на сегодня
            4) Записать питомца на приём
            5) Отменить приём
            6) Выйти из просмотра клиента
            """);

            input = Integer.parseInt(scanner.nextLine());

            switch (input){
                case 1:
                    createAndAddPet(client);
                    break;
                case 2:
                    printOutAllPets(client);
                    break;
                case 3:
                    printOutAllAppointmentsForToday(client);
                    break;
                case 4:
                    scheduleAnAppointmentForPet(client);
                    break;
                case 5:
                    cancelAppointment(client);
                    break;
                case 6:
                    return;
            }
        }
    }

    public void cancelAppointment(PetOwner client){

        Appointment appointment = null;

        while (true){
            try {
                if(appointment == null){
                    System.out.print("Введите дату и время приёма который хотите отменить: ");

                    LocalDateTime dateTime = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

                    appointment = services.appointments()
                            .findPlannedByPetOwnerIdAndDateTime(client.getId(), dateTime);

                    System.out.println("Приём найден:");
                }

                System.out.println("Вы уверены что хотите отменить приём?[да, нет]: ");

                String input = scanner.nextLine();
                if(input.equalsIgnoreCase("да")){
                    services.appointments().cancelAppointment(appointment.getId());

                    System.out.println("Приём отменен.");
                    return;
                } else if (input.equalsIgnoreCase("нет")) {
                    return;
                }
            } catch (Exception e){
                System.out.println("Проверьте правильность входных данных!");
            }
        }
    }

    public void findUserByContacts(){
        while (true){
            try {
                System.out.print("Введите почту/телефон клиента: ");

                String contact = scanner.nextLine();

                if(contact.contains("+")){
                    Phone phone = new Phone(contact);
                    manipulateClient(services.petOwners().findByPhoneNumber(phone));

                    return;
                }

                Email email = new Email(contact);
                manipulateClient(services.petOwners().findByEmail(email));

                return;

            } catch (ValidationException e) {
                System.out.println("Проверьте правильность написания данных!");
            } catch (Exception e){
                System.out.println("Не смогли найти клиента по данному контакту!");
                return;
            }
        }
    }

}

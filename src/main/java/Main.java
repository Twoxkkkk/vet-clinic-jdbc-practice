
import domain.pet_owner.PetOwner;
import domain.repository.PetOwnerRepository;
import domain.shared.Id;
import infrastructure.factory.PetOwnerRepositoryFactory;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        PetOwnerRepository petOwnerRepository = new PetOwnerRepositoryFactory().create();

        Id<PetOwner> petOwnerId = new Id<>(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        Optional<PetOwner> petOwner = petOwnerRepository.findById(petOwnerId);

        petOwner.ifPresent(petOwnerObject -> {
                System.out.printf(
                    "%s %s (%s)%n",
                    petOwnerObject.getFirstName(), petOwnerObject.getLastName(),
                    petOwnerObject.getId().value()
                );

                System.out.println("Животные:");

                petOwnerObject.getPets()
                .forEach((p)->{
                    System.out.println(p.getNickname());
                });
            }
        );

        Scanner scanner = new Scanner(System.in);

        scanner.nextLine();
    }
}

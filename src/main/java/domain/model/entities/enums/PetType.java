package domain.model.entities.enums;

public enum PetType {
    DOG("Собака"),
    CAT("Котик"),
    REPTILE("Рептилия"),
    PARROT("Попугай");

    private final String localizedAlias;

    PetType(String localizedAlias){
        this.localizedAlias = localizedAlias;
    }

}

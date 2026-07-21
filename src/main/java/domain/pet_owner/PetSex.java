package domain.pet_owner;

public enum PetSex {

    MALE("Мальчик"),
    FEMALE("Девочка"),
    UNKNOWN("Неизвестно");

    private final String alias;

    PetSex(String alias){
        this.alias = alias;
    }

}

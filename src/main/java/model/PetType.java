package model;

public enum PetType {
    DOG("Dog"),
    CAT("Cat"),
    REPTILE("Reptile"),
    PARROT("Parrot");

    private final String description;

    PetType(String description){
        this.description = description;
    }

}

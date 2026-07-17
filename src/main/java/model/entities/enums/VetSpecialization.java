package model.entities.enums;

public enum VetSpecialization {
    THERAPIST("Therapist"),
    SURGEON("Surgeon"),
    DENTIST("Dentist"),
    DIAGNOSTIC("Diagnostic");

    private final String description;

    VetSpecialization(String description){
        this.description = description;
    }
}

package domain.vet;

public enum VetSpecialization {
    THERAPIST("Терапевт"),
    SURGEON("Хирург"),
    DENTIST("Дантист"),
    DIAGNOSTIC("Диагностик");

    public final String alias;

    VetSpecialization(String alias){
        this.alias = alias;
    }
}
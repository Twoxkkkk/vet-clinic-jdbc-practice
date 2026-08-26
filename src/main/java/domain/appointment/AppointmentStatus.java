package domain.appointment;

public enum AppointmentStatus {

    PLANNED("Запланирован"),
    FINISHED("Завершен"),
    CANCELED("Отменен");

    public final String alias;

    AppointmentStatus(String alias){
        this.alias = alias;
    }

}

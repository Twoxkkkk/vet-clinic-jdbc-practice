package domain.appointment;

public enum AppointmentStatus {

    PLANNED("Запланирован"),
    FINISHED("Завершен"),
    CANCELED("Отменен");

    private String alias;

    AppointmentStatus(String alias){
        this.alias = alias;
    }

}

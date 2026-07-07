package model;

public class Vet extends BaseEntity{

    private String firstName;
    private String lastName;

    private VetSpecialization specialization;

    private String contactNumber;
    private double appointmentPrice;

    private Appointment[] appointments;

    public Vet(int id, String firstName, String lastName,
           VetSpecialization specialization, String contactNumber,
           double appointmentPrice, Appointment[] appointments
        ) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
        this.appointmentPrice = appointmentPrice;
        this.appointments = appointments;
    }
}

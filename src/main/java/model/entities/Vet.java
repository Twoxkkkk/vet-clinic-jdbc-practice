package model.entities;

import model.entities.base.ContactableEntity;
import model.entities.enums.VetSpecialization;
import value_objects.objects.Email;
import value_objects.objects.Phone;

public class Vet extends ContactableEntity {

    private String firstName;
    private String lastName;

    private Phone contactNumber;
    private Email email;

    private VetSpecialization specialization;


    public Vet(Long id, String firstName, String lastName,
           VetSpecialization specialization, Phone contactNumber, Email email
        ) {
        super(id, email, contactNumber);

        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setSpecialization(specialization);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank())
            return;

        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            return;

        this.lastName = lastName;
    }

    public VetSpecialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(VetSpecialization specialization) {
        this.specialization = specialization;
    }
}

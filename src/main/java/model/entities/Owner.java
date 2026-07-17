package model.entities;

import model.entities.base.BaseEntity;
import model.entities.base.ContactableEntity;
import value_objects.objects.Address;
import value_objects.objects.Email;
import value_objects.objects.Phone;

import java.time.LocalDate;

public class Owner extends ContactableEntity {

    private String firstName;
    private String lastName;

    private Phone contactNumber;
    private Address address;
    private Email email;

    private LocalDate registrationDate;

    public Owner(Long id, String firstName, String lastName, Phone contactNumber,
            Address address, Email email, LocalDate registrationDate
        ){
        super(id, email, contactNumber);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAddress(address);
        this.setRegistrationDate(registrationDate);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null)
            return;

        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank())
            return;

        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            return;

        this.lastName = lastName;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}

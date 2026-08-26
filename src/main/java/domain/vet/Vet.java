package domain.vet;

import domain.shared.ContactInfo;
import domain.shared.Id;

import java.time.format.DateTimeFormatter;

public class Vet{

    private Id<Vet> id;

    private String firstName;
    private String lastName;

    private ContactInfo contactInfo;

    private VetSpecialization specialization;

    public Vet(Id<Vet> id, String firstName, String lastName,
           VetSpecialization specialization, ContactInfo contactInfo
        ) {
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setSpecialization(specialization);
        this.setContactInfo(contactInfo);
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        if (contactInfo == null)
            return;

        this.contactInfo = contactInfo;
    }

    public Id<Vet> getId() {
        return id;
    }

    public void setId(Id<Vet> id) {
        this.id = id;
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

    @Override
    public String toString() {
        return String.format(
            "%s %s [%s][%s][%s]",
            this.getFirstName(), this.getLastName(),
            this.getSpecialization().alias,
            this.getContactInfo().email().getValue(),
            this.getContactInfo().phone().getValue()
        );
    }
}

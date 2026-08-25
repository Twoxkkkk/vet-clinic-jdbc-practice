package domain.pet_owner;

import domain.shared.Address;
import domain.shared.BreedId;
import domain.shared.ContactInfo;
import domain.shared.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PetOwner {

    private Id<PetOwner> id;

    private String firstName;
    private String lastName;

    private ContactInfo contactInfo;
    private Address address;

    private LocalDate registrationDate;

    private final List<Pet> pets = new ArrayList<>();

    public PetOwner(Id<PetOwner> id, String firstName, String lastName,
                    Address address, ContactInfo contactInfo, LocalDate registrationDate
        ){
        this.setId(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAddress(address);
        this.setContactInfo(contactInfo);
        this.setRegistrationDate(registrationDate);
    }

    public void addPet(Id<Pet> id, String nickname, LocalDate dateOfBirth, PetSex sex, double weight, BreedId breedId){
        this.pets.add(new Pet(id, nickname, dateOfBirth, this.getId(), sex, weight, breedId));
    }

    public Id<PetOwner> getId() {
        return id;
    }

    public void setId(Id<PetOwner> id) {
        this.id = id;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        if (contactInfo == null)
            return;

        this.contactInfo = contactInfo;
    }

    public List<Pet> getPets() {
        return pets;
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

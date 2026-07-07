package model;

import java.util.Date;

public class Owner extends BaseEntity{

    private String firstName;
    private String lastName;

    private String telephoneNumber;
    private String address;

    private int[] petsIds;
    private Date registrationDate;


    public Owner(int id, String firstName, String lastName, String telephoneNumber,
            String address, int[] petsIds, Date registrationDate
        ){
        super(id);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setTelephoneNumber(telephoneNumber);
        this.setAddress(address);
        this.setPetsIds(petsIds);
        this.setRegistrationDate(registrationDate);
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        if (telephoneNumber == null || telephoneNumber.isBlank())
            return;

        this.telephoneNumber = telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank())
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

    public int[] getPetsIds() {
        return petsIds;
    }

    public void setPetsIds(int[] petsIds) {
        if (petsIds == null)
            return;

        this.petsIds = petsIds;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}

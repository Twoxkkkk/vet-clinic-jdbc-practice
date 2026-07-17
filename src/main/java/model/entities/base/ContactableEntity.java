package model.entities.base;

import value_objects.objects.Email;
import value_objects.objects.Phone;

public class ContactableEntity extends BaseEntity{

    private Email email;
    private Phone contactNumber;

    public ContactableEntity(Long id, Email email, Phone contactNumber) {
        super(id);

        this.setEmail(email);
        this.setContactNumber(contactNumber);
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        if (email == null)
            return;

        this.email = email;
    }

    public Phone getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Phone contactNumber) {
        if (contactNumber == null)
            return;

        this.contactNumber = contactNumber;
    }
}

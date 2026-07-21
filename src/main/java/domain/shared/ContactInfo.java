package domain.shared;

import domain.shared.exceptions.NullOrBlankException;

public record ContactInfo(Phone phone, Email email){
    public ContactInfo{
        if(phone == null)
            throw new NullOrBlankException("Phone Cannot be Null!");
        if (email == null)
            throw new NullOrBlankException("Email Cannot be Null!");
    }
}

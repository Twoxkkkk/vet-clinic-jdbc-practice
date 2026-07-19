package domain.model.value_objects.objects;

import domain.model.value_objects.utils.ValidationException;

public class Email extends BaseValidatableVO{

    public Email(String value) throws ValidationException {
        super(value);
    }

    @Override
    public String getValidationPattern(){
        // example123@test.com
        return "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    }
}

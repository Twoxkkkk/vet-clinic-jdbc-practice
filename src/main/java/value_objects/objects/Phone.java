package value_objects.objects;

import value_objects.utils.ValidationException;

public class Phone extends BaseValidatableVO{

    public Phone(String value) throws ValidationException {
        super(value);
    }

    @Override
    public String getValidationPattern(){
        // +79999999999
        return "^\\+?[1-9]\\d{1,14}$";
    }
}

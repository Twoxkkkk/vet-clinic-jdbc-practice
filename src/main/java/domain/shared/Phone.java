package domain.shared;

public class Phone extends BaseValidatableVO{

    public Phone(String value) {
        super(value);
    }

    @Override
    public String getValidationPattern(){
        // +79999999999
        return "^\\+?[1-9]\\d{1,14}$";
    }
}

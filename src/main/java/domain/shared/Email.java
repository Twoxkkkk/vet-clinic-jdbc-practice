package domain.shared;

public class Email extends BaseValidatableVO{

    public Email(String value) {
        super(value);
    }

    @Override
    public String getValidationPattern(){
        // example123@test.com
        return "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    }
}

package domain.shared;

public class Address extends BaseValidatableVO {

    public Address(String value) {
        super(value);
    }

    @Override
    public String getValidationPattern() {
        //За неимением удобного заполнения адреса на фронте - принимаю тупо все значения
        return  ".+";
    }
}

package domain.model.value_objects.objects;

import domain.model.value_objects.utils.ValidationException;

import java.util.regex.Pattern;

public abstract class BaseValidatableVO implements ValidatableVO{

    private final String value;

    public BaseValidatableVO(String value) throws ValidationException {
        this.validate(value);

        this.value = value;
    }

    protected void validate(String value) throws ValidationException{

        if (value == null || value.isBlank()) {
            throw new ValidationException("Value cannot be null or blank");
        }

        Pattern pattern = Pattern.compile(this.getValidationPattern());

        if (!pattern.matcher(value.trim()).matches()) {
            throw new ValidationException(
                String.format("Invalid value format: '%s'", value)
            );
        }
    }

    @Override
    public abstract String getValidationPattern();

    @Override
    public String getValue(){
        return this.value;
    }
}

package domain.shared;

import domain.shared.exceptions.NullOrBlankException;
import domain.shared.exceptions.ValidationException;

import java.util.regex.Pattern;

public abstract class BaseValidatableVO {

    private final String value;

    public BaseValidatableVO(String value) {
        this.validate(value);

        this.value = value;
    }

    private void validate(String value) {

        if (value == null || value.isBlank()) {
            throw new NullOrBlankException("Value cannot be null or blank");
        }

        Pattern pattern = Pattern.compile(this.getValidationPattern());

        if (!pattern.matcher(value.trim()).matches()) {
            throw new ValidationException(
                String.format("Invalid value format: '%s'", value)
            );
        }
    }

    public String getValue(){
        return this.value;
    }

    public abstract String getValidationPattern();
}

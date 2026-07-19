package domain.model.value_objects.objects;

import domain.model.value_objects.utils.ValidationException;

public class Address extends BaseValidatableVO{

    public Address(String value) throws ValidationException {
        super(value);
    }

    @Override
    public String getValidationPattern() {
        //"123456, г. Москва, ул. Образцова, д. 9к9"
        return  "^\\d{6},\\s*(?:г\\.\\s*[А-Яа-яЁё][А-Яа-яЁё\\-]+(?:\\s+[А-Яа-яЁё][А-Яа-яЁё\\-]+)*|" +
            "[А-Яа-яЁё]+(?:ская|ская|ая|ий|ое)\\s*обл\\.,\\s*г\\.\\s*[А-Яа-яЁё][А-Яа-яЁё\\-]+)," +
            "\\s*(?:ул\\.|пр\\-т\\.|просп\\.|пер\\.|бул\\.|наб\\.|ш\\.|пл\\.)\\s*" +
            "[А-Яа-яЁё][А-Яа-яЁё\\-]+(?:\\s+[А-Яа-яЁё][А-Яа-яЁё\\-]+)*,\\s*д\\.\\s*\\d+" +
            "(?:[кКкК]?\\s*\\d+)?(?:,\\s*(?:кв\\.|оф\\.|комн\\.)\\s*\\d+)?$";
    }
}

package domain.shared;

import domain.shared.exceptions.NullOrBlankException;
import domain.shared.exceptions.ValidationException;

public record BreedId(Integer value) {
    public BreedId{
        if(value == null){
            throw new NullOrBlankException("BreedId cannot be null!");
        }
        if(value < 0){
            throw new ValidationException("BreedId must be a positive number!");
        }
    }
}

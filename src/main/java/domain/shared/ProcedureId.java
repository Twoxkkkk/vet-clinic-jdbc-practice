package domain.shared;

import domain.shared.exceptions.NullOrBlankException;
import domain.shared.exceptions.ValidationException;

public record ProcedureId(Integer value) {
    public ProcedureId{
        if(value == null){
            throw new NullOrBlankException("ProcedureId cannot be null!");
        }
        if(value < 0){
            throw new ValidationException("ProcedureId must be a positive number!");
        }
    }
}

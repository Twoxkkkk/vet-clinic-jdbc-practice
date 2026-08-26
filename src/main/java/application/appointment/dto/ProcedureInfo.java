package application.appointment.dto;

import domain.shared.ProcedureId;
import domain.vet.VetSpecialization;

public record ProcedureInfo (
    ProcedureId id,
    String name,
    int durationInMinutes,
    double price,
    VetSpecialization vetSpecialization
){}

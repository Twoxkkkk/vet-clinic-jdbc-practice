package domain.model.entities;

import domain.model.entities.base.BaseEntity;

import java.time.LocalDateTime;

//TODO

public class Appointment extends BaseEntity {

    private LocalDateTime dateTimeOfAppointment;

    private Long vet_id;
    private Long owner_id;

    public Appointment(Long id) {
        super(id);
    }
}

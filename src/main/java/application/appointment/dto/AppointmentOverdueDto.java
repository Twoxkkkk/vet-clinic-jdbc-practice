package application.appointment.dto;

import domain.appointment.Appointment;
import domain.shared.Email;

public record AppointmentOverdueDto(
    Appointment appointment,
    Email ownerEmail,
    String ownerInitials
) {}

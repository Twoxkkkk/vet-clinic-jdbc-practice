package application.appointment.dto;

import domain.appointment.Appointment;
import domain.shared.ContactInfo;

public record AppointmentPlannedWithinIntervalDto(
    Appointment appointment,
    ContactInfo ownersContactInfo,
    String ownerInitials,
    long timeLeftBeforeAppointment
) {}

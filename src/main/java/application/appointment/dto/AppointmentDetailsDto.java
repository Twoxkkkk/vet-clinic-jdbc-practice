package application.appointment.dto;

import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.medical_record.MedicalRecord;
import domain.shared.Id;
import domain.vet.VetSpecialization;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentDetailsDto(
    Id<Appointment> appointmentId,

    LocalDateTime appointmentDateTime,
    AppointmentStatus appointmentStatus,

    String vetInitials,
    VetSpecialization vetSpecialization,

    String ownerInitials,
    String ownerContactNumber,

    String petNickname,
    String petType,

    String procedureType,

    String diagnosis,
    String treatment
) {}

package presentation.cli.shared;

import application.appointment.AppointmentService;
import application.medical_record.MedicalRecordService;
import application.pet_owner.PetOwnerService;
import application.vet.VetService;

public interface ServicesFactory {
    AppointmentService appointments();
    VetService vets();
    PetOwnerService petOwners();
    MedicalRecordService medicalRecords();
}

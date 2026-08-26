import application.appointment.AppointmentService;
import application.medical_record.MedicalRecordService;
import application.pet_owner.PetOwnerService;
import application.vet.VetService;
import infrastructure.database.AppointmentRepositoryImpl;
import infrastructure.database.MedicalRecordRepositoryImpl;
import infrastructure.database.PetOwnerRepositoryImpl;
import infrastructure.database.VetRepositoryImpl;
import presentation.cli.shared.ServicesFactory;

public class ServicesFacade implements ServicesFactory {

    private final AppointmentService appointments;
    private final VetService vets;
    private final MedicalRecordService medicalRecords;
    private final PetOwnerService petOwners;

    public ServicesFacade(){
        this.appointments = new AppointmentService(new AppointmentRepositoryImpl());
        this.vets = new VetService(new VetRepositoryImpl());
        this.medicalRecords = new MedicalRecordService(new MedicalRecordRepositoryImpl());
        this.petOwners = new PetOwnerService(new PetOwnerRepositoryImpl());
    }

    public AppointmentService appointments() {
        return appointments;
    }

    public VetService vets() {
        return vets;
    }

    public MedicalRecordService medicalRecords() {
        return medicalRecords;
    }

    public PetOwnerService petOwners() {
        return petOwners;
    }
}

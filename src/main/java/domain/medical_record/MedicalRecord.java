package domain.medical_record;

import domain.pet_owner.Pet;
import domain.shared.Id;
import domain.vet.Vet;

import java.time.LocalDate;

public class MedicalRecord {

    private Id<MedicalRecord> id;

    private Id<Pet> petId;
    private Id<Vet> vetId;

    private String diagnosis;
    private String treatment;

    private LocalDate date;

    public MedicalRecord(Id<MedicalRecord> id, Id<Pet> petId, Id<Vet> vetId,
                 String diagnosis, String treatment, LocalDate date) {
        this.setId(id);
        this.setPetId(petId);
        this.setVetId(vetId);
        this.setDiagnosis(diagnosis);
        this.setTreatment(treatment);
        this.setDate(date);
    }


    public Id<MedicalRecord> getId() {
        return id;
    }

    public void setId(Id<MedicalRecord> id) {
        this.id = id;
    }

    public Id<Pet> getPetId() {
        return petId;
    }

    public void setPetId(Id<Pet> petId) {
        this.petId = petId;
    }

    public Id<Vet> getVetId() {
        return vetId;
    }

    public void setVetId(Id<Vet> vetId) {
        this.vetId = vetId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

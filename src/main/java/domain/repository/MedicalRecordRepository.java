package domain.repository;

import domain.medical_record.MedicalRecord;
import domain.shared.Id;

import java.util.Optional;

public interface MedicalRecordRepository {

    void save(MedicalRecord medicalRecord);
    void delete(Id<MedicalRecord> medicalRecordId);

    Optional<MedicalRecord> findById(Id<MedicalRecord> medicalRecordId);
}

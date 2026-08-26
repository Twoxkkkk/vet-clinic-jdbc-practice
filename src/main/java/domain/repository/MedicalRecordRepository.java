package domain.repository;

import application.medical_record.dto.TopDiagnosisDto;
import domain.medical_record.MedicalRecord;
import domain.pet_owner.Pet;
import domain.shared.Id;
import domain.vet.Vet;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordRepository extends Repository<MedicalRecord>{

    List<TopDiagnosisDto> getTopDiagnosisReports(LocalDate start, LocalDate end);


}

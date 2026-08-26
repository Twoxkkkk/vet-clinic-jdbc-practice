package application.medical_record;

import application.medical_record.dto.TopDiagnosisDto;
import application.shared.BaseService;
import domain.medical_record.MedicalRecord;
import domain.repository.MedicalRecordRepository;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class MedicalRecordService extends BaseService<MedicalRecord, MedicalRecordRepository> {

    private static final int EPIDEMIC_UNIQUE_PETS_DIAGNOSIS_COUNT = 6;

    public MedicalRecordService(MedicalRecordRepository repository) {
        super(repository);
    }

    public String getTopDiagnosisReportsForTheCurrentMonth(){
        LocalDate today = LocalDate.now();

        LocalDate start = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = today.with(TemporalAdjusters.firstDayOfNextMonth());

        List<TopDiagnosisDto> topDiagnosisDtos = repository.getTopDiagnosisReports(start, end);

        if(topDiagnosisDtos.isEmpty()) return null;

        StringBuilder result = new StringBuilder();

        for(TopDiagnosisDto dto: topDiagnosisDtos){

            result.append(
                String.format(
                    "Диагноз: %s%nБыл поставлен за месяц: %s раз %s разным питомцам.%n%n",
                    dto.diagnosis(), dto.encounters(), dto.uniquePets()
                )
            );

            if(dto.uniquePets() > EPIDEMIC_UNIQUE_PETS_DIAGNOSIS_COUNT){
                result.append("Количество поставленным разным питомцам одного диагноза похоже на эпидемию!\n");
            }
        }
        
        return result.toString();
    }
}

package application.medical_record.dto;

public record TopDiagnosisDto(
    String diagnosis,
    int encounters,
    int uniquePets
) {}

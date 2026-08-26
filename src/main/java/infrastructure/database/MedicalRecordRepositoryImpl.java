package infrastructure.database;

import application.medical_record.dto.TopDiagnosisDto;
import domain.medical_record.MedicalRecord;
import domain.pet_owner.Pet;
import domain.repository.MedicalRecordRepository;
import domain.shared.Id;
import domain.vet.Vet;
import infrastructure.config.DbConfig;
import infrastructure.database.exceptions.QueryException;
import infrastructure.database.exceptions.TransactionException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MedicalRecordRepositoryImpl implements MedicalRecordRepository{

    @Override
    public void save(MedicalRecord medicalRecord) {
        String sqlSaveMedicalRecord = """
            INSERT INTO\s
            medical_records(id, vet_id, pet_id, diagnosis, treatment)
            VALUES(?,?,?,?,?,?)
            ON CONFLICT(id) DO UPDATE SET
                id = EXCLUDED.id,
                vet_id = EXCLUDED.vet_id,
                pet_id = EXCLUDED.pet_id,
                diagnosis = EXCLUDED.diagnosis,
                treatment = EXCLUDED.treatment,
       \s""";

        try(Connection con = DbConfig.getInstance().getConnection()){
            try {
                try (PreparedStatement prstmnt = con.prepareStatement(sqlSaveMedicalRecord)){
                    prstmnt.setObject(1, medicalRecord.getId().value());
                    prstmnt.setObject(2, medicalRecord.getVetId().value());
                    prstmnt.setObject(3, medicalRecord.getPetId().value());
                    prstmnt.setString(4, medicalRecord.getDiagnosis());
                    prstmnt.setString(5, medicalRecord.getTreatment());
                    prstmnt.executeUpdate();
                }
                con.commit();

            } catch (SQLException e){
                con.rollback();
                throw new TransactionException("There was an error while saving appointment!", e);
            }
        } catch (SQLException e){
            throw new TransactionException("Couldn't get database connection!", e);
        }
    }

    @Override
    public void delete(Id<MedicalRecord> medicalRecordId) {
        String sqlDeleteMedicalRecord = """
            DELETE FROM medical_records
            WHERE id = ?
        """;

        try(Connection con = DbConfig.getInstance().getConnection()){
            try {
                try (PreparedStatement prstmnt = con.prepareStatement(sqlDeleteMedicalRecord)){
                    prstmnt.setObject(1, medicalRecordId.value());
                    prstmnt.executeUpdate();
                }
                con.commit();

            } catch (SQLException e){
                con.rollback();
                throw new TransactionException("There was an error while saving appointment!", e);
            }
        } catch (SQLException e){
            throw new TransactionException("Couldn't get database connection!", e);
        }
    }

    public MedicalRecord mapRsToMedicalRecord(ResultSet rs){

        try{
            Id<MedicalRecord> id = new Id<>((UUID) rs.getObject("id"));

            Id<Vet> vetId = new Id<>((UUID) rs.getObject("vet_id"));
            Id<Pet> petId = new Id<>((UUID) rs.getObject("pet_id"));

            String diagnosis = rs.getString("diagnosis");
            String treatment = rs.getString("treatment");


            LocalDate record_date = rs.getDate("record_date").toLocalDate();

            return new MedicalRecord(id, petId, vetId, diagnosis, treatment, record_date);

        } catch (SQLException e){
            throw new QueryException("Couldn't query some of the columns of MedicalRecord!", e);
        }

    }

    @Override
    public Optional<MedicalRecord> findById(Id<MedicalRecord> medicalRecordId) {
        String sqlFindMedicalRecord = """
            SELECT * FROM medical_records
            WHERE id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindMedicalRecord);

            prstmnt.setObject(1, medicalRecordId.value());

            try (ResultSet rs = prstmnt.executeQuery()){
                MedicalRecord medicalRecord = null;

                while (rs.next()){
                    if (medicalRecord == null){
                        medicalRecord = mapRsToMedicalRecord(rs);
                    }
                }
                return Optional.ofNullable(medicalRecord);
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }


    @Override
    public List<TopDiagnosisDto> getTopDiagnosisReports(LocalDate start, LocalDate end) {
        String sqlFindTopDiagnosis = """
            SELECT diagnosis, COUNT(*) AS encounters, COUNT(DISTINCT pet_id) AS unique_pets
            FROM medical_records
            WHERE record_date BETWEEN ? AND ?
            GROUP BY diagnosis
            ORDER BY encounters DESC
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindTopDiagnosis);

            prstmnt.setDate(1, Date.valueOf(start));
            prstmnt.setDate(2, Date.valueOf(end));

            try (ResultSet rs = prstmnt.executeQuery()){

                List<TopDiagnosisDto> reports = new ArrayList<>();

                while (rs.next()){
                    TopDiagnosisDto report = new TopDiagnosisDto(
                        rs.getString("diagnosis"),
                        rs.getInt("encounters"),
                        rs.getInt("unique_pets")
                    );

                    reports.add(report);
                }
                return reports;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }
}

package infrastructure.database;

import application.vet.dto.VetPerformanceDto;
import domain.repository.VetRepository;
import domain.shared.*;
import domain.vet.Vet;
import domain.vet.VetSpecialization;
import infrastructure.config.DbConfig;
import infrastructure.database.exceptions.QueryException;
import infrastructure.database.exceptions.TransactionException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VetRepositoryImpl implements VetRepository {
    @Override
    public void save(Vet vet) {
        String sqlSaveVet = """
            INSERT INTO\s
            vets(id, first_name, last_name, contact_number, email, specialization)
            VALUES(?,?,?,?,?,?)
            ON CONFLICT(id) DO UPDATE SET
                id = EXCLUDED.id,
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                contact_number = EXCLUDED.contact_number,
                email = EXCLUDED.email,
                specialization = EXCLUDED.specialization
       \s""";

        try(Connection con = DbConfig.getInstance().getConnection()){
            try {
                try (PreparedStatement prstmnt = con.prepareStatement(sqlSaveVet)){
                    prstmnt.setObject(1, vet.getId().value());
                    prstmnt.setString(2, vet.getFirstName());
                    prstmnt.setString(3, vet.getLastName());
                    prstmnt.setString(4, vet.getContactInfo().phone().getValue());
                    prstmnt.setString(5, vet.getContactInfo().email().getValue());
                    prstmnt.setString(6, vet.getSpecialization().name());
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
    public void delete(Id<Vet> vetId) {
        String sqlDeleteVet = """
            DELETE FROM vets
            WHERE id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()) {
            try (PreparedStatement prstmnt = con.prepareStatement(sqlDeleteVet)){
                prstmnt.setObject(1, vetId.value());

                prstmnt.executeUpdate();
            }
            con.commit();

        } catch (SQLException e){
            throw new TransactionException("Couldn't get database connection!", e);
        }
    }

    public Vet mapRsToVet(ResultSet rs){
        try{
            Id<Vet> id = new Id<>((UUID) rs.getObject("id"));

            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");

            ContactInfo contactInfo = new ContactInfo(
                    new Phone(rs.getString("contact_number")),
                    new Email(rs.getString("email"))
            );

            VetSpecialization vetSpecialization = VetSpecialization.valueOf(rs.getString("specialization"));

            return new Vet(id, firstName, lastName, vetSpecialization, contactInfo);

        } catch (SQLException e) {
            throw new QueryException("Couldn't query some of the columns of Vet!", e);
        }
    }

    @Override
    public Optional<Vet> findByPhoneNumber(Phone phone) {
        String sqlFindPetOwnerAndHisPets = """
            SELECT * FROM vets
            WHERE contact_number = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindPetOwnerAndHisPets);

            prstmnt.setObject(1, phone.getValue());

            try (ResultSet rs = prstmnt.executeQuery()){
                Vet vet = null;

                while (rs.next()){
                    if (vet == null){
                        vet = mapRsToVet(rs);
                    }
                }
                return Optional.ofNullable(vet);
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public Optional<Vet> findByEmail(Email email) {
        String sqlFindPetOwnerAndHisPets = """
            SELECT * FROM vets
            WHERE email = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindPetOwnerAndHisPets);

            prstmnt.setObject(1, email.getValue());

            try (ResultSet rs = prstmnt.executeQuery()){
                Vet vet = null;

                while (rs.next()){
                    if (vet == null){
                        vet = mapRsToVet(rs);
                    }
                }
                return Optional.ofNullable(vet);
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public List<Vet> findAllAvailableForTimeBySpecialization(VetSpecialization specialization, LocalDateTime dateTime) {
        String sqlFindAllBySpecAndTimeAndProcedure = """
        SELECT v.* FROM vets v
        WHERE specialization = ?
        AND NOT EXISTS (
            SELECT 1 FROM appointments a

            JOIN procedures p ON a.procedure_id = p.id

            WHERE a.vet_id = v.id

            AND a.status = 'PLANNED'
            AND ? BETWEEN a.date_time AND (a.date_time + p.duration_minutes * INTERVAL '1 minute')
        )
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllBySpecAndTimeAndProcedure);

            prstmnt.setString(1, specialization.toString());
            prstmnt.setTimestamp(2, Timestamp.valueOf(dateTime));

            try (ResultSet rs = prstmnt.executeQuery()){

                List<Vet> vets = new ArrayList<>();

                while (rs.next()){
                    Vet vet = mapRsToVet(rs);

                    vets.add(vet);
                }
                return vets;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public List<VetPerformanceDto> getVetsPerformanceReport(LocalDate start, LocalDate end) {
        String sqlFindAppointmentById = """
            SELECT v.id AS vet_id,
            CONCAT(v.last_name, ' ', SUBSTRING(v.first_name FROM 1 FOR 1), '.') AS vet_initials,
            COALESCE(SUM(p.price), 0.0) AS total_revenue,
            COUNT(a.id) AS appointments_count
            FROM vets v

            LEFT JOIN appointments a ON a.vet_id = v.id AND a.status = 'FINISHED'
            LEFT JOIN procedures p ON a.procedure_id = p.id

            WHERE a.date_time BETWEEN ? AND ?
            GROUP BY v.id, v.first_name, v.last_name
            ORDER BY total_revenue DESC
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAppointmentById);

            prstmnt.setDate(1, Date.valueOf(start));
            prstmnt.setDate(2, Date.valueOf(end));

            List<VetPerformanceDto> vetPerformanceDtos = new ArrayList<>();

            try (ResultSet rs = prstmnt.executeQuery()){

                while (rs.next()){
                    VetPerformanceDto dto = new VetPerformanceDto(
                        new Id<>( (UUID) rs.getObject("vet_id")),
                        rs.getString("vet_initials"),
                        rs.getInt("appointments_count"),
                        rs.getDouble("total_revenue")
                    );

                    vetPerformanceDtos.add(dto);
                }
                return vetPerformanceDtos;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public Optional<Vet> findById(Id<Vet> vetId) {
        String sqlFindAppointmentById = """
            SELECT * FROM appointments
            WHERE id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAppointmentById);

            prstmnt.setObject(1, vetId.value());

            try (ResultSet rs = prstmnt.executeQuery()){
                Vet vet = null;

                while (rs.next()){
                    if (vet == null){
                        vet = mapRsToVet(rs);
                    }
                }
                return Optional.ofNullable(vet);
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }
}

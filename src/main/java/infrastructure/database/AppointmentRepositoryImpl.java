package infrastructure.database;

import application.appointment.dto.AppointmentDetailsDto;
import application.appointment.dto.AppointmentPlannedWithinIntervalDto;
import application.appointment.dto.ProcedureInfo;
import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.repository.AppointmentRepository;
import domain.shared.*;
import domain.vet.Vet;
import domain.vet.VetSpecialization;
import infrastructure.config.DbConfig;
import infrastructure.database.exceptions.QueryException;
import infrastructure.database.exceptions.TransactionException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    @Override
    public void save(Appointment appointment) {
        String sqlSaveAppointment = """
            INSERT INTO\s
            appointments(id, vet_id, pet_id, procedure_id, date_time, status)
            VALUES(?,?,?,?,?,?)
            ON CONFLICT(id) DO UPDATE SET
                id = EXCLUDED.id,
                vet_id = EXCLUDED.vet_id,
                procedure_id = EXCLUDED.procedure_id,
                pet_id = EXCLUDED.pet_id,
                date_time = EXCLUDED.date_time,
                status = EXCLUDED.status
       \s""";

        try(Connection con = DbConfig.getInstance().getConnection()){
            try {
                try (PreparedStatement prstmnt = con.prepareStatement(sqlSaveAppointment)){
                    prstmnt.setObject(1, appointment.getId().value());
                    prstmnt.setObject(2, appointment.getVetId().value());
                    prstmnt.setObject(3, appointment.getPetId().value());
                    prstmnt.setInt(4, appointment.getProcedureId().value());
                    prstmnt.setTimestamp(5, Timestamp.valueOf(appointment.getDateTimeOfAppointment()));
                    prstmnt.setString(6, appointment.getStatus().toString());
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
    public void delete(Id<Appointment> appointmentId) {
        String sqlDeleteAppointment = """
            DELETE FROM appointments
            WHERE id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){

            try (PreparedStatement prstmnt = con.prepareStatement(sqlDeleteAppointment)){
                prstmnt.setObject(1, appointmentId.value());
                prstmnt.executeUpdate();
            }
            con.commit();

        } catch (SQLException e){
            throw new TransactionException("Couldn't get database connection!", e);
        }
    }

    private Appointment mapRsToAppointment(ResultSet rs){
        try{
            Id<Appointment> id = new Id<>((UUID) rs.getObject("id"));

            Id<Vet> vetId = new Id<>((UUID) rs.getObject("vet_id"));
            Id<Pet> petId = new Id<>((UUID) rs.getObject("pet_id"));

            ProcedureId procedureId = new ProcedureId(rs.getInt("procedure_id"));
            LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();

            AppointmentStatus status = AppointmentStatus.valueOf(rs.getString("status"));

            return new Appointment(id, vetId, petId, dateTime, status, procedureId);

        } catch (SQLException e){
            throw new QueryException("Couldn't query some of the columns of Appointment!", e);
        }
    }

    @Override
    public List<Appointment> findAllByVetIdAndDateTime(Id<Vet> vetId, LocalDateTime dateTime) {

        String sqlFindAllByVetId = """
            SELECT * FROM appointments
            WHERE vet_id = ? AND date_time = ?
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllByVetId);

            prstmnt.setObject(1, vetId.value());
            prstmnt.setTimestamp(2, Timestamp.valueOf(dateTime));

            try (ResultSet rs = prstmnt.executeQuery()){

                Appointment appointment;

                while (rs.next()){
                    appointment = mapRsToAppointment(rs);

                    appointments.add(appointment);
                }

                return appointments;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public Optional<Appointment> findById(Id<Appointment> appointmentId) {

        String sqlFindAppointmentById = """
            SELECT * FROM appointments
            WHERE id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAppointmentById);

            prstmnt.setObject(1, appointmentId.value());

            try (ResultSet rs = prstmnt.executeQuery()){
                Appointment appointment = null;

                while (rs.next()){
                    if (appointment == null){
                        appointment = mapRsToAppointment(rs);
                    }
                }
                return Optional.ofNullable(appointment);
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public List<ProcedureInfo> getAllProcedures() {
        String sqlGetAllProcedures = """
            SELECT * FROM procedures
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){

            Statement statement = con.createStatement();


            try (ResultSet rs = statement.executeQuery(sqlGetAllProcedures)){

                List<ProcedureInfo> procedures = new ArrayList<>();

                while (rs.next()){
                    ProcedureInfo info = new ProcedureInfo(
                        new ProcedureId(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getInt("duration_minutes"),
                        rs.getDouble("price"),
                        VetSpecialization.valueOf(rs.getString("vet_specialization"))
                    );

                    procedures.add(info);
                }
                return procedures;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public List<Appointment> findAllForTodayByVetId(Id<Vet> vetId) {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        String sqlFindAllForTodayByVetId = """
            SELECT * FROM appointments
            WHERE vet_id = ? AND date_time BETWEEN ? AND ? AND status = 'PLANNED'
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllForTodayByVetId);

            prstmnt.setObject(1, vetId.value());
            prstmnt.setTimestamp(2, Timestamp.valueOf(startOfDay));
            prstmnt.setTimestamp(3, Timestamp.valueOf(endOfDay));

            try (ResultSet rs = prstmnt.executeQuery()){
                Appointment appointment;

                while (rs.next()){
                    appointment = mapRsToAppointment(rs);

                    appointments.add(appointment);
                }
                return appointments;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public List<Appointment> findAllForTodayByPetOwnerId(Id<PetOwner> petOwnerId) {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        String sqlFindAllForTodayByVetId = """
            SELECT app.* FROM appointments app
            JOIN pets p ON app.pet_id = p.id
            JOIN pet_owners o ON o.id = p.owner_id
            WHERE o.id = ? AND date_time BETWEEN ? AND ? AND app.status = 'PLANNED'
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllForTodayByVetId);

            prstmnt.setObject(1, petOwnerId.value());
            prstmnt.setTimestamp(2, Timestamp.valueOf(startOfDay));
            prstmnt.setTimestamp(3, Timestamp.valueOf(endOfDay));

            try (ResultSet rs = prstmnt.executeQuery()){
                Appointment appointment;

                while (rs.next()){
                    appointment = mapRsToAppointment(rs);

                    appointments.add(appointment);
                }
                return appointments;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }


    @Override
    public List<AppointmentPlannedWithinIntervalDto> getAmountPlannedTodayWithInterval(int intervalInMinutes, int amount) {
        String sqlGetAllWithinInterval= """
        SELECT app.*,
        o.email AS owner_email,
        o.contact_number AS owner_phone,
        CONCAT(o.last_name, ' ', SUBSTRING(o.first_name FROM 1 FOR 1), '.') AS owner_initials,
        EXTRACT(EPOCH FROM(app.date_time - NOW())) / 60 AS minutes_left
        FROM appointments app
        
        JOIN pets p ON app.pet_id = p.id
        JOIN pet_owners o ON o.id = p.owner_id
        
        WHERE app.pet_id = p.id AND app.status = 'PLANNED'
        AND app.date_time >= NOW()
        AND app.date_time <= (NOW() + ? * INTERVAL '1 minute')
        
        ORDER BY minutes_left DESC
        LIMIT ?
        """;

        List<AppointmentPlannedWithinIntervalDto> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){

            PreparedStatement prstmnt = con.prepareStatement(sqlGetAllWithinInterval);

            prstmnt.setInt(1, intervalInMinutes);
            prstmnt.setInt(2, amount);

            try (ResultSet rs = prstmnt.executeQuery()){

                while (rs.next()){
                    Appointment appointment = mapRsToAppointment(rs);

                    ContactInfo ownerContacts = new ContactInfo(
                        new Phone(rs.getString("owner_phone")),
                        new Email(rs.getString("owner_email"))
                    );

                    AppointmentPlannedWithinIntervalDto appointmentOverdueDto = new AppointmentPlannedWithinIntervalDto(
                        appointment,
                        ownerContacts,
                        rs.getString("owner_initials"),
                        rs.getInt("minutes_left")
                    );

                    appointments.add(appointmentOverdueDto);
                }
                return appointments;
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }

    @Override
    public Optional<Appointment> findPlannedByPetOwnerIdAndDateTime(Id<PetOwner> petOwnerId, LocalDateTime dateTime) {
        String sqlFindAllForTodayByVetId = """
            SELECT app.* FROM appointments app
            JOIN pets p ON app.pet_id = p.id
            JOIN pet_owners o ON o.id = p.owner_id
            WHERE o.id = ? AND date_time = ? AND app.status = 'PLANNED'
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllForTodayByVetId);

            prstmnt.setObject(1, petOwnerId.value());
            prstmnt.setTimestamp(2, Timestamp.valueOf(dateTime));

            try (ResultSet rs = prstmnt.executeQuery()){
                Appointment appointment;

                if (rs.next()){
                    appointment = mapRsToAppointment(rs);

                    return Optional.of(appointment);
                }
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<AppointmentDetailsDto> getDetailsById(Id<Appointment> appointmentId) {
        String sqlGetDetails = """
            SELECT a.id AS appointment_id, a.date_time AS date_time, a.status AS status,
            CONCAT(v.last_name, ' ', SUBSTRING(v.first_name FROM 1 FOR 1), '.') AS vet_initials,
            v.specialization as vet_specialization,
            CONCAT(o.last_name, ' ', SUBSTRING(o.first_name FROM 1 FOR 1), '.') AS owner_initials,

            o.contact_number as owner_contact_number,
            p.nickname as pet_nickname,
            b.pet_type as pet_type,
            pr.name as procedure_type,

            m.diagnosis as diagnosis,
            m.treatment as treatment

            FROM appointments a

            JOIN vets v ON a.vet_id = v.id
        
            JOIN pets p ON p.id = a.pet_id
            JOIN pet_owners o ON p.owner_id = o.id

            JOIN breeds b ON b.id = p.breed_id
            JOIN procedures pr ON pr.id = a.procedure_id

            LEFT JOIN medical_records m
                ON m.vet_id = a.vet_id AND m.pet_id = a.pet_id
                AND m.record_date = a.date_time::date

            WHERE a.id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlGetDetails);

            prstmnt.setObject(1, appointmentId.value());

            try (ResultSet rs = prstmnt.executeQuery()){

                if(rs.next()){
                    AppointmentDetailsDto detailsDto = new AppointmentDetailsDto(
                        new Id<>((UUID) rs.getObject("appointment_id")),
                        rs.getTimestamp("date_time").toLocalDateTime(),
                        AppointmentStatus.valueOf(rs.getString("status")),
                        rs.getString("vet_initials"),
                        VetSpecialization.valueOf(rs.getString("vet_specialization")),
                        rs.getString("owner_initials"),
                        rs.getString("owner_contact_number"),
                        rs.getString("pet_nickname"),
                        rs.getString("pet_type"),
                        rs.getString("procedure_type"),
                        rs.getString("diagnosis"),
                        rs.getString("treatment")

                    );
                    return Optional.of(detailsDto);
                }
            }

        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }

        return Optional.empty();
    }
}

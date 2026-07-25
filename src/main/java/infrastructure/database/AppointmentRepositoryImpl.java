package infrastructure.database;

import domain.appointment.Appointment;
import domain.appointment.AppointmentStatus;
import domain.pet_owner.Pet;
import domain.repository.AppointmentRepository;
import domain.shared.*;
import domain.vet.Vet;
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
            LocalDateTime dateTime = LocalDateTime.from(rs.getDate("date_time").toInstant());

            AppointmentStatus status = AppointmentStatus.valueOf(rs.getString("status"));

            return new Appointment(id, vetId, petId, dateTime, status, procedureId);

        } catch (SQLException e){
            throw new QueryException("Couldn't query some of the columns of Appointment!", e);
        }
    }

    @Override
    public List<Appointment> findAllByPetId(Id<Pet> petId) {
        String sqlFindAllByPetId = """
            SELECT * FROM appointments
            WHERE pet_id = ?
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllByPetId);

            prstmnt.setObject(1, petId.value());

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
    public List<Appointment> findAllByVetIdWithStatus(Id<Vet> vetId, AppointmentStatus status) {

        String sqlFindAllByVetId = """
            SELECT * FROM appointments
            WHERE vet_id = ? AND status = ?
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllByVetId);

            prstmnt.setObject(1, vetId.value());
            prstmnt.setString(2, status.toString());

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
    public long countAllActiveByVetId(Id<Vet> vetId) {
        String sqlFindAllByVetId = """
            SELECT COUNT(*) FROM appointments
            WHERE vet_id = ? AND status NOT IN ('CANCELED', 'FINISHED')
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllByVetId);

            prstmnt.setObject(1, vetId.value());

            try (ResultSet rs = prstmnt.executeQuery()){
                return rs.getLong(1);
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
            WHERE vet_id = ? AND date_time BETWEEN ? AND ?
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllForTodayByVetId);

            prstmnt.setObject(1, vetId.value());

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
    public List<Appointment> findAllForTodayByPetId(Id<Pet> petId) {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        String sqlFindAllForTodayByVetId = """
            SELECT * FROM appointments
            WHERE pet_id = ? AND date_time BETWEEN ? AND ?
        """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindAllForTodayByVetId);

            prstmnt.setObject(1, petId.value());

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
}

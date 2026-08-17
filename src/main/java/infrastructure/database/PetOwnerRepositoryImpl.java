package infrastructure.database;

import domain.pet_owner.Pet;
import domain.pet_owner.PetOwner;
import domain.pet_owner.PetSex;
import domain.repository.PetOwnerRepository;
import domain.shared.*;
import infrastructure.config.DbConfig;
import infrastructure.database.exceptions.QueryException;
import infrastructure.database.exceptions.TransactionException;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class PetOwnerRepositoryImpl implements PetOwnerRepository {

    @Override
    public void save(PetOwner petOwner) {
        String sqlSavePetOwner = """
            INSERT INTO\s
            pet_owners(id, first_name, last_name, contact_number, email, address, registration_date)
            VALUES(?,?,?,?,?,?,?)
            ON CONFLICT(id) DO UPDATE SET
                first_name = EXCLUDED.first_name,
                last_name = EXCLUDED.last_name,
                contact_number = EXCLUDED.contact_number,
                email = EXCLUDED.email,
                address = EXCLUDED.address,
                registration_date = EXCLUDED.registration_date
       \s""";

        String sqlSavePets = """
            INSERT INTO\s
            pets(id, owner_id, breed_id, nickname, date_of_birth, sex, weight)
            VALUES(?,?,?,?,?,?,?)
            ON CONFLICT(id) DO UPDATE SET
                owner_id = EXCLUDED.owner_id,
                nickname = EXCLUDED.nickname,
                date_of_birth = EXCLUDED.date_of_birth,
                sex = EXCLUDED.sex,
                weight = EXCLUDED.weight,
                breed_id = EXCLUDED.breed_id
        """;

        String contactNumber = petOwner.getContactInfo().phone().getValue();
        String email = petOwner.getContactInfo().email().getValue();

        try(Connection con = DbConfig.getInstance().getConnection()){
            try {
                try (PreparedStatement prstmnt = con.prepareStatement(sqlSavePetOwner)){
                    prstmnt.setObject(1, petOwner.getId().value());
                    prstmnt.setString(2, petOwner.getFirstName());
                    prstmnt.setString(3, petOwner.getLastName());
                    prstmnt.setString(4, contactNumber);
                    prstmnt.setString(5, email);
                    prstmnt.setString(6, petOwner.getAddress().getValue());
                    prstmnt.setDate(7, Date.valueOf(petOwner.getRegistrationDate()));
                    prstmnt.executeUpdate();
                }

                try (PreparedStatement prstmnt = con.prepareStatement(sqlSavePets)){
                    for (Pet pet: petOwner.getPets()){
                        prstmnt.setObject(1, pet.getId());
                        prstmnt.setObject(2, petOwner.getId());
                        prstmnt.setInt(3, pet.getBreedId().value());
                        prstmnt.setString(4, pet.getNickname());
                        prstmnt.setDate(5, Date.valueOf(pet.getDateOfBirth()));
                        prstmnt.setString(6, pet.getSex().toString());
                        prstmnt.setDouble(7, pet.getWeight());
                        prstmnt.addBatch();
                    }
                    prstmnt.executeBatch();
                }

                con.commit();
            } catch (SQLException e){
                con.rollback();
                throw new TransactionException("There was an error while saving pet owner!", e);
            }
        } catch (SQLException e){
            throw new TransactionException("Couldn't get database connection!", e);
        }
    }

    @Override
    public void delete(Id<PetOwner> petOwnerId) {
        String sqlDeletePetOwner = """
            DELETE FROM pet_owners
            WHERE id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()) {
            try (PreparedStatement prstmnt = con.prepareStatement(sqlDeletePetOwner)){
                prstmnt.setObject(1, petOwnerId.value());

                prstmnt.executeUpdate();
            }
            con.commit();

        } catch (SQLException e){
            throw new TransactionException("Couldn't get database connection!", e);
        }
    }

    private PetOwner mapRsToPetOwner(ResultSet rs){
        try{
            Id<PetOwner> id = new Id<>((UUID) rs.getObject("id"));

            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");

            Address address = new Address(rs.getString("address"));

            ContactInfo contactInfo = new ContactInfo(
                new Phone(rs.getString("contact_number")),
                new Email(rs.getString("email"))
            );

            LocalDate registrationDate = rs.getDate("registration_date").toLocalDate();

            return new PetOwner(id, firstName, lastName, address, contactInfo, registrationDate);

        } catch (SQLException e){
            throw new QueryException("Couldn't query some of the columns of PetOwner!", e);
        }
    }

    @Override
    public Optional<PetOwner> findById(Id<PetOwner> petOwnerId) {
        String sqlFindPetOwnerAndHisPets = """
            SELECT o.*, p.id AS pet_id, p.breed_id,
            p.nickname, p.date_of_birth, p.sex, p.weight
            FROM pet_owners o
            LEFT JOIN pets p ON p.owner_id = o.id
            WHERE o.id = ?
        """;

        try (Connection con = DbConfig.getInstance().getConnection()){
            PreparedStatement prstmnt = con.prepareStatement(sqlFindPetOwnerAndHisPets);

            prstmnt.setObject(1, petOwnerId.value());

            try (ResultSet rs = prstmnt.executeQuery()){
                PetOwner petOwner = null;

                while (rs.next()){
                    if (petOwner == null){
                        petOwner = mapRsToPetOwner(rs);
                    }

                    if(rs.getObject("pet_id") != null){

                        Id<Pet> petId = new Id<>((UUID) rs.getObject("pet_id"));
                        BreedId breedId = new BreedId(rs.getInt("breed_id"));
                        Id<PetOwner> ownerId = petOwnerId;

                        String petNickname = rs.getString("nickname");
                        LocalDate petDateOfBirth =  rs.getDate("date_of_birth").toLocalDate();

                        PetSex sex = PetSex.valueOf(rs.getString("sex"));

                        double weight = rs.getDouble("weight");

                        petOwner.addPetFromDatabase(petId,petNickname, petDateOfBirth, sex, weight, breedId);
                    }
                }
                return Optional.ofNullable(petOwner);
            }
        } catch (SQLException e){
            throw new QueryException("Couldn't get database connection!", e);
        }
    }
}

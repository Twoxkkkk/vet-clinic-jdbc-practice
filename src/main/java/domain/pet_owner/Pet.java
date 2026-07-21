package domain.pet_owner;

import domain.shared.BreedId;
import domain.shared.Id;

import java.time.LocalDate;
import java.util.Date;

public class Pet{

    private Id<Pet> id;

    private Id<PetOwner> ownerId;

    private String nickname;
    private LocalDate dateOfBirth;

    private PetSex sex;
    private double weight;

    private BreedId breedId;


    Pet(Id<Pet> id, String nickname, LocalDate dateOfBirth, Id<PetOwner> ownerId,
        PetSex sex, double weight, BreedId breedId
    ) {
        this.setId(id);
        this.setNickname(nickname);
        this.setDateOfBirth(dateOfBirth);
        this.setOwnerId(ownerId);
        this.setWeight(weight);
        this.setBreedId(breedId);
        this.setSex(sex);
    }

    public Id<Pet> getId() {
        return id;
    }

    public void setId(Id<Pet> id) {
        this.id = id;
    }

    public void setSex(PetSex sex) {
        this.sex = sex;
    }

    public PetSex getSex() {
        return sex;
    }

    public BreedId getBreedId() {
        return breedId;
    }

    public void setBreedId(BreedId breedId) {
        this.breedId = breedId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.isBlank())
            return;

        this.nickname = nickname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Id<PetOwner> getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Id<PetOwner> ownerId) {
        this.ownerId = ownerId;
    }
}
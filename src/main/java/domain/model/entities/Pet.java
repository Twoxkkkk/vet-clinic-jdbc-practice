package domain.model.entities;

import domain.model.entities.base.BaseEntity;
import domain.model.entities.enums.PetType;

import java.util.Date;

public class Pet extends BaseEntity {

    private String nickname;
    private Date dateOfBirth;

    private boolean sex;
    private double weight;

    private PetType type;

    private int ownerId;

    public Pet(Long id, String nickname, Date dateOfBirth, int ownerId,
           boolean sex, double weight, PetType type
        ) {
        super(id);
        this.setNickname(nickname);
        this.setDateOfBirth(dateOfBirth);
        this.setOwnerId(ownerId);
        this.setWeight(weight);
        this.setType(type);

        this.sex = sex;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        if (type == null)
            return;

        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean getSex() {
        return sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if (nickname == null || nickname.isBlank())
            return;

        this.nickname = nickname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}

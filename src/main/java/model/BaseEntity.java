package model;

public abstract class BaseEntity {

    private int id;

    public BaseEntity(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0)
            return;

        this.id = id;
    }

    @Override
    public String toString() {
        return String.format(
            "%d%n", this.getId()
        );
    }

}

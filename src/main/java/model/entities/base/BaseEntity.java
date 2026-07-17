package model.entities.base;

public abstract class BaseEntity {

    private Long id;

    public BaseEntity(Long id) {
        setId(id);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        if (id == null)
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

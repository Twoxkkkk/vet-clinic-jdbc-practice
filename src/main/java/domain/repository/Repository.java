package domain.repository;

import domain.shared.Id;

import java.util.Optional;

public interface Repository<T> {

    void save(T entity);
    void delete(Id<T> entityId);

    Optional<T> findById(Id<T> entityId);

}

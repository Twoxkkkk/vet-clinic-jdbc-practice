package application.shared;

import domain.repository.Repository;
import domain.shared.Id;

public abstract class BaseService <E, R extends Repository<E>> {

    protected final R repository;

    public BaseService(R repository){
        if(repository == null){
            throw new IllegalArgumentException("Couldn't get valid repository factory!");
        }

        this.repository = repository;
    }

    public E getById(Id<E> id){
        return repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Couldn't get entity by id!")
        );
    }

}

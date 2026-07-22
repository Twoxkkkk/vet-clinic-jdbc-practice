package application;

public interface RepositoryFactory<T> {
    T create();
}

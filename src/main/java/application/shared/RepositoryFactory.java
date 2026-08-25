package application.shared;

public interface RepositoryFactory<T> {
    T create();
}

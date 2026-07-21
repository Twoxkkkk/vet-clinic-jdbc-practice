package infrastructure.database.exceptions;

public class QueryException extends RuntimeException {
    public QueryException(String message, Throwable e) {
        super(message, e);
    }
}

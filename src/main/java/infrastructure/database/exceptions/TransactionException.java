package infrastructure.database.exceptions;

public class TransactionException extends RuntimeException {
    public TransactionException(String message, Throwable e) {
        super(message, e);
    }
}

package domain.shared.exceptions;

public class NullOrBlankException extends RuntimeException {
    public NullOrBlankException(String message) {
        super(message);
    }
}

package ni.com.user.security.support.exception;

public class ValueAlreadyExistsException extends RuntimeException {
    public ValueAlreadyExistsException(String message) {
        super(message);
    }
}

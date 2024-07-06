package ni.com.user.security.support.exception;

public class InvalidRequestParameterException extends RuntimeException {
    public InvalidRequestParameterException(String message) {
        super(message);
    }
}

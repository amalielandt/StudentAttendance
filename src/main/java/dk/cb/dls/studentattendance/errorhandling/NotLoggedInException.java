package dk.cb.dls.studentattendance.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotLoggedInException extends Exception {
    public NotLoggedInException() {
        super();
    }
    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotLoggedInException(String message) {
        super(message);
    }
    public NotLoggedInException(Throwable cause) {
        super(cause);
    }
}

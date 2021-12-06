package dk.cb.dls.studentattendance.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotAllowedException extends Exception {
    public NotAllowedException() {
        super();
    }
    public NotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotAllowedException(String message) {
        super(message);
    }
    public NotAllowedException(Throwable cause) {
        super(cause);
    }
}

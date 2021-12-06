package dk.cb.dls.studentattendance.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class LocationException extends Exception {
    public LocationException() {
        super();
    }
    public LocationException(String message, Throwable cause) {
        super(message, cause);
    }
    public LocationException(String message) {
        super(message);
    }
    public LocationException(Throwable cause) {
        super(cause);
    }
}

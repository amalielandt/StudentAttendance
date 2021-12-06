package dk.cb.dls.studentattendance.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoginException extends Exception {
    public LoginException() {
        super();
    }
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
    public LoginException(String message) {
        super(message);
    }
    public LoginException(Throwable cause) {
        super(cause);
    }
}

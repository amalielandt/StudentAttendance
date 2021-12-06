package dk.cb.dls.studentattendance.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class AttendanceException extends Exception {
    public AttendanceException() {
        super();
    }
    public AttendanceException(String message, Throwable cause) {
        super(message, cause);
    }
    public AttendanceException(String message) {
        super(message);
    }
    public AttendanceException(Throwable cause) {
        super(cause);
    }
}

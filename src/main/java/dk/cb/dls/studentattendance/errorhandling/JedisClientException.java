package dk.cb.dls.studentattendance.errorhandling;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class JedisClientException extends Exception {
    public JedisClientException() {
        super();
    }
    public JedisClientException(String message, Throwable cause) {
        super(message, cause);
    }
    public JedisClientException(String message) {
        super(message);
    }
    public JedisClientException(Throwable cause) {
        super(cause);
    }
}

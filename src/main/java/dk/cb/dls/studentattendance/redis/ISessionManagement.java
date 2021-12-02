package dk.cb.dls.studentattendance.redis;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface ISessionManagement {
    String setSession(Session session, UUID id);
    UUID getSession(Session session, String token);
}

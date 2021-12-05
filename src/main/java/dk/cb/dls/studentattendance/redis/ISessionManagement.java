package dk.cb.dls.studentattendance.redis;

import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.util.UUID;

public interface ISessionManagement {
    String setSession(Session session, UUID id) throws JedisException, NullPointerException;
    UUID getSession(Session session, String token);
    void setLocation(UUID id, String ip) throws IOException, JedisException;
    boolean acceptLocation (UUID teacherId, String ip) throws IOException, NullPointerException;
}

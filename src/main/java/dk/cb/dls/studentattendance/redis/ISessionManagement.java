package dk.cb.dls.studentattendance.redis;

import dk.cb.dls.studentattendance.errorhandling.JedisClientException;
import dk.cb.dls.studentattendance.errorhandling.LocationException;
import dk.cb.dls.studentattendance.errorhandling.NotFoundException;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.util.UUID;

public interface ISessionManagement {
    String setSession(Session session, UUID id) throws JedisClientException;
    UUID getSession(Session session, String token) throws JedisClientException;
    void setLocation(UUID id, String ip) throws JedisClientException, LocationException;
    boolean acceptLocation (UUID teacherId, String ip) throws NotFoundException, LocationException, JedisClientException;
}

package dk.cb.dls.studentattendance.redis;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.time.Instant;
import java.util.UUID;

@Service
public class SessionManagement implements ISessionManagement {
    private JedisClient jedis;
    private long sessionTime;

    public SessionManagement() {

        this.jedis = JedisClient.getJedisClient();
        this.sessionTime = 60*30;
    }

    @Override
    public String setSession(Session session, UUID id) {

        StringBuilder builder = new StringBuilder();
        long currentTimeInMilliseconds = Instant.now().toEpochMilli();
        String token = builder.append(currentTimeInMilliseconds).append("-").append(UUID.randomUUID()).toString();
        String key = "session:" + session.toString().toLowerCase() + ":" + token;

        boolean success = jedis.setWithExpire(key, id.toString(), sessionTime);
        if (success) {
            return token;
        }
        return null;
    }

    @Override
    public UUID getSession(Session session, String token) {
        String id = jedis.get("session:" + session.toString().toLowerCase() + ":" + token);
        if(id != null) {
            return UUID.fromString(id);
        }
        return null;
    }
}

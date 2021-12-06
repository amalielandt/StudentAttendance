package dk.cb.dls.studentattendance.redis;

import dk.cb.dls.studentattendance.DTO.LocationDTO;
import dk.cb.dls.studentattendance.client.LocationClient;
import dk.cb.dls.studentattendance.errorhandling.JedisClientException;
import dk.cb.dls.studentattendance.errorhandling.LocationException;
import dk.cb.dls.studentattendance.errorhandling.NotFoundException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
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
    public String setSession(Session session, UUID id) throws JedisClientException {
        StringBuilder builder = new StringBuilder();
        long currentTimeInMilliseconds = Instant.now().toEpochMilli();
        String token = builder.append(currentTimeInMilliseconds).append("-").append(UUID.randomUUID()).toString();
        String key = "session:" + session.toString().toLowerCase() + ":" + token;

        jedis.setWithExpire(key, id.toString(), sessionTime);
        return token;
    }

    @Override
    public UUID getSession(Session session, String token) throws JedisClientException {
        String id = jedis.get("session:" + session.toString().toLowerCase() + ":" + token);
        if(id != null) {
            return UUID.fromString(id);
        }
        return null;
    }

    @Override
    public void setLocation(UUID id, String ip) throws JedisClientException, LocationException {
        ip = checkIp(ip);
        LocationDTO location = LocationClient.getLocation(ip);
        String longitudeKey = "location:" + id + ":longitude";
        String latitudeKey = "location:" + id + ":latitude";

        HashMap<String, String> redisValues = new HashMap<String, String>();

        redisValues.put(latitudeKey, String.valueOf(location.getLatitude()));
        redisValues.put(longitudeKey, String.valueOf(location.getLongitude()));

        jedis.setMultiWithExpire(redisValues, sessionTime);
    }

    @Override
    public boolean acceptLocation (UUID teacherId, String ip) throws NotFoundException, LocationException, JedisClientException {
        ip = checkIp(ip);
        LocationDTO studentLocation = LocationClient.getLocation(ip);

        double teacherLongitude = Double.valueOf(jedis.get("location:" + teacherId + ":longitude"));
        double teacherLatitude = Double.valueOf(jedis.get("location:" + teacherId + ":latitude"));

        if(teacherLatitude == 0 || teacherLongitude == 0 ) {
            throw new NotFoundException("Teacher's (" + teacherId +") location could not be found");
        }

        double longitudeDiff = teacherLongitude - studentLocation.getLongitude();
        double latitudeDiff = teacherLatitude - studentLocation.getLatitude();

        if((0.001 > latitudeDiff && latitudeDiff > -0.001) && (0.001 > longitudeDiff && longitudeDiff > -0.001)) {
            return true;
        }
        return false;
    }

    private String checkIp(String ip) throws LocationException {
        if(ip.startsWith("192") || ip.startsWith("10.24") || ip.equals("0:0:0:0:0:0:0:1") || ip.startsWith("0.0.0.0"))
        {
            ip = LocationClient.getIp();
        }
        return ip;
    }
}

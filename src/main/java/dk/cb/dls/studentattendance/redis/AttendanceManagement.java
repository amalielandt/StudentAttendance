package dk.cb.dls.studentattendance.redis;

import dk.cb.dls.studentattendance.errorhandling.JedisClientException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.exceptions.JedisException;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class AttendanceManagement implements IAttendanceManagement {

    private JedisClient jedis;
    private long expire;

    public AttendanceManagement() {

        this.jedis = JedisClient.getJedisClient();
        this.expire = 60*15;
    }

    @Override
    public String setAttendanceCode(UUID lectureId) throws JedisClientException {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String attendanceCode = String.format("%05d", num);

        jedis.setWithExpire("attendance:"+attendanceCode, lectureId.toString(), expire);
        return attendanceCode;
    }

    @Override
    public UUID getLectureId(String attendanceCode) throws JedisClientException {
        String id = jedis.get("attendance:"+attendanceCode);
        if(id != null) {
            return UUID.fromString(id);
        }
        return null;
    }
}

package dk.cb.dls.studentattendance.redis;

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
    public String setAttendanceCode(UUID lectureId) throws JedisException {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String attendanceCode = String.format("%05d", num);

        jedis.setWithExpire("attendance:"+attendanceCode, lectureId.toString(), expire);
        return attendanceCode;
    }

    @Override
    public UUID getLectureId(String attendanceCode) {
        String id = jedis.get("attendance:"+attendanceCode);
        if(id != null) {
            return UUID.fromString(id);
        }
        return null;
    }
}

package dk.cb.dls.studentattendance.redis;

import dk.cb.dls.studentattendance.errorhandling.JedisClientException;

import java.util.UUID;

public interface IAttendanceManagement {
    String setAttendanceCode(UUID lectureId) throws JedisClientException;
    UUID getLectureId(String attendanceCode) throws JedisClientException;
}

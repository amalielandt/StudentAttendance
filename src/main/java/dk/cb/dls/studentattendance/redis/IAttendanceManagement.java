package dk.cb.dls.studentattendance.redis;

import java.util.UUID;

public interface IAttendanceManagement {
    String setAttendanceCode(UUID lectureId);
    UUID getLectureId(String attendanceCode);
}

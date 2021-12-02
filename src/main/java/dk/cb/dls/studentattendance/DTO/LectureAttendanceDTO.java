package dk.cb.dls.studentattendance.DTO;

import dk.cb.dls.studentattendance.models.Lecture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureAttendanceDTO {
    private String date;
    private int attendance;
    private int students;

    public LectureAttendanceDTO(Lecture lecture) {
        this.date = lecture.getDate();
        this.attendance = lecture.attendanceCount();
        this.students = lecture.studentCount();
    }

}

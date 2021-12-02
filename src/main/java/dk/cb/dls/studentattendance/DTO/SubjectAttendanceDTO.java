package dk.cb.dls.studentattendance.DTO;

import dk.cb.dls.studentattendance.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectAttendanceDTO {
    private String name;
    private String teacher;
    private int attendance;
    private int lectures;

    public SubjectAttendanceDTO(Subject subject, UUID studentId) {

        this.name = subject.getName();
        this.teacher = subject.getTeacher().getName();
        this.attendance = subject.getStudentAttendance(studentId);
        this.lectures = subject.lectureCount();
    }
}

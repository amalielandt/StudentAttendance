package dk.cb.dls.studentattendance.DTO;

import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubDTO {
    private UUID id;
    private String name;

    public SubDTO(Student student) {
        this.id = student.getId();
        this.name = student.getName();
    }

    public SubDTO(Teacher teacher) {
        this.id = teacher.getId();
        this.name = teacher.getName();
    }

    public SubDTO(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
    }
}

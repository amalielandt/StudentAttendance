package dk.cb.dls.studentattendance.DTO;

import dk.cb.dls.studentattendance.models.Lecture;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSubjectDTO {

    private UUID id;
    private String name;
    private SubDTO teacher;
    private List<SubDTO> students = new ArrayList<>();
    private List<SubDTO> lectures = new ArrayList<>();

    public StudentSubjectDTO(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.teacher = new SubDTO(subject.getTeacher());

        for (Student student : subject.getStudents()) {
            this.students.add(new SubDTO(student));
        }

        for (Lecture lecture : subject.getLectures()) {
            this.lectures.add(new SubDTO(lecture));
        }
    }
}

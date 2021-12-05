package dk.cb.dls.studentattendance.DTO;

import dk.cb.dls.studentattendance.models.Lecture;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    private UUID id;
    @NotEmpty(message = "Name may not be empty")
    @NotNull(message = "Name may not be null")
    private String name;
    @NotEmpty(message = "Teacher may not be empty")
    @NotNull(message = "Teacher may not be null")
    private SubDTO teacher;
    private List<SubDTO> students = new ArrayList<>();
    private List<LectureDTO> lectures = new ArrayList<>();

    public SubjectDTO(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.teacher = new SubDTO(subject.getTeacher());

        for (Student student : subject.getStudents()) {
            this.students.add(new SubDTO(student));
        }

        for (Lecture lecture : subject.getLectures()) {
            this.lectures.add(new LectureDTO(lecture));
        }
    }
}

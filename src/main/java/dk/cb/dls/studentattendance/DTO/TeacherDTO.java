package dk.cb.dls.studentattendance.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherDTO {

    private UUID id;
    @NotBlank(message = "Email must be provided")
    private String email;
    @NotBlank(message = "Password must be provided")
    private String password;
    @NotBlank(message = "Name must be provided")
    private String name;
    private List<SubDTO> subjects = new ArrayList<>();

    public TeacherDTO(Teacher teacher) {
        this.id = teacher.getId();
        this.email = teacher.getEmail();
        this.name = teacher.getName();

        for (Subject subject : teacher.getSubjects()) {
            this.subjects.add(new SubDTO(subject));
        }
    }
}


package dk.cb.dls.studentattendance.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherDTO {

    private UUID id;
    @NotEmpty(message = "Email may not be empty")
    @NotNull(message = "Email may not be null")
    private String email;
    @NotEmpty(message = "Password may not be empty")
    @NotNull(message = "Password may not be null")
    private String password;
    @NotEmpty(message = "Name may not be empty")
    @NotNull(message = "Name may not be null")
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


package dk.cb.dls.studentattendance.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    @Pattern(message="Email not valid format",
            regexp = "^(?:[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$",
            flags = Pattern.Flag.UNICODE_CASE)
    private String email;
    @NotBlank(message = "Password must be provided")
    @Size(min = 4, message = "Password must be minimum 4 characters long")
    private String password;
    @NotBlank(message = "Full name must be provided")
    @Pattern(message="Full name must contain both firstname and lastname",
            regexp = "^\\w* \\w*.*$", flags = Pattern.Flag.UNICODE_CASE)
    private String fullName;
    private List<SubDTO> subjects = new ArrayList<>();

    public TeacherDTO(Teacher teacher) {
        this.id = teacher.getId();
        this.email = teacher.getEmail();
        this.fullName = teacher.getFullName();

        for (Subject subject : teacher.getSubjects()) {
            this.subjects.add(new SubDTO(subject));
        }
    }
}


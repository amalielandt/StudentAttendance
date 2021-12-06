package dk.cb.dls.studentattendance.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {

    private UUID id;
    @NotBlank(message = "Name must be provided")
    private String name;
    @NotBlank(message = "Email must be provided")
    private String email;
    @NotBlank(message = "Password must be provided")
    private String password;
    @NotBlank(message = "Phonenumber must be provided")
    private String phonenumber;
    @NotBlank(message = "Address must be provided")
    private String address;
    @NotBlank(message = "City must be provided")
    private String city;
    @NotBlank(message = "Zipcode must be provided")
    private String zipcode;
    @NotBlank(message = "Birthdate must be provided")
    @Pattern(message="Birthdate must be given in pattern: dd.mm.yyyy", regexp = "^[0,1,2,3][0,1-9].[0,1][0,1-9].[1,2][0,1,8,9][0,1-9][0,1-9]$", flags = Pattern.Flag.UNICODE_CASE)
    private String birthdate;
    private List<SubDTO> subjects = new ArrayList<>();

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.phonenumber = student.getPhonenumber();
        this.address = student.getAddress();
        this.city = student.getCity();
        this.zipcode = student.getZipcode();
        this.birthdate = student.getBirthdate();

        for (Subject subject : student.getSubjects()) {
            this.subjects.add(new SubDTO(subject));
        }
    }
}

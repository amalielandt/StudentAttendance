package dk.cb.dls.studentattendance.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {

    private UUID id;
    @NotEmpty(message = "Name may not be empty")
    @NotNull(message = "Name may not be null")
    private String name;
    @NotEmpty(message = "Email may not be empty")
    @NotNull(message = "Email may not be null")
    private String email;
    @NotEmpty(message = "Password may not be empty")
    @NotNull(message = "Password may not be null")
    private String password;
    @NotEmpty(message = "Phonenumber may not be empty")
    @NotNull(message = "Phonenumber may not be null")
    private String phonenumber;
    @NotEmpty(message = "Address may not be empty")
    @NotNull(message = "Address may not be null")
    private String address;
    @NotEmpty(message = "City may not be empty")
    @NotNull(message = "City may not be null")
    private String city;
    @NotEmpty(message = "Zipcode may not be empty")
    @NotNull(message = "Zipcode may not be null")
    private String zipcode;
    @NotEmpty(message = "Birthday may not be empty")
    @NotNull(message = "Birthday may not be null")
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

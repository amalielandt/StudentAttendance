package dk.cb.dls.studentattendance.DTO;

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
public class StudentDTO {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private String phonenumber;
    private String address;
    private String city;
    private String zipcode;
    private String birthdate;
    private List<SubDTO> subjects = new ArrayList<>();

    public StudentDTO(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.password = student.getPassword();
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

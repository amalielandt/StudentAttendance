package dk.cb.dls.studentattendance.models;

import dk.cb.dls.studentattendance.DTO.StudentDTO;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@NamedQueries({
        @NamedQuery(name="Student.findByEmail", query="SELECT s FROM Student s WHERE s.email = :email"),
        @NamedQuery(name="Student.findAll", query="SELECT s FROM Student s"),
        @NamedQuery(name="Student.deleteAll", query="DELETE FROM Student s")
})
public class Student {

    @Id
    private UUID id;
    @Column(unique = true)
    private String email;
    private String password;
    private String fullName;
    @Column(length = 8, unique = true)
    private String phonenumber;

    @ManyToMany(mappedBy = "students", cascade = {CascadeType.MERGE})
    private List<Subject> subjects;

    public Student(String fullName, String email, String password, String phonenumber) {
        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.password = hash(password);
        this.phonenumber = phonenumber;
        this.subjects = new ArrayList();
    }

    public Student(StudentDTO student) {
        this.id = UUID.randomUUID();
        this.fullName = student.getFullName();
        this.email = student.getEmail();
        this.password = hash(student.getPassword());
        this.phonenumber = student.getPhonenumber();
        this.subjects = new ArrayList();
    }

    public Student() {}

    public void addSubject(Subject subject) {
        if(!this.subjects.contains(subject)) {
            this.subjects.add(subject);
            subject.addStudent(this);
        }

    }

    public Subject getSubject(UUID id) {
        for (Subject subject: subjects) {
            if(subject.getId().equals(id)) {
                return subject;
            }
        }
        return null;
    }

    private String hash(String password) {
        password += "chokobanan";
        long sum = 0, mul = 1;
        for (int i = 0; i < password.length(); i++) {
            mul = (i % 4 == 0) ? 1 : mul * 256;
            sum += password.charAt(i) * mul;
        }
        return Long.toString(Math.abs(sum) % 99999);
    }



}

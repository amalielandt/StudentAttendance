package dk.cb.dls.studentattendance.models;

import dk.cb.dls.studentattendance.DTO.TeacherDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NamedQueries({
        @NamedQuery(name="Teacher.findByEmail", query="SELECT t FROM Teacher t WHERE t.email = :email"),
        @NamedQuery(name="Teacher.findAll", query="SELECT t FROM Teacher t"),
        @NamedQuery(name="Teacher.deleteAll", query="DELETE FROM Teacher t")
})
public class Teacher {

    @Id
    private UUID id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;

    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Subject> subjects;

    //Only used for Unit testing
    public Teacher(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.subjects = new ArrayList();
    }

    public Teacher(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = hash(password);
        this.subjects = new ArrayList();
    }

    public Teacher() {}

    public Teacher(TeacherDTO teacher) {
        this.id = UUID.randomUUID();
        this.name = teacher.getName();
        this.email = teacher.getEmail();
        this.password = hash(teacher.getPassword());
        this.subjects = new ArrayList();
    }

    public void addSubject(Subject subject) {
        if(!this.subjects.contains(subject)){
            this.subjects.add(subject);
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
        return Long.toString(Math.abs(sum) % 3000);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

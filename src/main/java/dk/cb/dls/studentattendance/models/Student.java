package dk.cb.dls.studentattendance.models;

import dk.cb.dls.studentattendance.DTO.StudentDTO;
import dk.cb.dls.studentattendance.utils.Utils;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String name;
    @Column(unique = true)
    private String phonenumber;
    private String address;
    private String city;
    private String zipcode;
    private String birthdate;

    @ManyToMany(mappedBy = "students", cascade = {CascadeType.MERGE})
    private List<Subject> subjects;

    //Only used for Unit testing
    public Student(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.subjects = new ArrayList();
    }

    public Student(String name, String email, String password, String phonenumber, String address, String city, String zipcode, Date birthdate) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = hash(password);
        this.phonenumber = phonenumber;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.birthdate = Utils.formatter.format(birthdate);
    }

    public Student(StudentDTO student) {
        this.id = UUID.randomUUID();
        this.name = student.getName();
        this.email = student.getEmail();
        this.password = hash(student.getPassword());
        this.phonenumber = student.getPhonenumber();
        this.address = student.getAddress();
        this.city = student.getCity();
        this.zipcode = student.getZipcode();
        this.birthdate = student.getBirthdate();
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
        return Long.toString(Math.abs(sum) % 3000);
    }



}

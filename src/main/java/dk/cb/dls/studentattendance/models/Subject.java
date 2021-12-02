package dk.cb.dls.studentattendance.models;

import dk.cb.dls.studentattendance.DTO.SubjectDTO;
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
        @NamedQuery(name="Subject.findAll", query="SELECT s FROM Subject s"),
        @NamedQuery(name="Subject.deleteAll", query="DELETE FROM Subject s")
})
public class Subject {

    @Id
    private UUID id;
    @Column(unique = true)
    private String name;


    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "subject_student", joinColumns = {
            @JoinColumn(name = "subject_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "student_id", referencedColumnName = "id")})
    private List<Student> students;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Teacher teacher;

    @OneToMany(mappedBy = "subject", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Lecture> lectures;

    public Subject(String name, Teacher teacher) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.students = new ArrayList();
        this.teacher = teacher;
        this.lectures = new ArrayList<>();
    }

    public Subject(SubjectDTO subjectDTO, Teacher teacher) {
        this.id = UUID.randomUUID();
        this.name = subjectDTO.getName();
        this.students = new ArrayList();
        this.teacher = teacher;
        this.lectures = new ArrayList<>();
    }

    public Subject() {}

    public void addStudent(Student student) {

        if(!this.students.contains(student)){
            this.students.add(student);
            student.addSubject(this);
        }
    }

    public Student getStudent(UUID id) {
        for (Student student: students) {
            if(student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public Lecture addLecture(Date date) {
        Lecture lecture = getLecture(date);
        if(lecture == null) {
            lecture = new Lecture(this, date);
            lectures.add(lecture);
        }


        return lecture;
    }

    public Lecture getLecture(Date date) {
        String dateAsString = Utils.formatter.format(date);
        for (Lecture lecture: lectures) {
            if(lecture.getDate().equals(dateAsString)) {
                return lecture;
            }
        }
        return null;
    }

    public int lectureCount() {
        return lectures.size();
    }

    public int getStudentAttendance(UUID studentId) {
        Student student = getStudent(studentId);

        int count = 0;
        if(student != null) {
            for (Lecture lecture : lectures) {
                if(lecture.getAttendee(student.getId()) != null) {
                    count++;
                }
            }
        }
        return count;
    }


    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                '}';
    }
}

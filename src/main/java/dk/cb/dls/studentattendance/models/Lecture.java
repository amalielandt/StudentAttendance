package dk.cb.dls.studentattendance.models;

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
        @NamedQuery(name="Lecture.findAll", query="SELECT l FROM Lecture l"),
        @NamedQuery(name="Lecture.deleteAll", query="DELETE FROM Lecture l")
})
public class Lecture {

    @Id
    private UUID id;
    private String date;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Subject subject;

    @OneToMany
    @JoinTable(name = "lecture_student", joinColumns = {
            @JoinColumn(name = "Lecture_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "student_id", referencedColumnName = "id")})
    private List<Student> attendees;

    public Lecture(Subject subject, Date date) {
        this.id = UUID.randomUUID();
        this.date = Utils.formatter.format(date);
        this.subject = subject;
        this.attendees = new ArrayList();
    }

    public Lecture() {}

    public void addAttendee(Student attendee) {
        if(!this.attendees.contains(attendee)) {
            this.attendees.add(attendee);
        }

    }

    public Student getAttendee(UUID id) {
        for (Student attendee: attendees) {
            if(attendee.getId().equals(id)) {
                return attendee;
            }
        }
        return null;
    }
}

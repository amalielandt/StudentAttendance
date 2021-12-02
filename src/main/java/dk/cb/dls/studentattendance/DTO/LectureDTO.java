package dk.cb.dls.studentattendance.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import dk.cb.dls.studentattendance.models.Lecture;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {
    private UUID id;
    private String date;
    private List<SubDTO> attendees = new ArrayList<>();

    public LectureDTO(Lecture lecture) {
        this.id = lecture.getId();
        this.date = lecture.getDate();
        for (Student attendee : lecture.getAttendees()) {
            this.attendees.add(new SubDTO(attendee));
        }
    }
}

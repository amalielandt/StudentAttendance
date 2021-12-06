package dk.cb.dls.studentattendance.DTO;

import dk.cb.dls.studentattendance.models.Lecture;
import dk.cb.dls.studentattendance.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {
    private UUID id;
    @NotBlank(message = "Date must be provided")
    @Pattern(message="Date must be given in pattern: dd.mm.yyyy", regexp = "^[0,1,2,3][0,1-9].[0,1][0,1-9].[1,2][0,1,8,9][0,1-9][0,1-9]$", flags = Pattern.Flag.UNICODE_CASE)
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

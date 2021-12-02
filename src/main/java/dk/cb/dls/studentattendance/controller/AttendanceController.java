package dk.cb.dls.studentattendance.controller;

import dk.cb.dls.studentattendance.DTO.LoginDTO;
import dk.cb.dls.studentattendance.DTO.SubjectDTO;
import dk.cb.dls.studentattendance.models.Lecture;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
import dk.cb.dls.studentattendance.redis.AttendanceManagement;
import dk.cb.dls.studentattendance.redis.Session;
import dk.cb.dls.studentattendance.redis.SessionManagement;
import dk.cb.dls.studentattendance.repository.LectureRepository;
import dk.cb.dls.studentattendance.repository.StudentRepository;
import dk.cb.dls.studentattendance.repository.SubjectRepository;
import dk.cb.dls.studentattendance.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    SessionManagement sessionManagement;
    @Autowired
    AttendanceManagement attendanceManagement;

    @PostMapping("/login/{session}")
    public String login(@PathVariable Session session, @RequestBody LoginDTO login) {
//        String ip = request.getRemoteAddr();
        if (session.equals(Session.STUDENT)) {
            Student student = studentRepository.findOneByEmail(login.getEmail());
            if (student != null) {
                if (student.getPassword().equals(login.hash())) {
                    String token = sessionManagement.setSession(session, student.getId());
                    return token;
                }
            }
        } else if (session.equals(Session.TEACHER)) {
            Teacher teacher = teacherRepository.findOneByEmail(login.getEmail());
            if (teacher != null) {
                if (teacher.getPassword().equals(login.hash())) {
                    String token = sessionManagement.setSession(session, teacher.getId());
                    return token;
                }
            }
        }
        return null;
    }

    @GetMapping("/{lectureId}")
    public String getAttendanceCode(@PathVariable UUID lectureId, @RequestBody String token) {
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if(teacherId != null) {
            Optional<Lecture> optionalLecture = lectureRepository.findById(lectureId);
            if(optionalLecture.isPresent())
            {
                Lecture lecture = optionalLecture.get();
                if(lecture.getSubject().getTeacher().getId().equals(teacherId))
                {
                    String attendanceCode = attendanceManagement.setAttendanceCode(lecture.getId());
                    return attendanceCode;
                }
            }
        }
        return null;
    }

    @PutMapping("/{subjectId}/{studentId}")
    public SubjectDTO studentAttendsSubject(@PathVariable UUID subjectId, @PathVariable UUID studentId, @RequestBody String token) {
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if(teacherId != null) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);

            if(optionalSubject.isPresent() && optionalStudent.isPresent())
            {
                Subject subject = optionalSubject.get();
                Student student = optionalStudent.get();

                subject.addStudent(student);
                subject = subjectRepository.save(subject);
                return new SubjectDTO(subject);
            }
        }
        return null;
    }

    @PutMapping("/{attendanceCode}")
    public SubjectDTO studentAttendsLecture(@PathVariable String attendanceCode, @RequestBody String token) {
        UUID studentId = sessionManagement.getSession(Session.STUDENT, token);
        UUID lectureId = attendanceManagement.getLectureId(attendanceCode);

        if(studentId != null && lectureId != null) {
            Optional<Lecture> optionalLecture = lectureRepository.findById(lectureId);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);

            if(optionalLecture.isPresent() && optionalStudent.isPresent())
            {
                Lecture lecture = optionalLecture.get();
                Student student = optionalStudent.get();

                if(lecture.getSubject().getStudent(student.getId()) != null)
                {
                    lecture.addAttendee(student);
                    lecture = lectureRepository.save(lecture);
                    return new SubjectDTO(lecture.getSubject());
                }
            }
        }
        return null;
    }
}
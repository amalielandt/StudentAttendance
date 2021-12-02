package dk.cb.dls.studentattendance.controller;

import dk.cb.dls.studentattendance.DTO.*;
import dk.cb.dls.studentattendance.client.LocationClient;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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


    @GetMapping("/subject/{subjectId}")
    public List<LectureAttendanceDTO> getSubjectAttendance(@PathVariable UUID subjectId, @RequestBody String token) {
        List<LectureAttendanceDTO> lectureAttendance = new ArrayList<>();
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if(teacherId != null) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if(optionalSubject.isPresent())
            {
                Subject subject = optionalSubject.get();
                for (Lecture lecture : subject.getLectures()) {
                    lectureAttendance.add(new LectureAttendanceDTO(lecture));
                }

                return lectureAttendance;
            }
        }
        return null;
    }

    @GetMapping("/student/{studentId}")
    public List<SubjectAttendanceDTO> getStudentAttendance(@PathVariable UUID studentId, @RequestBody String token) {
        List<SubjectAttendanceDTO> studentAttendance = new ArrayList<>();
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);
        UUID studentID = sessionManagement.getSession(Session.STUDENT, token);

        if(studentID != null) {
            studentId = studentID;
        }
        if(studentID != null || teacherID != null) {
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if(optionalStudent.isPresent())
            {
                Student student = optionalStudent.get();
                for (Subject subject : student.getSubjects()) {
                    studentAttendance.add(new SubjectAttendanceDTO(subject, student.getId()));
                }
                return studentAttendance;
            }
        }
        return null;
    }

    @GetMapping("/location/")
    public LocationDTO getIp(HttpServletRequest request) throws IOException {
        String ip = request.getRemoteAddr();
        LocationDTO location = LocationClient.getLocation(ip);
        return location;
    }
}
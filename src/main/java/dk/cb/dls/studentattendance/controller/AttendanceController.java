package dk.cb.dls.studentattendance.controller;

import dk.cb.dls.studentattendance.DTO.*;
import dk.cb.dls.studentattendance.errorhandling.*;
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
import javax.validation.Valid;
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
    public String login(HttpServletRequest request, @PathVariable Session session, @RequestBody @Valid LoginDTO login) throws JedisClientException, LoginException, LocationException {
        String ip = request.getRemoteAddr();
        String token = null;

        if (session.equals(Session.STUDENT)) {
            Student student = studentRepository.findOneByEmail(login.getEmail());
            if (student != null) {
                if (student.getPassword().equals(login.hash())) {
                    token = sessionManagement.setSession(session, student.getId());
                    return token;
                }
                throw new LoginException("Wrong email or password");
            }
            throw new LoginException("Wrong email or password");

        } else if (session.equals(Session.TEACHER)) {
            Teacher teacher = teacherRepository.findOneByEmail(login.getEmail());
            if (teacher != null) {
                if (teacher.getPassword().equals(login.hash())) {
                    token = sessionManagement.setSession(session, teacher.getId());
                    sessionManagement.setLocation(teacher.getId(), ip);
                    return token;
                }
                throw new LoginException("Wrong email or password");
            }
            throw new LoginException("Wrong email or password");
        }
        throw new LoginException("Only STUDENT or TEACHER is authorized");
    }

    @GetMapping("/{lectureId}")
    public String getAttendanceCode(@PathVariable UUID lectureId, @RequestHeader("Session-Token") String token) throws NotLoggedInException, JedisClientException, NotFoundException, NotAllowedException {
        String attendanceCode = null;
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherId != null) {
            Optional<Lecture> optionalLecture = lectureRepository.findById(lectureId);
            if (optionalLecture.isPresent()) {
                Lecture lecture = optionalLecture.get();
                if (lecture.getSubject().getTeacher().getId().equals(teacherId)) {
                    attendanceCode = attendanceManagement.setAttendanceCode(lecture.getId());
                    return attendanceCode;
                }
                throw new NotAllowedException("Teacher with id: " + teacherId + "does not teach this subject");
            }
            throw new NotFoundException("Lecture with id: " + lectureId + "not found");
        }
        throw new NotLoggedInException("Teacher not logged in");
    }

    @PutMapping("/{subjectId}/{studentId}")
    public SubjectDTO studentAttendsSubject(@PathVariable UUID subjectId, @PathVariable UUID studentId, @RequestHeader("Session-Token") String token) throws JedisClientException, NotLoggedInException, NotFoundException {
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherId != null) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);

            if (optionalSubject.isPresent() && optionalStudent.isPresent()) {
                Subject subject = optionalSubject.get();
                Student student = optionalStudent.get();

                subject.addStudent(student);
                subject = subjectRepository.save(subject);
                return new SubjectDTO(subject);
            }
            throw new NotFoundException("Subject or student not found");
        }
        throw new NotLoggedInException("Teacher not logged in");
    }

    @PutMapping("/{attendanceCode}")
    public SubjectDTO studentAttendsLecture(HttpServletRequest request, @PathVariable String attendanceCode, @RequestHeader("Session-Token") String token) throws JedisClientException, NotLoggedInException, NotFoundException, LocationException, AttendanceException {
        String ip = request.getRemoteAddr();
        UUID studentId = sessionManagement.getSession(Session.STUDENT, token);
        UUID lectureId = attendanceManagement.getLectureId(attendanceCode);

        if (studentId != null && lectureId != null) {
            Optional<Lecture> optionalLecture = lectureRepository.findById(lectureId);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);

            if (optionalLecture.isPresent() && optionalStudent.isPresent()) {
                Lecture lecture = optionalLecture.get();
                Student student = optionalStudent.get();
                boolean locationAccepted = sessionManagement.acceptLocation(lecture.getSubject().getTeacher().getId(), ip);
                if (locationAccepted && lecture.getSubject().getStudent(student.getId()) != null) {
                    lecture.addAttendee(student);
                    lecture = lectureRepository.save(lecture);
                    return new SubjectDTO(lecture.getSubject());
                }
                throw new AttendanceException("You must be within a distance to your teacher, to attend the lecture");
            }
            throw new NotFoundException("Student(" + studentId + ") or Lecture(" + lectureId + ") not found");
        }
        throw new NotLoggedInException("Student not logged in or attendance code " + attendanceCode + " is not valid");
    }

    @GetMapping("/subject/{subjectId}")
    public List<LectureAttendanceDTO> getSubjectAttendance(@PathVariable UUID subjectId, @RequestHeader("Session-Token") String token) throws JedisClientException, NotLoggedInException, NotFoundException {
        List<LectureAttendanceDTO> lectureAttendance = new ArrayList<>();
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherId != null) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (optionalSubject.isPresent()) {
                Subject subject = optionalSubject.get();
                for (Lecture lecture : subject.getLectures()) {
                    lectureAttendance.add(new LectureAttendanceDTO(lecture));
                }
            }
            throw new NotFoundException("Subject with id: " + subjectId + " not found");
        }
        throw new NotLoggedInException("Teacher not logged in");

    }

    @GetMapping("/student/{studentId}")
    public List<SubjectAttendanceDTO> getStudentAttendance(@PathVariable UUID studentId, @RequestHeader("Session-Token") String token) throws JedisClientException, NotLoggedInException, NotFoundException {
        List<SubjectAttendanceDTO> studentAttendance = new ArrayList<>();
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);
        UUID studentID = sessionManagement.getSession(Session.STUDENT, token);

        if (studentID != null) {
            studentId = studentID;
        }
        if (studentID != null || teacherID != null) {
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();
                for (Subject subject : student.getSubjects()) {
                    studentAttendance.add(new SubjectAttendanceDTO(subject, student.getId()));
                }
            }
            throw new NotFoundException("Student with id: " + studentId + " not found");
        }
        throw new NotLoggedInException("Login required");


    }
}
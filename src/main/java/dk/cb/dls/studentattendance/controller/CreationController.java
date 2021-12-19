package dk.cb.dls.studentattendance.controller;

import dk.cb.dls.studentattendance.DTO.StudentDTO;
import dk.cb.dls.studentattendance.DTO.SubjectDTO;
import dk.cb.dls.studentattendance.DTO.TeacherDTO;
import dk.cb.dls.studentattendance.errorhandling.JedisClientException;
import dk.cb.dls.studentattendance.errorhandling.NotFoundException;
import dk.cb.dls.studentattendance.errorhandling.NotLoggedInException;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
import dk.cb.dls.studentattendance.redis.Session;
import dk.cb.dls.studentattendance.redis.SessionManagement;
import dk.cb.dls.studentattendance.repository.LectureRepository;
import dk.cb.dls.studentattendance.repository.StudentRepository;
import dk.cb.dls.studentattendance.repository.SubjectRepository;
import dk.cb.dls.studentattendance.repository.TeacherRepository;
import dk.cb.dls.studentattendance.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/new")
public class CreationController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    SessionManagement sessionManagement;
    @Autowired
    LectureRepository lectureRepository;

    @PostMapping("/student")
    public StudentDTO newStudent(@RequestBody @Valid StudentDTO studentDTO, @RequestHeader("Session-Token") String token) throws NotLoggedInException, JedisClientException {
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherId != null) {
            Student student = new Student(studentDTO);
            studentRepository.save(student);
            Optional<Student> optionalStudent = studentRepository.findById(student.getId());
            return new StudentDTO(optionalStudent.get());
        }
        throw new NotLoggedInException("Teacher not logged in");
    }

    @PostMapping("/teacher")
    public TeacherDTO newTeacher(@RequestBody @Valid TeacherDTO teacherDTO, @RequestHeader("Session-Token") String token) throws JedisClientException, NotLoggedInException {
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if(teacherId != null) {
            Teacher teacher = new Teacher(teacherDTO);
            teacherRepository.save(teacher);
            Optional<Teacher> optionalTeacher = teacherRepository.findById(teacher.getId());
            return new TeacherDTO(optionalTeacher.get());
           }
        throw new NotLoggedInException("Teacher not logged in");
    }

    @PostMapping("/subject")
    public SubjectDTO newSubject(@RequestBody @Valid SubjectDTO subjectDTO, @RequestHeader("Session-Token") String token) throws JedisClientException, NotLoggedInException, NotFoundException {
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if(teacherId != null) {
            Optional<Teacher> optionalTeacher = teacherRepository.findById(subjectDTO.getTeacher().getId());
            if (optionalTeacher.isPresent()) {
                Teacher teacher = optionalTeacher.get();
                Subject subject = new Subject(subjectDTO, teacher);
                subjectRepository.save(subject);
                teacher.addSubject(subject);
                teacherRepository.save(teacher);
                Optional<Subject> optionalSubject = subjectRepository.findById(subject.getId());
                return new SubjectDTO(optionalSubject.get());
            }
            throw new NotFoundException("Teacher with id: " + subjectDTO.getTeacher().getId() + " not found");
        }
        throw new NotLoggedInException("Teacher not logged in");
    }

    @PostMapping("/lecture/{subjectId}")
    public SubjectDTO newLecture(@PathVariable UUID subjectId, @RequestBody String date, @RequestHeader("Session-Token") String token) throws ParseException, JedisClientException, NotFoundException, NotLoggedInException {
        UUID teacherId = sessionManagement.getSession(Session.TEACHER, token);

        if(teacherId != null) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
            if (optionalSubject.isPresent()) {
                Subject subject = optionalSubject.get();
                subject.addLecture(Utils.formatter.parse(date));
                subjectRepository.save(subject);
                optionalSubject = subjectRepository.findById(subject.getId());
                return new SubjectDTO(optionalSubject.get());
            }
            throw new NotFoundException("Subject with id: " + subjectId + " not found");
        }
        throw new NotLoggedInException("Teacher not logged in");
    }
}

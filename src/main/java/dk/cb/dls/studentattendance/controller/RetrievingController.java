package dk.cb.dls.studentattendance.controller;

import dk.cb.dls.studentattendance.DTO.*;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
import dk.cb.dls.studentattendance.redis.Session;
import dk.cb.dls.studentattendance.redis.SessionManagement;
import dk.cb.dls.studentattendance.repository.LectureRepository;
import dk.cb.dls.studentattendance.repository.StudentRepository;
import dk.cb.dls.studentattendance.repository.SubjectRepository;
import dk.cb.dls.studentattendance.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/get")
public class RetrievingController {

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


    @GetMapping("/student/{studentId}")
    public StudentDTO getStudent(@PathVariable UUID studentId, @RequestHeader("Session-Token") String token) {
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);
        UUID studentID = sessionManagement.getSession(Session.STUDENT, token);

        if (studentID != null) {
            studentId = studentID;
        }
        if (studentID != null || teacherID != null) {
            Optional<Student> student = studentRepository.findById(studentId);
            if(student.isPresent()) {
                return new StudentDTO(student.get());
            }
        }
        return null;
    }

    @GetMapping("/students")
    public List<StudentDTO> getStudents(@RequestHeader("Session-Token") String token) {
        List<StudentDTO> studentDTOs = new ArrayList<>();
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherID != null) {
            List<Student> students = studentRepository.findAll();

            for (Student student : students) {
                studentDTOs.add(new StudentDTO(student));
            }
        }
        return studentDTOs;
    }

    @GetMapping("/teacher/{teacherId}")
    public TeacherDTO getTeacher(@PathVariable UUID teacherId, @RequestHeader("Session-Token") String token) {
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherID != null) {
            Optional<Teacher> teacher = teacherRepository.findById(teacherId);
            if(teacher.isPresent()) {
                return new TeacherDTO(teacher.get());
            }
        }
        return null;
    }

    @GetMapping("/teachers")
    public List<TeacherDTO> getTeachers(@RequestHeader("Session-Token") String token) {
        List<TeacherDTO> teacherDTOS = new ArrayList<>();
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherID != null) {
            List<Teacher> teachers = teacherRepository.findAll();

            for (Teacher teacher : teachers) {
                teacherDTOS.add(new TeacherDTO(teacher));
            }
        }
        return teacherDTOS;
    }

    @GetMapping("/{studentId}/subjects")
    public List<StudentSubjectDTO> getStudentSubjects(@PathVariable UUID studentId, @RequestHeader("Session-Token") String token) {
        List<StudentSubjectDTO> subjectDTOS = new ArrayList<>();
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);
        UUID studentID = sessionManagement.getSession(Session.STUDENT, token);

        if(studentID != null) {
            studentId = studentID;
        }

        if (studentID != null || teacherID != null) {
            Optional<Student> student = studentRepository.findById(studentId);
            if(student.isPresent()) {
                for (Subject subject : student.get().getSubjects()) {
                    subjectDTOS.add(new StudentSubjectDTO(subject));
                }
            }
        }
        return subjectDTOS;
    }

    @GetMapping("/subject/{subjectId}")
    public SubjectDTO getSubject(@PathVariable UUID subjectId, @RequestHeader("Session-Token") String token) {
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherID != null) {
            Optional<Subject> subject = subjectRepository.findById(subjectId);
            if(subject.isPresent()) {
                return new SubjectDTO(subject.get());
            }
        }
        return null;
    }

    @GetMapping("/subjects")
    public List<SubjectDTO> getSubjects(@RequestHeader("Session-Token") String token) {
        List<SubjectDTO> subjectDTOS = new ArrayList<>();
        UUID teacherID = sessionManagement.getSession(Session.TEACHER, token);

        if (teacherID != null) {
            List<Subject> subjects = subjectRepository.findAll();

            for (Subject subject : subjects) {
                subjectDTOS.add(new SubjectDTO(subject));
            }
        }
        return subjectDTOS;
    }
}

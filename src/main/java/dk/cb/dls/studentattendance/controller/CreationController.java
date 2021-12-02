package dk.cb.dls.studentattendance.controller;

import dk.cb.dls.studentattendance.DTO.StudentDTO;
import dk.cb.dls.studentattendance.DTO.SubjectDTO;
import dk.cb.dls.studentattendance.DTO.TeacherDTO;
import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import dk.cb.dls.studentattendance.models.Teacher;
import dk.cb.dls.studentattendance.repository.LectureRepository;
import dk.cb.dls.studentattendance.repository.StudentRepository;
import dk.cb.dls.studentattendance.repository.SubjectRepository;
import dk.cb.dls.studentattendance.repository.TeacherRepository;
import dk.cb.dls.studentattendance.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    LectureRepository lectureRepository;

    @PostMapping("/student")
    public StudentDTO newStudent(@RequestBody StudentDTO studentDTO) {
        Student student = new Student(studentDTO);
        studentRepository.save(student);
        Optional<Student> optionalStudent = studentRepository.findById(student.getId());
        return new StudentDTO(optionalStudent.get());
    }

    @PostMapping("/teacher")
    public TeacherDTO newTeacher(@RequestBody TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher(teacherDTO);
        teacherRepository.save(teacher);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacher.getId());
        return new TeacherDTO(optionalTeacher.get());
    }

    @PostMapping("/subject")
    public SubjectDTO newSubject(@RequestBody SubjectDTO subjectDTO) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(subjectDTO.getTeacher().getId());
        if(optionalTeacher.isPresent())
        {
            Teacher teacher = optionalTeacher.get();
            Subject subject = new Subject(subjectDTO, teacher);
            subjectRepository.save(subject);
            teacher.addSubject(subject);
            teacherRepository.save(teacher);
            Optional<Subject> optionalSubject = subjectRepository.findById(subject.getId());
            return new SubjectDTO(optionalSubject.get());
        }
        return null;
    }

    @PostMapping("/lecture/{subjectId}")
    public SubjectDTO newLecture(@PathVariable UUID subjectId, @RequestBody String date) throws ParseException {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        if(optionalSubject.isPresent())
        {
            Subject subject = optionalSubject.get();
            subject.addLecture(Utils.formatter.parse(date));
            subjectRepository.save(subject);
            optionalSubject = subjectRepository.findById(subject.getId());
            return new SubjectDTO(optionalSubject.get());
        }
        return null;
    }
}

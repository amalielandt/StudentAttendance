package dk.cb.dls.studentattendance.repository;

import dk.cb.dls.studentattendance.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    Student findOneByEmail (@Param("email") String email);
}

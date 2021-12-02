package dk.cb.dls.studentattendance.repository;

import dk.cb.dls.studentattendance.models.Student;
import dk.cb.dls.studentattendance.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

}

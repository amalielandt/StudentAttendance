package dk.cb.dls.studentattendance.repository;

import dk.cb.dls.studentattendance.models.Lecture;
import dk.cb.dls.studentattendance.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, UUID> {

}

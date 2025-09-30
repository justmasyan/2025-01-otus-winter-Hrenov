package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.Homework;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    @EntityGraph(attributePaths = {
            "status", "student.account", "lector.account",
            "lecture.lectureTopic", "lecture.status"
    })
    Optional<Homework> findById(long id);

    @EntityGraph(attributePaths = {"status"})
    Optional<Homework> findByLectureIdAndStudentAccountId(long lectureId, long accountId);

    List<Homework> findAllByLectureIdAndLectorId(long lectureId, long lectorId);

    @EntityGraph(attributePaths = {
            "status", "student.account",
            "lecture.lectureTopic", "lecture.status"
    })
    List<Homework> findAllByLectorAccountId(long accountId);

    @Query("SELECT h from Homework h " +
            "JOIN h.student hs " +
            "JOIN h.lecture hl " +
            "JOIN hl.studyGroup hlsd " +
            "where hs.id = :studentId AND hlsd.id = :studyGroupId")
    List<Homework> findAllByStudentAndStudyGroup(long studentId, long studyGroupId);

    @Query("SELECT h FROM Homework h " +
            "JOIN h.lector hl " +
            "JOIN h.lecture hle " +
            "JOIN hle.studyGroup hles " +
            "JOIN hles.course lsc " +
            "WHERE hl.id = :lectorId AND hles.id = :courseId")
    List<Homework> findAllByLectorAndCourse(long lectorId, long courseId);
}

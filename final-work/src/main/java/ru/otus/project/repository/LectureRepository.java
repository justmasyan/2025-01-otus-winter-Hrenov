package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.Lecture;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @EntityGraph(attributePaths = {"status", "lectureTopic", "lector.account", "studyGroup.course"})
    Optional<Lecture> findById(long id);

    @EntityGraph(attributePaths = {"status", "lectureTopic"})
    List<Lecture> findAllByStudyGroupId(long groupId);

    @EntityGraph(attributePaths = {"status", "lectureTopic", "studyGroup"})
    List<Lecture> findAllByLectorAccountId(long lectorId);

    @Query("SELECT l FROM Lecture l " +
            "JOIN l.lector le " +
            "JOIN le.account lea " +
            "JOIN l.studyGroup ls " +
            "JOIN ls.course lsc " +
            "WHERE lea.id = :accountId AND lsc.id = :courseId")
    List<Lecture> findAllByLectorAccountIdAndCourseId(long accountId, long courseId);
}

package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.Lecture;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @EntityGraph(attributePaths = {"status", "lectureTopic", "lector.account", "studyGroup"})
    Optional<Lecture> findById(long id);

    @EntityGraph(attributePaths = {"status", "lectureTopic"})
    List<Lecture> findAllByStudyGroupId(long groupId);

    @EntityGraph(attributePaths = {"status", "lectureTopic", "studyGroup"})
    List<Lecture> findAllByLectorAccountId(long lectorId);

    @EntityGraph(attributePaths = {"status", "lectureTopic"})
    List<Lecture> findAllByLectorAccountIdAndStudyGroupCourseId(long lectorId, long courseId);
}

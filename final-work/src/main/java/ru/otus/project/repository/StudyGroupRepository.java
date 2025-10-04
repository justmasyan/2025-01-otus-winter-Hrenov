package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.StudyGroup;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup,Long>{

    @EntityGraph(attributePaths = {"course"})
    Optional<StudyGroup> findById(long id);

    List<StudyGroup> findAllByStudentsAccountId(long studentId);

    List<StudyGroup> findAllByCourseId(long courseId);
}

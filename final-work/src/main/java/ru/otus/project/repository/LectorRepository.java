package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {

    @Query("SELECT l FROM Lector l JOIN FETCH l.account WHERE l.account.id = :accountId")
    Optional<Lector> findByAccountId(long accountId);

    @EntityGraph(attributePaths = {"account"})
    List<Lector> findAllByCoursesId(long courseId);
}

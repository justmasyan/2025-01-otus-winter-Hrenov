package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.LectureTopic;

import java.util.Optional;

@Repository
public interface LectureTopicRepository extends JpaRepository<LectureTopic, Long> {

    Optional<LectureTopic> findByTitle(String title);
}

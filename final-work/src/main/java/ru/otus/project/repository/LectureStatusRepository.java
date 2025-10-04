package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.LectureStatus;

@Repository
public interface LectureStatusRepository extends JpaRepository<LectureStatus, Long> {

}

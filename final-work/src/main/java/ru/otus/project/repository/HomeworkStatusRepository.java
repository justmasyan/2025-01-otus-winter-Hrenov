package ru.otus.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.HomeworkStatus;

@Repository
public interface HomeworkStatusRepository extends JpaRepository<HomeworkStatus,Long> {
}

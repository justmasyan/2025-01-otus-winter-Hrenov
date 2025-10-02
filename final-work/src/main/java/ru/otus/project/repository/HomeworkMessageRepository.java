package ru.otus.project.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.project.domain.HomeworkMessage;

import java.util.List;

@Repository
public interface HomeworkMessageRepository extends JpaRepository<HomeworkMessage, Long> {

    @EntityGraph(attributePaths = {"sender"})
    List<HomeworkMessage> findAllByHomeworkIdOrderBySendTimeDesc(long homeworkId);
}

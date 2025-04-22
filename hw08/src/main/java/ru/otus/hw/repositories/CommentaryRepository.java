package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Commentary;

import java.util.List;

public interface CommentaryRepository extends MongoRepository<Commentary, Long> {

    @Query("{ 'book.$id': :#{#bookId} }")
    List<Commentary> findAllByBookId(@Param("bookId") Long bookId);
}

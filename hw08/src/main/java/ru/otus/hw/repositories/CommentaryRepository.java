package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Commentary;

import java.util.List;

public interface CommentaryRepository extends MongoRepository<Commentary, String> {

    @Query("{ 'book.$id': :#{#bookId} }")
    List<Commentary> findAllByBookId(@Param("bookId") String bookId);

    @DeleteQuery("{ 'book.$id': :#{#bookId} }")
    void deleteAllByBookId(@Param("bookId") String bookId);
}

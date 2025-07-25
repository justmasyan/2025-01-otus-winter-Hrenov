package ru.otus.hw.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain_jpa.AuthorJpa;
import ru.otus.hw.domain_jpa.BookJpa;
import ru.otus.hw.domain_jpa.CommentaryJpa;
import ru.otus.hw.domain_jpa.GenreJpa;
import ru.otus.hw.domain_mongo.AuthorMongo;
import ru.otus.hw.domain_mongo.BookMongo;
import ru.otus.hw.domain_mongo.CommentaryMongo;
import ru.otus.hw.domain_mongo.GenreMongo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportServiceImpl implements ImportService {

    private final Map<Class<?>, Map<ObjectId, Long>> mapDbId = Map.of(
            BookMongo.class, new HashMap<>(),
            AuthorMongo.class, new HashMap<>(),
            GenreMongo.class, new HashMap<>(),
            CommentaryMongo.class, new HashMap<>()
    );

    @Override
    public BookJpa convertBookfromMongoToJpa(BookMongo bookMongo) {
        AuthorJpa authorJpa = convertAuthorFromMongoToJpa(bookMongo.getAuthor());
        List<GenreJpa> genresJpa = bookMongo.getGenres().stream()
                .map(this::convertGenreFromMongoToJpa)
                .toList();
        return new BookJpa(getJpaId(bookMongo.getClass(), bookMongo.getId()),
                bookMongo.getTitle(), authorJpa, genresJpa);
    }

    @Override
    public AuthorJpa convertAuthorFromMongoToJpa(AuthorMongo authorMongo) {
        return new AuthorJpa(getJpaId(authorMongo.getClass(), authorMongo.getId()), authorMongo.getFullName());
    }

    @Override
    public GenreJpa convertGenreFromMongoToJpa(GenreMongo genreMongo) {
        return new GenreJpa(getJpaId(genreMongo.getClass(), genreMongo.getId()), genreMongo.getName());
    }

    @Override
    public CommentaryJpa convertCommentFromMongoToJpa(CommentaryMongo commentaryMongo) {
        BookJpa bookJpa = convertBookfromMongoToJpa(commentaryMongo.getBook());
        return new CommentaryJpa(getJpaId(commentaryMongo.getClass(), commentaryMongo.getId()),
                bookJpa, commentaryMongo.getText());
    }

    private Long getJpaId(Class<?> clazz, ObjectId mongoId) {
        Map<ObjectId, Long> map = mapDbId.get(clazz);

        if (map.isEmpty()) {
            map.put(mongoId, 1L);
            return 1L;
        }

        Long jpaId = map.getOrDefault(mongoId, (long) map.size() + 1);
        if (jpaId == map.size() + 1) {
            map.put(mongoId, jpaId);
        }

        return jpaId;
    }

}

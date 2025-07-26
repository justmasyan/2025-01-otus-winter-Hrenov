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
public class ConvertServiceImpl implements ConvertService {

    private final Map<Class<?>, Map<ObjectId, Long>> mapDbId = Map.of(
            BookMongo.class, new HashMap<>(),
            AuthorMongo.class, new HashMap<>(),
            GenreMongo.class, new HashMap<>(),
            CommentaryMongo.class, new HashMap<>()
    );

    @Override
    public BookJpa convertBook(BookMongo bookMongo) {
        AuthorJpa authorJpa = convertAuthor(bookMongo.getAuthor());
        List<GenreJpa> genresJpa = bookMongo.getGenres().stream()
                .map(this::convertGenre)
                .toList();
        return new BookJpa(getJpaId(bookMongo.getClass(), bookMongo.getId()),
                bookMongo.getTitle(), authorJpa, genresJpa);
    }

    @Override
    public AuthorJpa convertAuthor(AuthorMongo authorMongo) {
        return new AuthorJpa(getJpaId(authorMongo.getClass(), authorMongo.getId()), authorMongo.getFullName());
    }

    @Override
    public GenreJpa convertGenre(GenreMongo genreMongo) {
        return new GenreJpa(getJpaId(genreMongo.getClass(), genreMongo.getId()), genreMongo.getName());
    }

    @Override
    public CommentaryJpa convertComment(CommentaryMongo commentaryMongo) {
        BookJpa bookJpa = convertBook(commentaryMongo.getBook());
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

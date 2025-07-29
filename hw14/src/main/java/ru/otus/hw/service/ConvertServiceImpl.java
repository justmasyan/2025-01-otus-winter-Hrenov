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

    private final Map<ObjectId, AuthorJpa> authors = new HashMap<>();

    private final Map<ObjectId, GenreJpa> genres = new HashMap<>();

    private final Map<ObjectId, BookJpa> books = new HashMap<>();

    @Override
    public BookJpa convertBook(BookMongo bookMongo) {
        AuthorJpa authorJpa = authors.get(bookMongo.getAuthor().getId());
        List<GenreJpa> genresJpa = bookMongo.getGenres().stream()
                .map(genreMongo -> genres.get(genreMongo.getId()))
                .toList();
        BookJpa bookJpa = new BookJpa(0, bookMongo.getTitle(), authorJpa, genresJpa);
        books.put(bookMongo.getId(), bookJpa);
        return bookJpa;
    }

    @Override
    public AuthorJpa convertAuthor(AuthorMongo authorMongo) {
        AuthorJpa authorJpa = new AuthorJpa(0, authorMongo.getFullName(), authorMongo.getId());
        authors.put(authorMongo.getId(), authorJpa);
        return authorJpa;
    }

    @Override
    public GenreJpa convertGenre(GenreMongo genreMongo) {
        GenreJpa genreJpa = new GenreJpa(0, genreMongo.getName(), genreMongo.getId());
        genres.put(genreMongo.getId(), genreJpa);
        return genreJpa;
    }

    @Override
    public CommentaryJpa convertComment(CommentaryMongo commentMongo) {
        BookJpa bookJpa = books.get(commentMongo.getBook().getId());
        return new CommentaryJpa(0, bookJpa, commentMongo.getText(), commentMongo.getId());
    }
}

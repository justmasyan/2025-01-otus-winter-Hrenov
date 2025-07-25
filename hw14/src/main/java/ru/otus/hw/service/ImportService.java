package ru.otus.hw.service;

import ru.otus.hw.domain_jpa.AuthorJpa;
import ru.otus.hw.domain_jpa.BookJpa;
import ru.otus.hw.domain_jpa.CommentaryJpa;
import ru.otus.hw.domain_jpa.GenreJpa;
import ru.otus.hw.domain_mongo.AuthorMongo;
import ru.otus.hw.domain_mongo.BookMongo;
import ru.otus.hw.domain_mongo.CommentaryMongo;
import ru.otus.hw.domain_mongo.GenreMongo;

public interface ImportService {

    BookJpa convertBookfromMongoToJpa(BookMongo bookMongo);

    AuthorJpa convertAuthorFromMongoToJpa(AuthorMongo authorMongo);

    GenreJpa convertGenreFromMongoToJpa(GenreMongo genreMongo);

    CommentaryJpa convertCommentFromMongoToJpa(CommentaryMongo commentaryMongo);
}

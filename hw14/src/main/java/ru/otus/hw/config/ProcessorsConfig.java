package ru.otus.hw.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.domain_jpa.AuthorJpa;
import ru.otus.hw.domain_jpa.BookJpa;
import ru.otus.hw.domain_jpa.CommentaryJpa;
import ru.otus.hw.domain_jpa.GenreJpa;
import ru.otus.hw.domain_mongo.AuthorMongo;
import ru.otus.hw.domain_mongo.BookMongo;
import ru.otus.hw.domain_mongo.CommentaryMongo;
import ru.otus.hw.domain_mongo.GenreMongo;
import ru.otus.hw.service.ConvertService;

@Configuration
public class ProcessorsConfig {

    @Bean
    public ItemProcessor<BookMongo, BookJpa> bookProcessor(ConvertService importService) {
        return importService::convertBook;
    }

    @Bean
    public ItemProcessor<AuthorMongo, AuthorJpa> authorProcessor(ConvertService importService) {
        return importService::convertAuthor;
    }

    @Bean
    public ItemProcessor<GenreMongo, GenreJpa> genreProcessor(ConvertService importService) {
        return importService::convertGenre;
    }

    @Bean
    public ItemProcessor<CommentaryMongo, CommentaryJpa> commentProcessor(ConvertService importService) {
        return importService::convertComment;
    }
}

package ru.otus.hw.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
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
import ru.otus.hw.service.ImportServiceImpl;

@Configuration
public class ProcessorsConfig {

    @Bean
    @StepScope
    public ItemProcessor<BookMongo, BookJpa> bookProcessor(ImportServiceImpl importService) {
        return importService::convertBookfromMongoToJpa;
    }

    @Bean
    @StepScope
    public ItemProcessor<AuthorMongo, AuthorJpa> authorProcessor(ImportServiceImpl importService) {
        return importService::convertAuthorFromMongoToJpa;
    }

    @Bean
    @StepScope
    public ItemProcessor<GenreMongo, GenreJpa> genreProcessor(ImportServiceImpl importService) {
        return importService::convertGenreFromMongoToJpa;
    }

    @Bean
    @StepScope
    public ItemProcessor<CommentaryMongo, CommentaryJpa> commentProcessor(ImportServiceImpl importService) {
        return importService::convertCommentFromMongoToJpa;
    }
}

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
import ru.otus.hw.service.ConvertServiceImpl;

@Configuration
public class ProcessorsConfig {

    @Bean
    @StepScope
    public ItemProcessor<BookMongo, BookJpa> bookProcessor(ConvertServiceImpl importService) {
        return importService::convertBook;
    }

    @Bean
    @StepScope
    public ItemProcessor<AuthorMongo, AuthorJpa> authorProcessor(ConvertServiceImpl importService) {
        return importService::convertAuthor;
    }

    @Bean
    @StepScope
    public ItemProcessor<GenreMongo, GenreJpa> genreProcessor(ConvertServiceImpl importService) {
        return importService::convertGenre;
    }

    @Bean
    @StepScope
    public ItemProcessor<CommentaryMongo, CommentaryJpa> commentProcessor(ConvertServiceImpl importService) {
        return importService::convertComment;
    }
}

package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoCursorItemReader;
import org.springframework.batch.item.data.builder.MongoCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.domain_mongo.AuthorMongo;
import ru.otus.hw.domain_mongo.BookMongo;
import ru.otus.hw.domain_mongo.CommentaryMongo;
import ru.otus.hw.domain_mongo.GenreMongo;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class ReadersConfig {

    @Autowired
    private final MongoOperations mongoOperations;

    @Bean
    @StepScope
    public MongoCursorItemReader<BookMongo> bookReader() {
        return getMongoCursorItemReader(BookMongo.class, "bookReader", "books");
    }

    @Bean
    @StepScope
    public MongoCursorItemReader<AuthorMongo> authorReader() {
        return getMongoCursorItemReader(AuthorMongo.class, "authorReader", "authors");
    }

    @Bean
    @StepScope
    public MongoCursorItemReader<GenreMongo> genreReader() {
        return getMongoCursorItemReader(GenreMongo.class, "genreReader", "genres");
    }

    @Bean
    @StepScope
    public MongoCursorItemReader<CommentaryMongo> commentReader() {
        return getMongoCursorItemReader(CommentaryMongo.class, "commentReader", "commentaries");
    }

    public <T> MongoCursorItemReader<T> getMongoCursorItemReader(Class<T> clazz,
                                                                 String name, String collection) {
        return new MongoCursorItemReaderBuilder<T>()
                .name(name)
                .template(mongoOperations)
                .collection(collection)
                .jsonQuery("{}")
                .targetType(clazz)
                .sorts(new HashMap<>())
                .build();
    }
}

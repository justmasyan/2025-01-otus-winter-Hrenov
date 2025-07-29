package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.domain_jpa.AuthorJpa;
import ru.otus.hw.domain_jpa.BookJpa;
import ru.otus.hw.domain_jpa.CommentaryJpa;
import ru.otus.hw.domain_jpa.GenreJpa;

@Configuration
@RequiredArgsConstructor
public class WritersConfig {

    @Autowired
    private final EntityManagerFactory emFactory;

    @Bean
    public JpaItemWriter<BookJpa> bookWriter() {
        return getJpaItemWriter(BookJpa.class);
    }

    @Bean
    public JpaItemWriter<AuthorJpa> authorWriter() {
        return getJpaItemWriter(AuthorJpa.class);
    }

    @Bean
    public JpaItemWriter<GenreJpa> genreWriter() {
        return getJpaItemWriter(GenreJpa.class);
    }

    @Bean
    public JpaItemWriter<CommentaryJpa> commentWriter() {
        return getJpaItemWriter(CommentaryJpa.class);
    }

    public <T> JpaItemWriter<T> getJpaItemWriter(Class<T> clazz) {
        return new JpaItemWriterBuilder<T>()
                .entityManagerFactory(emFactory)
                .usePersist(true)
                .build();
    }
}

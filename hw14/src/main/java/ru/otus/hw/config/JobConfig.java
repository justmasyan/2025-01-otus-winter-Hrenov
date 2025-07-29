package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.domain_jpa.AuthorJpa;
import ru.otus.hw.domain_jpa.BookJpa;
import ru.otus.hw.domain_jpa.CommentaryJpa;
import ru.otus.hw.domain_jpa.GenreJpa;
import ru.otus.hw.domain_mongo.AuthorMongo;
import ru.otus.hw.domain_mongo.BookMongo;
import ru.otus.hw.domain_mongo.CommentaryMongo;
import ru.otus.hw.domain_mongo.GenreMongo;
import ru.otus.hw.service.CleanUpService;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final String IMPORT_JOB_NAME = "importJob";

    private static final int CHUNK_SIZE = 3;

    @Autowired
    private CleanUpService cleanUpService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Job importJob(Step cleanUpStep,
                         Step importAuthorsStep,
                         Step importGenresStep,
                         Step importBooksStep,
                         Step importCommentsStep) {
        return new JobBuilder(IMPORT_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(cleanUpStep)
                .next(importAuthorsStep)
                .next(importGenresStep)
                .next(importBooksStep)
                .next(importCommentsStep)
                .end()
                .build();
    }

    @Bean
    public Step cleanUpStep() {
        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(cleanUpTasklet(), transactionManager)
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Step importAuthorsStep(ItemReader<AuthorMongo> authorReader,
                                  ItemProcessor<AuthorMongo, AuthorJpa> authorProcessor,
                                  ItemWriter<AuthorJpa> authorWriter) {
        return getImportStep("importAuthorsStep", authorReader, authorProcessor, authorWriter);
    }

    @Bean
    public Step importGenresStep(ItemReader<GenreMongo> genreReader,
                                 ItemProcessor<GenreMongo, GenreJpa> genreProcessor,
                                 ItemWriter<GenreJpa> genreWriter) {
        return getImportStep("importGenresStep", genreReader, genreProcessor, genreWriter);
    }

    @Bean
    public Step importBooksStep(ItemReader<BookMongo> bookReader,
                                ItemProcessor<BookMongo, BookJpa> bookProcessor,
                                ItemWriter<BookJpa> bookWriter) {
        return getImportStep("importBooksStep", bookReader, bookProcessor, bookWriter);
    }

    @Bean
    public Step importCommentsStep(ItemReader<CommentaryMongo> commentReader,
                                   ItemProcessor<CommentaryMongo, CommentaryJpa> commentProcessor,
                                   ItemWriter<CommentaryJpa> commentWriter) {
        return getImportStep("importCommentsStep", commentReader, commentProcessor, commentWriter);
    }

    private <T, V> Step getImportStep(String nameStep, ItemReader<T> reader,
                                      ItemProcessor<T, V> processor,
                                      ItemWriter<V> writer) {
        return new StepBuilder(nameStep, jobRepository)
                .<T, V>chunk(CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}

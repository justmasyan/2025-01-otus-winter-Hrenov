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
import ru.otus.hw.service.CleanUpServiceImpl;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final String IMPORT_JOB_NAME = "importJob";

    private static final int CHUNK_SIZE = 3;

    @Autowired
    private CleanUpServiceImpl cleanUpService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public Job importJob(Step cleanUpStep,
                         Step convertAuthorsStep,
                         Step convertGenresStep,
                         Step convertBooksStep,
                         Step convertCommentsStep) {
        return new JobBuilder(IMPORT_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(cleanUpStep)
                .next(convertAuthorsStep)
                .next(convertGenresStep)
                .next(convertBooksStep)
                .next(convertCommentsStep)
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
    public Step convertAuthorsStep(ItemReader<AuthorMongo> authorReader,
                                   ItemProcessor<AuthorMongo, AuthorJpa> authorProcessor,
                                   ItemWriter<AuthorJpa> authorWriter) {
        return getImportStep(authorReader, authorProcessor, authorWriter);
    }

    @Bean
    public Step convertGenresStep(ItemReader<GenreMongo> genreReader,
                                  ItemProcessor<GenreMongo, GenreJpa> genreProcessor,
                                  ItemWriter<GenreJpa> genreWriter) {
        return getImportStep(genreReader, genreProcessor, genreWriter);
    }

    @Bean
    public Step convertBooksStep(ItemReader<BookMongo> bookReader,
                                 ItemProcessor<BookMongo, BookJpa> bookProcessor,
                                 ItemWriter<BookJpa> bookWriter) {
        return getImportStep(bookReader, bookProcessor, bookWriter);
    }

    @Bean
    public Step convertCommentsStep(ItemReader<CommentaryMongo> commentReader,
                                    ItemProcessor<CommentaryMongo, CommentaryJpa> commentProcessor,
                                    ItemWriter<CommentaryJpa> commentWriter) {
        return getImportStep(commentReader, commentProcessor, commentWriter);
    }

    private <T, V> Step getImportStep(ItemReader<T> reader,
                                      ItemProcessor<T, V> processor,
                                      ItemWriter<V> writer) {
        return new StepBuilder("importCommentStep", jobRepository)
                .<T, V>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}

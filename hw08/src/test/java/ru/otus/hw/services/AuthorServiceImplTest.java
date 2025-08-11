package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.TestDataProvider.getDbAuthors;

@DataMongoTest()
@Import({AuthorServiceImpl.class,BookServiceImpl.class,
        GenreServiceImpl.class, CommentaryServiceImpl.class, CommentaryConverter.class,
        BookConverter.class, AuthorConverter.class, GenreConverter.class})
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @Test
    void findAll() {
        List<AuthorDto> expectedAuthors = getDbAuthors();
        assertThat(expectedAuthors).isEqualTo(authorService.findAll());
    }
}

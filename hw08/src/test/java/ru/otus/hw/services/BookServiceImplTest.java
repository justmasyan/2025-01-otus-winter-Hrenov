package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.TestDataProvider.getDbAuthors;
import static ru.otus.hw.TestDataProvider.getDbBooks;
import static ru.otus.hw.TestDataProvider.getDbGenres;

@DataMongoTest
@Import({BookServiceImpl.class, BookConverter.class,
        AuthorConverter.class, GenreConverter.class})
class BookServiceImplTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private BookService bookService;

    @ParameterizedTest
    @MethodSource("ru.otus.hw.TestDataProvider#getDbBooks")
    void findById(BookDto expectedBook) {
        assertThat(bookService.findById(expectedBook.getId()))
                .isPresent().get().isEqualTo(expectedBook);

    }

    @Test
    void findAll() {
        assertThat(bookService.findAll())
                .isEqualTo(getDbBooks());
    }

    @Test
    @DirtiesContext
    void insert() {
        BookDto expectedDto = new BookDto("0", "NewTitle",
                getDbAuthors().get(0),
                getDbGenres().subList(0, 3));

        BookDto actualDto = bookService.insert(expectedDto.getTitle(), expectedDto.getAuthor().getId(),
                expectedDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );

        expectedDto.setId(actualDto.getId());
        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DirtiesContext
    void update() {
        BookDto expectedDto = new BookDto("3", "NewTitle", getDbAuthors().get(2),
                getDbGenres().subList(4, 6));

        BookDto actualDto = bookService.update(expectedDto.getId(), expectedDto.getTitle(),
                expectedDto.getAuthor().getId(),
                expectedDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );

        assertThat(actualDto).isEqualTo(expectedDto);
    }

    @Test
    @DirtiesContext
    void deleteById() {
        BookDto bookDto = getDbBooks().get(0);
        bookService.deleteById(bookDto.getId());

        Query searchQuery = new Query(Criteria.where("id").is(bookDto.getId()));
        assertThat(mongoOperations.findOne(searchQuery, BookDto.class)).isNull();
    }

}
package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookServiceImpl.class, JpaAuthorRepository.class,
        JpaGenreRepository.class, JpaBookRepository.class,
        BookConverter.class, AuthorConverter.class,
        GenreConverter.class, CommentaryConverter.class
})
@Transactional(propagation = Propagation.NEVER)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("Результат Метода findById не должен содержать Lazy полей")
    @ParameterizedTest
    @MethodSource("getDbBooksDto")
    void resultShouldNoHaveLazyFieldsFindById(BookDto expectedDto) {
        Optional<Integer> actualHashCode = bookService.findById(expectedDto.getId())
                .map(BookDto::hashCode);

        assertThat(actualHashCode).isPresent()
                .get().isEqualTo(expectedDto.hashCode());

    }

    @DisplayName("Результат Метода findAll не должен содержать Lazy полей")
    @Test
    void resultShouldNoHaveLazyFieldsFindAll() {
        List<Integer> expectedHashCodes = getDbBooksDto().stream()
                .map(BookDto::hashCode).toList();

        List<Integer> actualHashCodes = bookService.findAll().stream()
                .map(BookDto::hashCode).toList();

        assertThat(actualHashCodes).isEqualTo(expectedHashCodes);
    }

    @DisplayName("Результат Метода insert не должен содержать Lazy полей")
    @Test
    @Transactional
    void resultShouldNoHaveLazyFieldsInsert() {
        BookDto expectedDto = new BookDto(4L, "NewTitle",
                getDbAuthors().get(0),
                getDbGenres().subList(0, 3));

        BookDto actualDto = bookService.insert(expectedDto.getTitle(), expectedDto.getAuthor().getId(),
                expectedDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );

        assertThat(actualDto.hashCode()).isEqualTo(expectedDto.hashCode());
    }

    @DisplayName("Результат Метода update не должен содержать Lazy полей")
    @Test
    @Transactional
    void resultShouldNoHaveLazyFieldsUpdate() {
        BookDto expectedDto = new BookDto(3L, "NewTitle", getDbAuthors().get(2),
                getDbGenres().subList(4, 6));

        BookDto actualDto = bookService.update(expectedDto.getId(), expectedDto.getTitle(),
                expectedDto.getAuthor().getId(),
                expectedDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet())
        );

        assertThat(actualDto.hashCode()).isEqualTo(expectedDto.hashCode());
    }

    private static List<BookDto> getDbBooksDto() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooksDto(dbAuthors, dbGenres);
    }

    private static List<BookDto> getDbBooksDto(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }
}
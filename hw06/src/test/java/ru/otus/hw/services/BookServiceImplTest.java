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
import ru.otus.hw.entities.AuthorDto;
import ru.otus.hw.entities.BookDto;
import ru.otus.hw.entities.CommentaryDto;
import ru.otus.hw.entities.GenreDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({BookServiceImpl.class, JpaAuthorRepository.class,
        JpaGenreRepository.class, JpaBookRepository.class,
        BookConverter.class, AuthorConverter.class,
        GenreConverter.class, CommentaryConverter.class
})
@Transactional(propagation = Propagation.NESTED)
class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private TestEntityManager em;

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
        long bookId = 1L;
        List<Integer> expectedHashCodes = getDbBooksDto().stream()
                .map(BookDto::hashCode).toList();

        List<Integer> actualHashCodes = bookService.findAll().stream()
                .map(BookDto::hashCode).toList();

        assertThat(actualHashCodes).isEqualTo(expectedHashCodes);
    }

    @DisplayName("Результат Метода insert не должен содержать Lazy полей")
    @Test
    void resultShouldNoHaveLazyFieldsInsert() {
        List<CommentaryDto> commentaries = List.of(
                new CommentaryDto(7L, 4L, "WOW"),
                new CommentaryDto(8L, 4L, "CLASS")
        );

        BookDto expectedDto = new BookDto(4L, "NewTitle", getDbAuthors().get(0),
                getDbGenres().subList(0, 3), commentaries);

        BookDto actualDto = bookService.insert(expectedDto.getTitle(), expectedDto.getAuthor().getId(),
                expectedDto.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()),
                expectedDto.getCommentaries().stream().map(CommentaryDto::getText).toList()
        );

        Book oldBook = em.find(Book.class, actualDto.getId());
        em.detach(oldBook);

        assertThat(actualDto.hashCode()).isEqualTo(expectedDto.hashCode());
    }

    @DisplayName("Результат Метода update не должен содержать Lazy полей")
    @Test
    void resultShouldNoHaveLazyFieldsUpdate() {
        long bookId = 3L;
        String newTitle = "NewTitle";
        List<String> comms = List.of("WOW", "CLASS");

        BookDto actualDto = bookService.update(bookId, newTitle, 3L, Set.of(5L, 6L), comms);

        List<CommentaryDto> expectedComments = new ArrayList<>();
        List<CommentaryDto> actualComments = actualDto.getCommentaries();
        for (int i = 0; i < actualComments.size(); i++) {
            CommentaryDto expectedComment = new CommentaryDto(
                    actualComments.get(i).getId(),
                    bookId,
                    comms.get(i)
            );
            expectedComments.add(expectedComment);
        }

        BookDto expectedDto = new BookDto(bookId, newTitle, getDbAuthors().get(2),
                getDbGenres().subList(4, 6), expectedComments);

        Book oldBook = em.find(Book.class, 3);
        em.detach(oldBook);

        assertThat(actualDto.hashCode()).isEqualTo(expectedDto.hashCode());
    }

    private static List<BookDto> getDbBooksDto() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var dbComments = getDbCommentaries();
        return getDbBooksDto(dbAuthors, dbGenres, dbComments);
    }

    private static List<BookDto> getDbBooksDto(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres, List<CommentaryDto> dbComments) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2),
                        dbComments.subList((id - 1) * 2, (id - 1) * 2 + 2)
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

    private static List<CommentaryDto> getDbCommentaries() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new CommentaryDto(id, (id - 1) / 2 + 1, "Comment_" + id))
                .toList();
    }
}
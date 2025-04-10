package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaCommentaryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CommentaryServiceImpl.class, CommentaryConverter.class,
        JpaCommentaryRepository.class, JpaBookRepository.class,
        BookConverter.class, AuthorConverter.class, GenreConverter.class})
@Transactional(propagation = Propagation.NEVER)
class CommentaryServiceImplTest {

    @Autowired
    private CommentaryServiceImpl commentaryService;

    private List<CommentaryDto> dbCommentaries;

    private List<BookDto> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = getDbBooks();
        dbCommentaries = getDbCommentaries();
    }

    @DisplayName("Результат Метода findById не должен содержать Lazy полей")
    @ParameterizedTest
    @MethodSource("getDbCommentaries")
    void resultShouldNoHaveLazyFieldsFindById(CommentaryDto expectedDto) {
        Optional<Integer> actualHashCode = commentaryService.findById(expectedDto.getId())
                .map(CommentaryDto::hashCode);

        assertThat(actualHashCode).isPresent().get()
                .isEqualTo(expectedDto.hashCode());
    }

    @DisplayName("Результат Метода findAllByBookId не должен содержать Lazy полей")
    @Test
    void resultShouldNoHaveLazyFieldsFindByBookId() {
        long bookId = 1L;
        List<Integer> expectedHashCodes = dbCommentaries.stream()
                .filter(comment -> comment.getBook().getId() == bookId)
                .map(CommentaryDto::hashCode)
                .toList();

        List<Integer> actualHashCodes = commentaryService.findAllByBookId(bookId).stream()
                .map(CommentaryDto::hashCode).toList();

        assertThat(actualHashCodes).isEqualTo(expectedHashCodes);
    }

    @DisplayName("Результат Метода insert не должен содержать Lazy полей")
    @Test
    @Transactional
    void resultShouldNoHaveLazyFieldsInsert() {
        CommentaryDto expectedDto = new CommentaryDto(7, dbBooks.get(0), "WOW");
        CommentaryDto actualDto = commentaryService.insert(expectedDto.getBook().getId(), expectedDto.getText());
        assertThat(actualDto.hashCode()).isEqualTo(expectedDto.hashCode());
    }

    @DisplayName("Результат Метода update не должен содержать Lazy полей")
    @Test
    @Transactional
    void resultShouldNoHaveLazyFieldsUpdate() {
        String newText = "NEW_TEXT";
        CommentaryDto oldCommentary = dbCommentaries.get(2);
        CommentaryDto updatedCommentary = commentaryService.update(oldCommentary.getId(), oldCommentary.getBook().getId(), newText);
        assertThat(updatedCommentary.getText()).isNotEqualTo(oldCommentary.getText()).isEqualTo(newText);

        oldCommentary.setText(newText);
        assertThat(updatedCommentary.hashCode()).isEqualTo(oldCommentary.hashCode());
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<CommentaryDto> getDbCommentaries() {
        var dbBooks = getDbBooks();
        return getDbCommentaries(dbBooks);
    }

    private static List<CommentaryDto> getDbCommentaries(List<BookDto> dbBooks) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new CommentaryDto(id, dbBooks.get((id - 1) / 2), "Comment_" + id))
                .toList();
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
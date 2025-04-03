package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentaryRepository.class})
class JpaCommentaryRepositoryTest {

    @Autowired
    private JpaCommentaryRepository jpaCommentaryRepository;

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbCommentaries")
    void shouldReturnCorrectCommentById(Commentary expectedComment) {
        var actualBook = jpaCommentaryRepository.findById(expectedComment.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарии по id книги")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectCommentsByBookId(Book book) {
        var expectedComments = book.getCommentaries();
        var actualComments = jpaCommentaryRepository.findAllByBookId(book.getId());
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewBook() {
        var expectedComment = new Commentary(0L, 1L, "NEW_COMMENT");

        var returnedComment = jpaCommentaryRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(jpaCommentaryRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedComment = new Commentary(1L, 2, "NEW_COMMENT");

        assertThat(jpaCommentaryRepository.findById(expectedComment.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = jpaCommentaryRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(jpaCommentaryRepository.findById(returnedComment.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(jpaCommentaryRepository.findById(1L)).isPresent();
        jpaCommentaryRepository.deleteById(1L);
        assertThat(jpaCommentaryRepository.findById(1L)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres, List<Commentary> dbComments) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2),
                        dbComments.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var dbComments = getDbCommentaries();
        return getDbBooks(dbAuthors, dbGenres, dbComments);
    }

    private static List<Commentary> getDbCommentaries() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Commentary(id, (id - 1) / 2 + 1, "Comment_" + id))
                .toList();
    }
}
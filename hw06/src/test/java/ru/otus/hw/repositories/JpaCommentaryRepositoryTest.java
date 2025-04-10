package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

    @Autowired
    private TestEntityManager em;

    private List<Commentary> dbCommentaries;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = getDbBooks();
        dbCommentaries = getDbCommentaries();
    }

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
        var expectedComments = dbCommentaries.stream()
                .filter(comment -> comment.getBook().equals(book))
                .toList();
        var actualComments = jpaCommentaryRepository.findAllByBookId(book.getId());
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        Book newBook = dbBooks.get(0);
        var expectedComment = new Commentary(0L, newBook, "NEW_COMMENT");

        var returnedComment = jpaCommentaryRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Commentary.class, returnedComment.getId()))
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        Book newBook = dbBooks.get(2);
        var expectedComment = new Commentary(1L, newBook, "NEW_COMMENT");

        assertThat(em.find(Commentary.class, expectedComment.getId()))
                .isNotEqualTo(expectedComment);

        var returnedComment = jpaCommentaryRepository.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        assertThat(em.find(Commentary.class, expectedComment.getId()))
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        long idCommentForDelete = 1L;
        assertThat(em.find(Commentary.class, idCommentForDelete)).isNotNull();
        jpaCommentaryRepository.deleteById(idCommentForDelete);
        assertThat(em.find(Commentary.class, idCommentForDelete)).isNull();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<Commentary> getDbCommentaries() {
        var dbBooks = getDbBooks();
        return getDbCommentaries(dbBooks);
    }

    private static List<Commentary> getDbCommentaries(List<Book> dbBooks) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Commentary(id, dbBooks.get((id - 1) / 2), "Comment_" + id))
                .toList();
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
}
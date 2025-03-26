package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.BookForUpdateNotFoundException;
import ru.otus.hw.exceptions.BookNothingUpdateException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcBookRepository(GenreRepository genreRepository,
                              NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.genreRepository = genreRepository;
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.jdbcOperations = namedParameterJdbcOperations.getJdbcOperations();
    }

    @Override
    public Optional<Book> findById(long id) {
        String query = """
                SELECT b.id as bookId, b.title, a.id as authorId, a.full_name, g.id as genreId, g.name
                FROM books b
                INNER JOIN authors a ON a.id = b.author_id
                INNER JOIN books_genres bg ON b.id = bg.book_id
                INNER JOIN genres g ON g.id = bg.genre_id
                WHERE b.id = :id
                """;

        Map<String, Long> params = Map.of("id", id);
        return Optional.ofNullable(namedParameterJdbcOperations.query(query, params, new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("DELETE FROM books WHERE id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        String query = "SELECT b.id as bookId, b.title, a.id as authorId, a.full_name FROM books b\n" +
                "INNER JOIN authors a ON a.id = b.author_id";
        return jdbcOperations.query(query, new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcOperations.query("SELECT book_id, genre_id FROM books_genres", new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {

        Map<Long, Book> booksMap = booksWithoutGenres.stream().collect(
                Collectors.toMap(Book::getId, book -> {
                    book.setGenres(new ArrayList<>());
                    return book;
                })
        );

        Map<Long, Genre> genreMap = genres.stream().collect(Collectors.toMap(Genre::getId, genre -> genre));
        relations.forEach(relation ->
                booksMap.get(relation.bookId).getGenres().add(genreMap.get(relation.genreId))
        );
    }

    private Book insert(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("INSERT INTO books (title, author_id) VALUES (:title, :author_id)",
                params, keyHolder, new String[]{"id"});

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        Book oldBook = findById(book.getId()).orElseThrow(
                () -> new BookForUpdateNotFoundException("Not found book with id %d".formatted(book.getId()))
        );
        if (oldBook.equals(book)) {
            throw new BookNothingUpdateException("No changes to update the book with id %d".formatted(book.getId()));
        }

        Map<String, Object> params = Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "id", book.getId()
        );
        namedParameterJdbcOperations.update("UPDATE books SET title = :title, author_id = :author_id WHERE id = :id",
                params);
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        MapSqlParameterSource[] params = book.getGenres().stream()
                .map(genre -> {
                    MapSqlParameterSource param = new MapSqlParameterSource();
                    param.addValue("book_id", book.getId());
                    param.addValue("genre_id", genre.getId());
                    return param;
                }).toArray(MapSqlParameterSource[]::new);
        namedParameterJdbcOperations.batchUpdate("INSERT INTO books_genres VALUES (:book_id, :genre_id)", params);
    }

    private void removeGenresRelationsFor(Book book) {
        namedParameterJdbcOperations.update("DELETE FROM books_genres WHERE book_id = :book_id",
                Map.of("book_id", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            Author author = new Author(rs.getLong("authorId"), rs.getString("full_name"));

            book.setId(rs.getLong("bookId"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(author);
            return book;
        }
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }
    }

    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }

            Book book = new Book();
            book.setId(rs.getLong("bookId"));
            book.setTitle(rs.getString("title"));

            Author author = new Author(rs.getLong("authorId"), rs.getString("full_name"));
            book.setAuthor(author);

            List<Genre> bookGenres = new ArrayList<>();
            do {
                bookGenres.add(new Genre(rs.getLong("genreId"), rs.getString("name")));
            } while (rs.next());
            book.setGenres(bookGenres);

            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}

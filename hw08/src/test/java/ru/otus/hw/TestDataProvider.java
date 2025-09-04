package ru.otus.hw;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.stream.IntStream;

public class TestDataProvider {

    public static List<BookDto> getDbBooks() {
        List<AuthorDto> authors = getDbAuthors();
        List<GenreDto> genres = getDbGenres();
        return getDbBooks(authors, genres);
    }

    public static List<CommentaryDto> getDbComments() {
        return getDbCommentaries(getDbBooks());
    }

    public static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id.toString(), "Author_" + id))
                .toList();
    }

    public static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id.toString(), "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id.toString(),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<CommentaryDto> getDbCommentaries(List<BookDto> books) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new CommentaryDto(id.toString(), books.get((id - 1) / 2), "Comment_" + id))
                .toList();
    }
}

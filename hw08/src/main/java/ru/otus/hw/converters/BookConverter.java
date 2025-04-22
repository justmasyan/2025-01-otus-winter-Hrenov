package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookDto bookToDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                authorConverter.authorToDto(book.getAuthor()),
                book.getGenres().stream().map(genreConverter::genreToDto).toList()
        );
    }

    public String bookDtoToString(BookDto book) {
        var genresString = book.getGenres().stream()
                .map(genreConverter::genreDtoToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));

        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorDtoToString(book.getAuthor()),
                genresString);
    }
}

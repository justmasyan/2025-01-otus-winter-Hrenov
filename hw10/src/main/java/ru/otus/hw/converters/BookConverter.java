package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

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

    public BookDto bookToBaseInfoDto(Book book) {
        return new BookDto(book.getId(), book.getTitle());
    }
}

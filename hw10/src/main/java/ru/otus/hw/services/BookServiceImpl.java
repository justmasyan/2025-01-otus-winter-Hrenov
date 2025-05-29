package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.AuthorNotFoundException;
import ru.otus.hw.exceptions.GenreNotFoundException;
import ru.otus.hw.exceptions.GenresIsEmptyException;
import ru.otus.hw.exceptions.NotEmptyIdInInsert;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        return bookOptional.map(bookConverter::bookToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookConverter::bookToDto).toList();
    }

    @Override
    @Transactional
    public BookDto insert(BookDto bookDto) {
        if (bookDto.getId() != 0) {
            throw new NotEmptyIdInInsert("Not empty id when insert book");
        }
        return save(bookDto);
    }

    @Override
    @Transactional
    public BookDto update(BookDto bookDto) {
        return save(bookDto);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private BookDto save(BookDto bookDto) {
        Set<Long> genresIds = bookDto.getGenres().stream()
                .map(GenreDto::getId)
                .collect(Collectors.toSet());

        if (isEmpty(genresIds)) {
            throw new GenresIsEmptyException("Genres ids must not be null");
        }

        Long authorId = bookDto.getAuthor().getId();
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id %d not found".formatted(authorId)));
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new GenreNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }
        Book book = bookRepository.save(new Book(bookDto.getId(), bookDto.getTitle(), author, genres));
        return bookConverter.bookToDto(book);
    }
}

package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentaryRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final CommentaryConverter commentaryConverter;

    private final BookRepository bookRepository;

    @Override
    public Optional<CommentaryDto> findById(String id) {
        return commentaryRepository.findById(id).map(commentaryConverter::commentaryToDto);
    }

    @Override
    public List<CommentaryDto> findAllByBookId(String bookId) {
        return commentaryRepository.findAllByBookId(bookId).stream()
                .map(commentaryConverter::commentaryToBaseInfoDto).toList();
    }

    @Override
    public CommentaryDto insert(String bookId, String text) {
        return save(null, bookId, text);
    }

    @Override
    public CommentaryDto update(String id, String bookId, String text) {
        return save(id, bookId, text);
    }

    @Override
    public void deleteById(String id) {
        commentaryRepository.deleteById(id);
    }

    private CommentaryDto save(String id, String bookId, String text) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException("Book with id %s not found".formatted(bookId))
        );
        Commentary savedCommentary = commentaryRepository.save(new Commentary(id, book, text));
        return commentaryConverter.commentaryToDto(savedCommentary);
    }
}

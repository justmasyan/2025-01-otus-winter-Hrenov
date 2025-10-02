package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public Optional<CommentaryDto> findById(long id) {
        return commentaryRepository.findById(id).map(commentaryConverter::commentaryToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentaryDto> findAllByBookId(long bookId) {
        return commentaryRepository.findAllByBookId(bookId).stream()
                .map(commentaryConverter::commentaryToDto).toList();
    }

    @Override
    @Transactional
    public CommentaryDto insert(long bookId, String text) {
        return save(0, bookId, text);
    }

    @Override
    @Transactional
    public CommentaryDto update(long id, long bookId, String text) {
        return save(id, bookId, text);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentaryRepository.deleteById(id);
    }

    private CommentaryDto save(long id, long bookId, String text) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException("Book with id %d not found".formatted(bookId))
        );
        Commentary savedCommentary = commentaryRepository.save(new Commentary(id, book, text));
        return commentaryConverter.commentaryToDto(savedCommentary);
    }
}

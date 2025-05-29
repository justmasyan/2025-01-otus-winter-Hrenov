package ru.otus.hw.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.CommentaryConverter;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.exceptions.BookNotFoundException;
import ru.otus.hw.exceptions.NotEmptyIdInInsert;
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
        return commentaryRepository.findById(id).map(commentaryConverter::commentaryToDtoWithoutBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentaryDto> findAllByBookId(long bookId) {
        return commentaryRepository.findAllByBookId(bookId).stream()
                .map(commentaryConverter::commentaryToDtoWithoutBook).toList();
    }

    @Override
    @Transactional
    public CommentaryDto insert(CommentaryDto commentaryDto) {
        if (commentaryDto.getId() != 0) {
            throw new NotEmptyIdInInsert("Not empty id when insert book");
        }
        return save(commentaryDto);
    }

    @Override
    @Transactional
    public CommentaryDto update(CommentaryDto commentaryDto) {
        return save(commentaryDto);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentaryRepository.deleteById(id);
    }

    private CommentaryDto save(CommentaryDto commentaryDto) {
        long bookId = commentaryDto.getBook().getId();

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException("Book with id %d not found".formatted(bookId))
        );

        Commentary commentaryForSave = new Commentary(commentaryDto.getId(), book, commentaryDto.getText());
        Commentary savedCommentary = commentaryRepository.save(commentaryForSave);
        return commentaryConverter.commentaryToDtoWithoutBook(savedCommentary);
    }
}

package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequiredArgsConstructor
public class CommentaryServiceImpl implements CommentaryService {

    private final CommentaryRepository commentaryRepository;

    private final CommentaryConverter commentaryConverter;

    private final PermissionService permissionService;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.CommentaryDto', 'READ')")
    public Optional<CommentaryDto> findById(long id) {
        return commentaryRepository.findById(id).map(commentaryConverter::commentaryToDto);
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<CommentaryDto> findAllByBookId(long bookId) {
        return commentaryRepository.findAllByBookId(bookId).stream()
                .map(commentaryConverter::commentaryToDto).toList();
    }

    @Override
    @Transactional
    @Secured({"ROLE_ADMIN"})
    public CommentaryDto insert(long bookId, String text) {
        CommentaryDto commentaryDto = save(0, bookId, text);
        permissionService.createDefaultPermissions(commentaryDto);
        return commentaryDto;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.CommentaryDto', 'WRITE')")
    public CommentaryDto update(long id, long bookId, String text) {
        return save(id, bookId, text);
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id, 'ru.otus.hw.dto.CommentaryDto', 'DELETE')")
    public void deleteById(long id) {
        commentaryRepository.findById(id).ifPresent(comment -> {
            commentaryRepository.delete(comment);
            permissionService.deletePermissions(comment);
        });
    }

    private CommentaryDto save(long id, long bookId, String text) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException("Book with id %d not found".formatted(bookId))
        );
        Commentary savedCommentary = commentaryRepository.save(new Commentary(id, book, text));
        return commentaryConverter.commentaryToDto(savedCommentary);
    }
}

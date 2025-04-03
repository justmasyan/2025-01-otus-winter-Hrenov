package ru.otus.hw.services;

import ru.otus.hw.entities.CommentaryDto;

import java.util.List;
import java.util.Optional;

public interface CommentaryService {

    Optional<CommentaryDto> findById(long id);

    List<CommentaryDto> findAllByBookId(long bookId);

    CommentaryDto insert(long bookId, String text);

    CommentaryDto update(long id, long bookId, String text);

    void deleteById(long id);
}

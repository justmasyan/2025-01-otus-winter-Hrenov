package ru.otus.hw.services;

import ru.otus.hw.dto.CommentaryDto;

import java.util.List;
import java.util.Optional;

public interface CommentaryService {

    Optional<CommentaryDto> findById(String id);

    List<CommentaryDto> findAllByBookId(String bookId);

    CommentaryDto insert(String bookId, String text);

    CommentaryDto update(String id, String bookId, String text);

    void deleteById(String id);
}

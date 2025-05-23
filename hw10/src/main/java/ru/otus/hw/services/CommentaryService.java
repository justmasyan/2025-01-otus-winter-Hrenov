package ru.otus.hw.services;

import ru.otus.hw.dto.CommentaryDto;

import java.util.List;
import java.util.Optional;

public interface CommentaryService {

    Optional<CommentaryDto> findById(long id);

    List<CommentaryDto> findAllByBookId(long bookId);

    CommentaryDto save(CommentaryDto commentaryDto);

    void deleteById(long id);
}

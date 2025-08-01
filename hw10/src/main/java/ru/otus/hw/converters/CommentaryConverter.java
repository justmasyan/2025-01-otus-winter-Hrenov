package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.models.Commentary;

@Component
@RequiredArgsConstructor
public class CommentaryConverter {

    private final BookConverter bookConverter;

    public CommentaryDto commentaryToDto(Commentary commentary) {
        return new CommentaryDto(commentary.getId(),
                bookConverter.bookToBaseInfoDto(commentary.getBook()),
                commentary.getText());
    }

    public CommentaryDto commentaryToDtoWithoutBook(Commentary commentary) {
        return new CommentaryDto(commentary.getId(), commentary.getText());
    }
}

package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.entities.CommentaryDto;
import ru.otus.hw.models.Commentary;

@Component
public class CommentaryConverter {

    public CommentaryDto commentaryToDto(Commentary commentary) {
        return new CommentaryDto(commentary.getId(), commentary.getBookId(), commentary.getText());
    }

    public String commentaryDtoToString(CommentaryDto commentary) {
        return "Id: %d, BookId: %d, Text: %s".formatted(
                commentary.getId(),
                commentary.getBookId(),
                commentary.getText()
        );
    }
}

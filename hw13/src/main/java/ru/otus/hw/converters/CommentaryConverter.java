package ru.otus.hw.converters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.models.Commentary;

@Component
@AllArgsConstructor
public class CommentaryConverter {

    public CommentaryDto commentaryToDto(Commentary commentary) {
        return new CommentaryDto(commentary.getId(), commentary.getText());
    }

    public String commentaryDtoToString(CommentaryDto commentary) {
        return "Id: %d, Text: %s".formatted(
                commentary.getId(),
                commentary.getText()
        );
    }
}

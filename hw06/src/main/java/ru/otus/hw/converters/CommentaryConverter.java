package ru.otus.hw.converters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentaryDto;
import ru.otus.hw.models.Commentary;

@Component
@AllArgsConstructor
public class CommentaryConverter {

    private final BookConverter bookConverter;

    public CommentaryDto commentaryToDto(Commentary commentary) {
        BookDto bookDto = bookConverter.bookToDto(commentary.getBook());
        return new CommentaryDto(commentary.getId(), bookDto, commentary.getText());
    }

    public String commentaryDtoToString(CommentaryDto commentary) {

        String bookString = bookConverter.bookDtoToString(commentary.getBook());
        return "Id: %d, Book: %s, Text: %s".formatted(
                commentary.getId(),
                bookString,
                commentary.getText()
        );
    }
}

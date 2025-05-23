package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentaryDto {

    private long id;

    BookDto book;

    private String text;

    public CommentaryDto(long id, String text) {
        this.id = id;
        this.text = text;
    }
}

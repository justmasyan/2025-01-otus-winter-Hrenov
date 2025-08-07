package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentaryDto {

    private String id;

    private BookDto book;

    private String text;

    public CommentaryDto(String id, String text) {
        this.id = id;
        this.text = text;
    }
}

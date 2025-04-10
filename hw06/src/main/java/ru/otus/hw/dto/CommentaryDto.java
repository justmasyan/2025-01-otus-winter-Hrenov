package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentaryDto {

    private long id;

    private BookDto book;

    private String text;
}

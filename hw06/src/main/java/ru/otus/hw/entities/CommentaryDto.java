package ru.otus.hw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentaryDto {

    private long id;

    private long bookId;

    private String text;
}

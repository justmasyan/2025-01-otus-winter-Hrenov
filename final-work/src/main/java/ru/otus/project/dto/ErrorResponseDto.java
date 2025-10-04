package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponseDto {

    private String message;

    private Date moment;
}

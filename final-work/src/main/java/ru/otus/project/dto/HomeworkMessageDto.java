package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class HomeworkMessageDto {

    private long id;

    private String text;

    private Date sendTime;

    private AccountDto sender;
}

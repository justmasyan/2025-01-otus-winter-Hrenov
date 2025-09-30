package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.project.domain.Homework;

import java.util.Date;

@Data
@AllArgsConstructor
public class HomeworkMessageDto {

    private long id;

    private String text;

    private Date sendTime;

    private AccountDto sender;

    private HomeworkDto homework;

    public HomeworkMessageDto(long id, String text, Date sendTime, AccountDto sender) {
        this.id = id;
        this.text = text;
        this.sendTime = sendTime;
        this.sender = sender;
    }
}

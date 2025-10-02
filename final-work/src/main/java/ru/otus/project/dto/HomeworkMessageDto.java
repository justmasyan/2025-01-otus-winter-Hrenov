package ru.otus.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.domain.Homework;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkMessageDto {

    private long id;

    private String text;

    private Date sendTime;

    private String senderDesc;

    private AccountDto sender;

    private HomeworkDto homework;

    public HomeworkMessageDto(long id, String text, Date sendTime, String senderDesc, AccountDto sender) {
        this.id = id;
        this.text = text;
        this.sendTime = sendTime;
        this.senderDesc = senderDesc;
        this.sender = sender;
    }
}

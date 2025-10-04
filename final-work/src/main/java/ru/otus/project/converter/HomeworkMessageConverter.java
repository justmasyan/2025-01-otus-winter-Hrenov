package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.HomeworkMessage;
import ru.otus.project.dto.HomeworkMessageDto;

@Component
@RequiredArgsConstructor
public class HomeworkMessageConverter {

    private final AccountConverter accountConverter;

    public HomeworkMessageDto entityToWithAccountDto(HomeworkMessage message) {
        return new HomeworkMessageDto(message.getId(),
                message.getText(),
                message.getSendTime(),
                message.getSenderDesc(),
                accountConverter.entityToBaseInfoDto(message.getSender())
        );
    }
}

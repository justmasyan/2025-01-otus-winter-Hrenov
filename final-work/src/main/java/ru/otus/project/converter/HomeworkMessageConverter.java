package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.HomeworkMessage;
import ru.otus.project.dto.HomeworkMessageDto;

@Component
@RequiredArgsConstructor
public class HomeworkMessageConverter {

    private final AccountConverter accountConverter;

    public HomeworkMessageDto entityToDto(HomeworkMessage message) {
        return new HomeworkMessageDto(message.getId(),
                message.getText(),
                message.getSendTime(),
                accountConverter.entityToBaseInfoDto(message.getSender())
        );
    }
}

package ru.otus.project.converter;

import org.springframework.stereotype.Component;
import ru.otus.project.domain.HomeworkStatus;
import ru.otus.project.dto.HomeworkStatusDto;
import ru.otus.project.enums.HomeworkStatusEnum;

@Component
public class HomeworkStatusConverter {

    public HomeworkStatus enumToEntity(HomeworkStatusEnum homeworkStatus) {
        return new HomeworkStatus(homeworkStatus.id, homeworkStatus.text);
    }

    public HomeworkStatusDto entityToDto(HomeworkStatus homeworkStatus) {
        return new HomeworkStatusDto(homeworkStatus.getId(), homeworkStatus.getText());
    }
}

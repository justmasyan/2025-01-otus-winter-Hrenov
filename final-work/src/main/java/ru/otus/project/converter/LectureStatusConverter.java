package ru.otus.project.converter;

import org.springframework.stereotype.Component;
import ru.otus.project.domain.LectureStatus;
import ru.otus.project.dto.LectureStatusDto;
import ru.otus.project.enums.LectureStatusEnum;

@Component
public class LectureStatusConverter {

    public LectureStatusDto entityToDto(LectureStatus status) {
        return new LectureStatusDto(status.getId(), status.getText());
    }

    public LectureStatus dtoToEntity(LectureStatusDto status) {
        return new LectureStatus(status.getId(), status.getText());
    }

    public LectureStatus enumToEntity(LectureStatusEnum status) {
        return new LectureStatus(status.id, status.text);
    }
}

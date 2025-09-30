package ru.otus.project.converter;

import org.springframework.stereotype.Component;
import ru.otus.project.domain.LectureStatus;
import ru.otus.project.dto.LectureStatusDto;

@Component
public class LectureStatusConverter {

    public LectureStatusDto entityToDto(LectureStatus status) {
        return new LectureStatusDto(status.getId(), status.getText());
    }

}

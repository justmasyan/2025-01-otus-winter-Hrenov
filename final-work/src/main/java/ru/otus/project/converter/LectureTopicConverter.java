package ru.otus.project.converter;

import org.springframework.stereotype.Component;
import ru.otus.project.domain.LectureTopic;
import ru.otus.project.dto.LectureTopicDto;

@Component
public class LectureTopicConverter {

    public LectureTopicDto entityToDto(LectureTopic lectureTopic) {
        return new LectureTopicDto(lectureTopic.getId(), lectureTopic.getTitle());
    }
}

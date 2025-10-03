package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.Lecture;
import ru.otus.project.dto.LectureDto;

@Component
@RequiredArgsConstructor
public class LectureConverter {

    private final LectureTopicConverter topicConverter;

    private final StudyGroupConverter studyGroupConverter;

    private final LectorConverter lectorConverter;

    private final LectureStatusConverter statusConverter;

    public LectureDto entityToWithStatusTopicDto(Lecture lecture) {
        return new LectureDto(lecture.getId(), lecture.getUrl(),
                lecture.getLectureTime(),
                statusConverter.entityToDto(lecture.getStatus()),
                topicConverter.entityToDto(lecture.getLectureTopic()));
    }

    public LectureDto entityToDtoForLector(Lecture lecture) {
        return new LectureDto(lecture.getId(), lecture.getUrl(),
                lecture.getLectureTime(),
                statusConverter.entityToDto(lecture.getStatus()),
                topicConverter.entityToDto(lecture.getLectureTopic()),
                studyGroupConverter.entityToBaseInfoDto(lecture.getStudyGroup()));
    }

    public LectureDto entityToDtoForStudent(Lecture lecture) {
        return new LectureDto(lecture.getId(), lecture.getUrl(),
                lecture.getLectureTime(),
                topicConverter.entityToDto(lecture.getLectureTopic()),
                studyGroupConverter.entityToBaseInfoDto(lecture.getStudyGroup()));
    }

    public LectureDto entityToFullDto(Lecture lecture) {
        return new LectureDto(lecture.getId(), lecture.getUrl(),
                lecture.getLectureTime(),
                statusConverter.entityToDto(lecture.getStatus()),
                topicConverter.entityToDto(lecture.getLectureTopic()),
                lectorConverter.entityToBaseInfoDto(lecture.getLector()),
                studyGroupConverter.entityToBaseInfoDto(lecture.getStudyGroup()));
    }
}

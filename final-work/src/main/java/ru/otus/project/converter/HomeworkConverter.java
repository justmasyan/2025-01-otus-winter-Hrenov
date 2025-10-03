package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.Homework;
import ru.otus.project.dto.HomeworkDto;

@Component
@RequiredArgsConstructor
public class HomeworkConverter {

    private final HomeworkStatusConverter statusConverter;

    private final StudentConverter studentConverter;

    private final LectureConverter lectureConverter;

    private final LectorConverter lectorConverter;

    public HomeworkDto entityToBaseInfoDto(Homework homework) {
        return new HomeworkDto(homework.getId(), homework.getMark(),
                statusConverter.entityToDto(homework.getStatus()));
    }

    public HomeworkDto entityToWithStudentDto(Homework homework) {
        return new HomeworkDto(homework.getId(), homework.getMark(),
                statusConverter.entityToDto(homework.getStatus()),
                studentConverter.entityToBaseInfoDto(homework.getStudent()));
    }

    public HomeworkDto entityToDtoForLector(Homework homework) {
        return new HomeworkDto(homework.getId(), homework.getMark(),
                statusConverter.entityToDto(homework.getStatus()),
                lectureConverter.entityToWithStatusTopicDto(homework.getLecture()),
                studentConverter.entityToBaseInfoDto(homework.getStudent()));
    }

    public HomeworkDto entityToDtoForStudent(Homework homework) {
        return new HomeworkDto(homework.getId(), homework.getMark(),
                statusConverter.entityToDto(homework.getStatus()),
                lectureConverter.entityToDtoForStudent(homework.getLecture()));
    }

    public HomeworkDto entityToDtoForLecture(Homework homework) {
        return new HomeworkDto(homework.getId(), homework.getMark(),
                statusConverter.entityToDto(homework.getStatus()),
                studentConverter.entityToBaseInfoDto(homework.getStudent()),
                lectorConverter.entityToDto(homework.getLector()));
    }

    public HomeworkDto entityToDto(Homework homework) {
        return new HomeworkDto(homework.getId(), homework.getMark(),
                statusConverter.entityToDto(homework.getStatus()),
                lectureConverter.entityToWithStatusTopicDto(homework.getLecture()),
                studentConverter.entityToDto(homework.getStudent()),
                lectorConverter.entityToDto(homework.getLector()));
    }
}

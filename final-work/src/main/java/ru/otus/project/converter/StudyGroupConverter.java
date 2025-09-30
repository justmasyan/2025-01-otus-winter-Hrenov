package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.StudyGroup;
import ru.otus.project.dto.StudyGroupDto;

@Component
@RequiredArgsConstructor
public class StudyGroupConverter {

    private final CourseConverter courseConverter;


    public StudyGroupDto entityToBaseInfoDto(StudyGroup studyGroup) {
        return new StudyGroupDto(studyGroup.getId(), studyGroup.getCode(), studyGroup.getStartDate());
    }
    public StudyGroupDto entityWithCourseDto(StudyGroup studyGroup){
        return new StudyGroupDto(studyGroup.getId(), studyGroup.getCode(), studyGroup.getStartDate(),
                courseConverter.entityToMinDto(studyGroup.getCourse()));
    }
}

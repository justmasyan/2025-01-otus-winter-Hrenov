package ru.otus.project.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.project.domain.Course;
import ru.otus.project.dto.CourseDto;

@Component
@RequiredArgsConstructor
public class CourseConverter {

    private final LectureTopicConverter lectureTopicConverter;

    public CourseDto entityToMinDto(Course course) {
        return new CourseDto(course.getId(), course.getTitle(), course.getPrice());
    }

    public CourseDto entityToWithTopicDto(Course course) {
        return new CourseDto(course.getId(), course.getTitle(), course.getPrice(),
                course.getLectureTopics().stream().map(lectureTopicConverter::entityToDto).toList()
        );
    }
}

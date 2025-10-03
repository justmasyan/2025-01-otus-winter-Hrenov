package ru.otus.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.converter.CourseConverter;
import ru.otus.project.domain.Course;
import ru.otus.project.domain.Lector;
import ru.otus.project.domain.LectureTopic;
import ru.otus.project.dto.CourseDto;
import ru.otus.project.dto.LectureTopicDto;
import ru.otus.project.exception.CourseNotFoundException;
import ru.otus.project.exception.EmptyTopicsException;
import ru.otus.project.exception.LectorAlreadyInCourseException;
import ru.otus.project.exception.LectorNotFoundException;
import ru.otus.project.exception.LectorNotInCourseException;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.exception.TopicNotFoundException;
import ru.otus.project.repository.CourseRepository;
import ru.otus.project.repository.LectorRepository;
import ru.otus.project.repository.LectureTopicRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final LectureServiceImpl lectureService;

    private final HomeworkServiceImpl homeworkService;

    private final LectorRepository lectorRepository;

    private final CourseRepository courseRepository;

    private final LectureTopicRepository topicRepository;

    private final CourseConverter courseConverter;

    @Override
    @Transactional(readOnly = true)
    public CourseDto findById(long id) {
        return courseRepository.findById(id).map(courseConverter::entityToWithTopicDto)
                .orElseThrow(() -> new CourseNotFoundException("Курса с таким id не существует"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream()
                .map(courseConverter::entityToMinDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> findAllByTopicId(long topicId) {
        return courseRepository.findAllByLectureTopicsId(topicId).stream()
                .map(courseConverter::entityToMinDto).toList();
    }

    @Override
    @Transactional
    public CourseDto createCourse(CourseDto courseDto, long lectorAccountId) {
        if (courseDto.getId() != 0) {
            throw new NoZeroIdException("Id должен равнять 0");
        }

        Set<Long> topicIds = courseDto.getLectureTopics().stream()
                .map(LectureTopicDto::getId)
                .collect(Collectors.toSet());

        if (isEmpty(topicIds)) {
            throw new EmptyTopicsException("Темы лекции не должны быть пустыми");
        }

        List<LectureTopic> topics = topicRepository.findAllById(topicIds);
        if (isEmpty(topics) || topicIds.size() != topics.size()) {
            throw new TopicNotFoundException("Тема лекции не найдена");
        }

        Lector lector = lectorRepository.findByAccountId(lectorAccountId)
                .orElseThrow(() -> new LectorNotFoundException("Преподаватель не найден"));

        Course course = new Course(courseDto.getId(),
                courseDto.getTitle(), courseDto.getPrice(),
                topics, List.of(lector));
        Course savedCourse = courseRepository.save(course);
        return courseConverter.entityToWithTopicDto(savedCourse);
    }

    @Override
    @Transactional
    public void addLectorToCourse(long accountId, long courseId) {
        Lector lector = lectorRepository.findByAccountId(accountId)
                .orElseThrow(() -> new LectorNotFoundException("Лектор не существует"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Курс не существует"));

        if (course.getLectors().contains(lector)) {
            throw new LectorAlreadyInCourseException("Вы уже преподаете на этом курсе");
        }

        course.getLectors().add(lector);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteLectorFromCourse(long accountId, long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Такого курса не существует"));

        Lector lector = lectorRepository.findByAccountId(accountId)
                .orElseThrow(() -> new LectorNotFoundException("Такого преподавателя не существует"));

        if (!course.getLectors().contains(lector)) {
            throw new LectorNotInCourseException("Вы не преподаете на этом курсе");
        }

        course.getLectors().remove(lector);

        if (course.getLectors().isEmpty()) {
            courseRepository.delete(course);
            return;
        }

        lectureService.removeLectorForCourseLectures(course, lector);
        homeworkService.removeLectorForCourseHomeworks(course, lector);
        courseRepository.save(course);
    }
}

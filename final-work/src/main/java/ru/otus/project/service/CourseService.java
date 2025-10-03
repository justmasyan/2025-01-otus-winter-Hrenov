package ru.otus.project.service;

import ru.otus.project.dto.CourseDto;

import java.util.List;

public interface CourseService {


    CourseDto findById(long id);

    List<CourseDto> findAll();

    List<CourseDto> findAllByTopicId(long topicId);

    CourseDto createCourse(CourseDto courseDto, long lectorAccountId);

    void addLectorToCourse(long accountId, long courseId);

    void deleteLectorFromCourse(long accountId, long courseId);
}

package ru.otus.project.service;

import ru.otus.project.dto.CourseDto;

import java.util.List;

public interface CourseService {

    CourseDto findById(long id);

    List<CourseDto> findAllBaseInfo();
}

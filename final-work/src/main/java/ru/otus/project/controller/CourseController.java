package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.CourseDto;
import ru.otus.project.service.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("api/courses/{id}")
    public CourseDto findCourseById(@PathVariable long id) {
        return courseService.findById(id);
    }

    @GetMapping("api/courses")
    public List<CourseDto> findAllCourses() {
        return courseService.findAllBaseInfo();
    }
}

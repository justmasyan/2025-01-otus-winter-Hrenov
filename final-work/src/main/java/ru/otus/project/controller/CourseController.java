package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.CourseDto;
import ru.otus.project.service.CourseServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @GetMapping("api/courses/{id}")
    public CourseDto findCourseById(@PathVariable long id) {
        return courseService.findById(id);
    }

    @GetMapping("api/courses")
    public List<CourseDto> findAllCourses() {
        return courseService.findAllBaseInfo();
    }

    @PostMapping("api/courses/{courseId}/lector")
    public void addLectorToCourse(@AuthenticationPrincipal AccountDto accountDto,
                                  @PathVariable long courseId) {
        courseService.addLectorToCourse(accountDto.getId(), courseId);
    }

    @DeleteMapping("api/courses/{courseId}/lector")
    public void deleteLectorFromCourse(@AuthenticationPrincipal AccountDto accountDto,
                                       @PathVariable long courseId) {
        courseService.deleteLectorFromCourse(accountDto.getId(), courseId);
    }
}

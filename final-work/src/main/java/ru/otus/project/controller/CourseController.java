package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.CourseDto;
import ru.otus.project.service.CourseServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @GetMapping("api/courses/{id}")
    public CourseDto findById(@PathVariable long id) {
        return courseService.findById(id);
    }

    @GetMapping("api/courses")
    public List<CourseDto> findAll() {
        return courseService.findAllBaseInfo();
    }

    @GetMapping("api/courses/lecture-topics/{topicId}")
    public List<CourseDto> findAllByTopicId(@PathVariable long topicId) {
        return courseService.findAllByTopicId(topicId);
    }

    @PostMapping("api/courses/new")
    public CourseDto findAllByTopicId(@AuthenticationPrincipal Jwt jwt,
                                      @RequestBody CourseDto courseDto) {
        return courseService.createCourse(courseDto,jwt.getClaim("accountId"));
    }

    @PostMapping("api/courses/{courseId}/lector")
    public void addLectorToCourse(@AuthenticationPrincipal Jwt jwt,
                                  @PathVariable long courseId) {
        courseService.addLectorToCourse(jwt.getClaim("accountId"), courseId);
    }

    @DeleteMapping("api/courses/{courseId}/lector")
    public void deleteLectorFromCourse(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable long courseId) {
        courseService.deleteLectorFromCourse(jwt.getClaim("accountId"), courseId);
    }
}

package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.HomeworkDto;
import ru.otus.project.service.HomeworkServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkServiceImpl homeworkService;

    @GetMapping("api/homeworks/{id}")
    public HomeworkDto findById(@PathVariable long id) {
        return homeworkService.findById(id);
    }

    @GetMapping("api/homeworks/lector")
    public List<HomeworkDto> findAllByLector(@AuthenticationPrincipal Jwt jwt) {
        return homeworkService.findAllByLectorId(jwt.getClaim("accountId"));
    }

    @GetMapping("api/homeworks/lectures/{lectureId}")
    public List<HomeworkDto> findAllByLectureId(@PathVariable long lectureId) {
        return homeworkService.findAllByLectureId(lectureId);
    }

    @PostMapping("api/homeworks/{id}/lector")
    public void assignLectorToHomework(@AuthenticationPrincipal Jwt jwt,
                                       @PathVariable long id) {
        homeworkService.assignLectorToHomework(id, jwt.getClaim("accountId"));
    }

    @DeleteMapping("api/homeworks/{id}/lector")
    public void removeLectorFromHomework(@AuthenticationPrincipal Jwt jwt,
                                         @PathVariable long id) {
        homeworkService.removeLectorFromHomework(id, jwt.getClaim("accountId"));
    }

    @PutMapping("api/homeworks/{id}")
    public HomeworkDto updateHomework(@AuthenticationPrincipal Jwt jwt,
                                      @RequestBody HomeworkDto homeworkDto) {
        return homeworkService.updateHomework(homeworkDto, jwt.getClaim("accountId"));
    }

    @GetMapping("api/homeworks/lectures/{lectureId}/student")
    public HomeworkDto findByLectureIdForStudent(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable long lectureId) {
        return homeworkService.findByLectureIdAndStudentId(lectureId, jwt.getClaim("accountId"));
    }

    @GetMapping("api/homeworks/lectures/{lectureId}/lector")
    public List<HomeworkDto> findAllByLectureIdForLector(@AuthenticationPrincipal Jwt jwt,
                                                         @PathVariable long lectureId) {
        return homeworkService.findByLectureIdAndLectorId(lectureId, jwt.getClaim("accountId"));
    }
}

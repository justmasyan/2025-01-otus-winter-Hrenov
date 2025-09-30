package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.HomeworkDto;
import ru.otus.project.service.HomeworkServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkServiceImpl homeworkService;

    @GetMapping("/api/homeworks/{id}")
    public HomeworkDto findById(@PathVariable long id) {
        return homeworkService.findById(id);
    }

    @GetMapping("/api/homeworks/lector")
    public List<HomeworkDto> findAllByLector(@AuthenticationPrincipal AccountDto accountDto) {
        return homeworkService.findAllByLectorId(accountDto.getId());
    }

    @GetMapping("/api/homeworks/lectures/{lectureId}/student")
    public HomeworkDto findByLectureIdForStudent(@AuthenticationPrincipal AccountDto accountDto,
                                                 @PathVariable long lectureId) {
        return homeworkService.findByLectureIdAndStudentId(lectureId, accountDto.getId());
    }

    @GetMapping("/api/homeworks/lectures/{lectureId}/lector")
    public List<HomeworkDto> findAllByLectureIdForLector(@AuthenticationPrincipal AccountDto accountDto,
                                                         @PathVariable long lectureId) {
        return homeworkService.findByLectureIdAndLectorId(lectureId, accountDto.getId());
    }
}

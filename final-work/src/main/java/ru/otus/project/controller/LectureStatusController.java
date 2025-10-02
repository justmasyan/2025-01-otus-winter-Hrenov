package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.LectureStatusDto;
import ru.otus.project.service.LectureStatusServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectureStatusController {

    private final LectureStatusServiceImpl statusService;

    @GetMapping("api/lecture-statuses")
    public List<LectureStatusDto> findAll() {
        return statusService.findAll();
    }
}

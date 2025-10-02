package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.HomeworkStatusDto;
import ru.otus.project.service.HomeworkStatusServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeworkStatusController {

    private final HomeworkStatusServiceImpl statusService;

    @GetMapping("api/homework-statuses")
    public List<HomeworkStatusDto> findAll() {
        return statusService.findAll();
    }
}

package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.HomeworkMessageDto;
import ru.otus.project.service.HomeworkMessageServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeworkMessageController {

    private final HomeworkMessageServiceImpl messageService;

    @GetMapping("api/messages/homeworks/{homeworkId}")
    public List<HomeworkMessageDto> findAllByHomeworkId(@PathVariable long homeworkId) {
        return messageService.findAllByHomeworkId(homeworkId);
    }
}

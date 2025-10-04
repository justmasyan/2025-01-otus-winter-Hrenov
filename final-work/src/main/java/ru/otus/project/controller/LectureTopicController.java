package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.LectureTopicDto;
import ru.otus.project.service.LectureTopicService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectureTopicController {

    private final LectureTopicService topicService;

    @GetMapping("api/lecture-topics")
    public List<LectureTopicDto> findAll() {
        return topicService.findAll();
    }

    @PostMapping("api/lecture-topics/new")
    public LectureTopicDto insertTopic(@RequestBody LectureTopicDto topicDto) {
        return topicService.insert(topicDto);
    }
}

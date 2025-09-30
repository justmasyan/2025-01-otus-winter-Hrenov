package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.LectureDto;
import ru.otus.project.service.LectureServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureServiceImpl lectureService;

    @GetMapping("api/lectures/{id}")
    public LectureDto findById(@PathVariable long id) {
        return lectureService.findById(id);
    }

    @GetMapping("api/lectures/groups/{groupId}")
    public List<LectureDto> findAllByGroupId(@PathVariable long groupId) {
        return lectureService.findAllByGroupId(groupId);
    }

    @GetMapping("api/lectures/lector")
    public List<LectureDto> findAllByLectorId(@AuthenticationPrincipal AccountDto accountDto) {
        return lectureService.findAllByLectorAccountId(accountDto.getId());
    }
}

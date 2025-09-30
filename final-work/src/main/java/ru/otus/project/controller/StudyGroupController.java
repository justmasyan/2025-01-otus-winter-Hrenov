package ru.otus.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.StudyGroupDto;
import ru.otus.project.service.StudyGroupServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupServiceImpl studyGroupService;

    @GetMapping("api/groups/{id}")
    public StudyGroupDto findById(@PathVariable long id) {
        return studyGroupService.findById(id);
    }

    @GetMapping("api/groups/students/accounts/{accountId}")
    public List<StudyGroupDto> findAllByStudentAccountId(@PathVariable long accountId) {
        return studyGroupService.findAllByStudentAccountId(accountId);
    }

    @GetMapping("api/groups/courses/{courseId}")
    public List<StudyGroupDto> findAllByCourseId(@PathVariable long courseId) {
        return studyGroupService.findAllByCourseId(courseId);
    }

    @PostMapping("api/groups/{id}/students/new")
    public StudyGroupDto addStudentToGroup(@AuthenticationPrincipal AccountDto accountDto,
                                           @PathVariable long id) {
        return studyGroupService.addStudentToGroup(id, accountDto.getId());
    }
}

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
import ru.otus.project.dto.StudyGroupDto;
import ru.otus.project.service.StudyGroupService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

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

    @PostMapping("api/groups/new")
    public StudyGroupDto addStudentToGroup(@AuthenticationPrincipal Jwt jwt,
                                           @RequestBody StudyGroupDto studyGroupDto) {
        return studyGroupService.insertGroup(studyGroupDto, jwt.getClaim("accountId"));
    }

    @PostMapping("api/groups/{id}/student")
    public StudyGroupDto addStudentToGroup(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable long id) {
        return studyGroupService.addStudentToGroup(id, jwt.getClaim("accountId"));
    }

    @DeleteMapping("api/groups/{id}/student")
    public StudyGroupDto removeStudentFromGroup(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable long id) {
        return studyGroupService.removeStudentFromGroup(id, jwt.getClaim("accountId"));
    }
}

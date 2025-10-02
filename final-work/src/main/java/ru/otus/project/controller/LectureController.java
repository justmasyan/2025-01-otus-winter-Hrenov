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
    public List<LectureDto> findAllByLectorId(@AuthenticationPrincipal Jwt jwt) {
        return lectureService.findAllByLectorAccountId(jwt.getClaim("accountId"));
    }

    @PutMapping("api/lectures/{id}")
    public LectureDto updateLecture(@AuthenticationPrincipal Jwt jwt,
                                    @RequestBody LectureDto lectureDto) {
        return lectureService.updateLectureByAccount(lectureDto, jwt.getClaim("accountId"));
    }

    @PostMapping("api/lectures/{id}/lector")
    public void assignLectorToLecture(@AuthenticationPrincipal Jwt jwt,
                                      @PathVariable long id) {
        lectureService.assignLectorOnLecture(id, jwt.getClaim("accountId"));
    }

    @DeleteMapping("api/lectures/{id}/lector")
    public void removeLectorFromLecture(@AuthenticationPrincipal Jwt jwt,
                                        @PathVariable long id) {
        lectureService.removeLectorFromLecture(id, jwt.getClaim("accountId"));
    }
}

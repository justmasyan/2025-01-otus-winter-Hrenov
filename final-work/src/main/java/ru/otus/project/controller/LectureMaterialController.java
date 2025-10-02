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
import ru.otus.project.dto.LectureMaterialDto;
import ru.otus.project.service.LectureMaterialServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectureMaterialController {

    private final LectureMaterialServiceImpl materialService;

    @GetMapping("api/lecture-materials/lectures/{lectureId}")
    public List<LectureMaterialDto> findAllByLectureId(@PathVariable long lectureId) {
        return materialService.findAllByLectureId(lectureId);
    }

    @PostMapping("api/lecture-materials/new")
    public LectureMaterialDto insertMaterial(@AuthenticationPrincipal Jwt jwt,
                                             @RequestBody LectureMaterialDto materialDto) {
        return materialService.insertMaterial(materialDto, jwt.getClaim("accountId"));
    }

    @DeleteMapping("api/lecture-materials/{id}")
    public void deleteMaterial(@PathVariable long id) {
        materialService.deleteMaterial(id);
    }
}

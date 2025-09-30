package ru.otus.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.dto.LectorDto;
import ru.otus.project.service.LectorServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LectorController {

    private final LectorServiceImpl lectorService;

    @GetMapping("/api/lectors/accounts/{accountId}")
    public LectorDto findByAccountId(@PathVariable long accountId) {
        return lectorService.findByAccountId(accountId);
    }

    @GetMapping("/api/lectors/courses/{courseId}")
    public List<LectorDto> findAllByCourseId(@PathVariable long courseId) {
        return lectorService.findAllByCourseId(courseId);
    }

    @PostMapping("api/lectors/new")
    public LectorDto createStudent(@Valid @RequestBody LectorDto lectorDto) {
        return lectorService.insert(lectorDto);
    }

    @PostMapping("api/lectors/courses/{courseId}")
    public LectorDto addLectorToCourse(@AuthenticationPrincipal AccountDto accountDto,
                                       @PathVariable long courseId) {
        return lectorService.moveLectorToCourse(accountDto.getId(), courseId);
    }

    @DeleteMapping("api/lectors/courses/{courseId}")
    public LectorDto deleteLectorFromCourse(@AuthenticationPrincipal AccountDto accountDto,
                                            @PathVariable long courseId) {
        return lectorService.deleteLectorFromCourse(accountDto.getId(), courseId);
    }
}
